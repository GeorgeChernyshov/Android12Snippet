package com.example.post31.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.example.post31.domain.bluetooth.BluetoothConnectionStatus

data class BluetoothStateImpl(
    val devices: List<BluetoothDevice> = emptyList(),
    val selectedDevice: BluetoothDevice? = null,
    val connectionStatus: BluetoothConnectionStatus = BluetoothConnectionStatus.Disconnected,
    val connectionError: String? = null,
    val isAdvertising: Boolean = false,
    val isScanning: Boolean = false,
    val advertiseAvailable: Boolean = false,
    val scanAvailable: Boolean = false,
    val displayNames: Boolean = false
)

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDevice(
    displayNames: Boolean
) = com.example.post31.domain.bluetooth.BluetoothDevice(
    name = if (displayNames) name else "Unknown",
    address = address
)