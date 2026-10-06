package com.example.post31.ui.screen.bluetooth

import android.Manifest
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.post31.data.PermissionsRepository
import com.example.post31.domain.bluetooth.BluetoothDevice
import com.example.post31.domain.bluetooth.BluetoothRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BluetoothScreenViewModel @Inject constructor(
    private val bluetoothRepository: BluetoothRepository,
    private val permissionsRepository: PermissionsRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(BluetoothScreenState())
    val uiState: StateFlow<BluetoothScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            bluetoothRepository.state.collect {
                _uiState.value = BluetoothScreenState(
                    devices = it.devices,
                    selectedDevice = it.selectedDevice,
                    connectionStatus = it.connectionStatus,
                    connectionError = it.connectionError,
                    isAdvertising = it.isAdvertising,
                    isScanning = it.isScanning,
                    advertiseButtonEnabled = it.advertiseAvailable,
                    scanButtonEnabled = it.scanAvailable,
                    showDisplayNamesButton = !it.displayNames
                )
            }
        }
    }

    override fun onCleared() {
        bluetoothRepository.dispose()
        super.onCleared()
    }

    fun startScanning() = viewModelScope.launch {
        val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            listOf(Manifest.permission.BLUETOOTH_SCAN)
        else listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (permissionsRepository.request(requiredPermissions))
            bluetoothRepository.startScanning()
    }

    fun stopScanning() = viewModelScope.launch {
        bluetoothRepository.stopScanning()
    }

    fun startAdvertising() = viewModelScope.launch {
        if (
            Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
            permissionsRepository.request(Manifest.permission.BLUETOOTH_ADVERTISE)
        ) bluetoothRepository.startAdvertising()
    }

    fun stopAdvertising() = viewModelScope.launch {
        bluetoothRepository.stopAdvertising()
    }

    fun setSelectedDevice(device: BluetoothDevice) = viewModelScope.launch {
        bluetoothRepository.setSelectedDevice(device)
    }

    fun connectToDevice(device: BluetoothDevice) = viewModelScope.launch {
        if (
            Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
            permissionsRepository.request(Manifest.permission.BLUETOOTH_CONNECT)
        ) bluetoothRepository.connectToDevice(device)
    }

    fun disconnectFromDevice() = viewModelScope.launch {
        bluetoothRepository.disconnectFromDevice()
    }

    fun displayNames() = viewModelScope.launch {
        if (
            Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
            permissionsRepository.request(Manifest.permission.BLUETOOTH_CONNECT)
        ) bluetoothRepository.displayNames()
    }
}