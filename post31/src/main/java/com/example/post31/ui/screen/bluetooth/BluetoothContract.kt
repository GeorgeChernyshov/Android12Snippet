package com.example.post31.ui.screen.bluetooth

import androidx.compose.runtime.Immutable
import com.example.post31.domain.bluetooth.BluetoothConnectionStatus
import com.example.post31.domain.bluetooth.BluetoothDevice

@Immutable
data class BluetoothScreenState(
    val devices: List<BluetoothDevice> = emptyList(),
    val selectedDevice: BluetoothDevice? = null,
    val connectionStatus: BluetoothConnectionStatus = BluetoothConnectionStatus.Disconnected,
    val connectionError: String? = null,
    val isScanning: Boolean = false,
    val isAdvertising: Boolean = false,
    val advertiseButtonEnabled: Boolean = false,
    val scanButtonEnabled: Boolean = false,
    val showDisplayNamesButton: Boolean = true
)