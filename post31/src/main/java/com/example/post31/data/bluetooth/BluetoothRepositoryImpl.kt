package com.example.post31.data.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import com.example.post31.domain.bluetooth.BluetoothConnectionStatus
import com.example.post31.domain.bluetooth.BluetoothDevice
import com.example.post31.domain.bluetooth.BluetoothRepository
import com.example.post31.domain.bluetooth.BluetoothState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class BluetoothRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : BluetoothRepository {

    private val bluetoothManager = context.getSystemService(
        Context.BLUETOOTH_SERVICE
    ) as BluetoothManager

    private val bluetoothAdapter = bluetoothManager.adapter
    private val bluetoothLeAdvertiser = bluetoothAdapter?.bluetoothLeAdvertiser
    private val mainHandler = Handler(Looper.getMainLooper())

    private var bluetoothSocket: BluetoothSocket? = null
    private var connectionThread: Thread? = null

    private val advertiseCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
            setAdvertising(true)
        }

        override fun onStartFailure(errorCode: Int) {
            setAdvertising(false)
        }
    }

    private val discoveryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                android.bluetooth.BluetoothDevice.ACTION_FOUND -> {
                    @Suppress("DEPRECATION")
                    val device = intent.getParcelableExtra<android.bluetooth.BluetoothDevice>(
                        android.bluetooth.BluetoothDevice.EXTRA_DEVICE)

                    if (device != null) {
                        _state.update { current ->
                            if (current.devices.any { it.address == device.address }) current
                            else current.copy(devices = current.devices + device)
                        }
                    }
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> setScanning(false)
            }
        }
    }

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    private val _state = MutableStateFlow(BluetoothStateImpl())
    override val state = _state.map {
        BluetoothState(
            devices = it.devices.map {
                    device -> device.toBluetoothDevice(it.displayNames)
            },
            selectedDevice = it.selectedDevice
                ?.toBluetoothDevice(it.displayNames),
            connectionStatus = it.connectionStatus,
            connectionError = it.connectionError,
            isAdvertising = it.isAdvertising,
            isScanning = it.isScanning,
            advertiseAvailable = it.advertiseAvailable,
            scanAvailable = it.scanAvailable,
            displayNames = it.displayNames
        )
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BluetoothState()
    )

    init {
        val filter = IntentFilter().apply {
            addAction(android.bluetooth.BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        }

        context.registerReceiver(discoveryReceiver, filter)

        _state.value = _state.value.copy(
            advertiseAvailable = (bluetoothLeAdvertiser != null),
            scanAvailable = (bluetoothAdapter != null),
            displayNames = (
                Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
                context.checkSelfPermission(
                    Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED
            )
        )
    }

    @SuppressLint("MissingPermission")
    override fun dispose() {
        if (state.value.isScanning) {
            bluetoothAdapter?.cancelDiscovery()
            context.unregisterReceiver(discoveryReceiver)
        }

        if (state.value.isAdvertising) {
            bluetoothLeAdvertiser?.stopAdvertising(advertiseCallback)
            setAdvertising(false)
        }

        connectionThread?.interrupt()
        bluetoothSocket?.close()
    }

    @SuppressLint("MissingPermission")
    override suspend fun startAdvertising() {
        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            .setConnectable(true)
            .build()

        val data = AdvertiseData.Builder()
            .setIncludeDeviceName(false)
            .build()

        bluetoothLeAdvertiser?.startAdvertising(
            settings,
            data,
            advertiseCallback
        )
    }

    @SuppressLint("MissingPermission")
    override suspend fun stopAdvertising() {
        bluetoothLeAdvertiser?.stopAdvertising(advertiseCallback)
        setAdvertising(false)
    }

    @SuppressLint("MissingPermission")
    override suspend fun startScanning() {
        _state.value = _state.value.copy(devices = emptyList())
        setScanning(bluetoothAdapter?.startDiscovery() == true)
    }

    @SuppressLint("MissingPermission")
    override suspend fun stopScanning() {
        bluetoothAdapter?.cancelDiscovery()
        setScanning(false)
    }

    override suspend fun setSelectedDevice(device: BluetoothDevice) {
        val actualDevice = _state.value.devices
            .find { it.address == device.address }

        _state.value = _state.value.copy(selectedDevice = actualDevice)
    }

    @SuppressLint("MissingPermission")
    override suspend fun connectToDevice(device: BluetoothDevice) {
        connectionThread?.interrupt()
        bluetoothSocket?.close()
        bluetoothAdapter?.cancelDiscovery()

        val actualDevice = _state.value.devices
            .find { it.address == device.address }
            ?: return

        val thread = Thread {
            try {
                val socket = actualDevice.createRfcommSocketToServiceRecord(SerialPortServiceClassUuid)
                socket.connect()
                mainHandler.post {
                    bluetoothSocket = socket
                    _state.value = _state.value.copy(
                        connectionStatus = BluetoothConnectionStatus.Connected,
                        connectionError = null
                    )
                }
            } catch (exception: IOException) {
                mainHandler.post {
                    _state.value = _state.value.copy(
                        connectionStatus = BluetoothConnectionStatus.Failed,
                        connectionError = exception.message
                    )
                }
            }
        }.also { it.start() }

        connectionThread = thread

        _state.value = _state.value.copy(
            connectionStatus = BluetoothConnectionStatus.Connecting,
            connectionError = null
        )
    }

    override suspend fun disconnectFromDevice() {
        connectionThread?.interrupt()
        connectionThread = null
        bluetoothSocket?.close()
        bluetoothSocket = null

        _state.value = _state.value.copy(
            connectionStatus = BluetoothConnectionStatus.Disconnected,
            connectionError = null
        )
    }

    override suspend fun displayNames() {
        _state.value = _state.value.copy(displayNames = true)
    }

    private fun setAdvertising(advertising: Boolean) {
        _state.value = _state.value.copy(isAdvertising = advertising)
    }

    private fun setScanning(scanning: Boolean) {
        _state.value = _state.value.copy(isScanning = scanning)
    }

    companion object {
        private val SerialPortServiceClassUuid: UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }
}