package com.example.post31.domain.bluetooth

data class BluetoothState(
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