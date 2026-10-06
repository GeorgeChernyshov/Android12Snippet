package com.example.post31.ui.screen.bluetooth

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.post31.domain.bluetooth.BluetoothConnectionStatus
import com.example.post31.domain.bluetooth.BluetoothDevice
import com.example.post31.R
import com.example.post31.ui.components.AppBar
import com.example.post31.ui.navigation.Screen
import com.example.post31.ui.theme.Android12SnippetTheme

@Composable
fun BluetoothScreen() {
    val viewModel: BluetoothScreenViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    BluetoothScreenContent(
        state = uiState,
        advertiseButtonEnabled = uiState.advertiseButtonEnabled,
        scanButtonEnabled = uiState.scanButtonEnabled,
        onAdvertiseClick = {
            if (uiState.isAdvertising)
                viewModel.stopAdvertising()
            else viewModel.startAdvertising()
        },
        onScanClick = {
            if (uiState.isScanning)
                viewModel.stopScanning()
            else viewModel.startScanning()
        },
        connectToDevice = viewModel::connectToDevice,
        disconnectFromDevice = viewModel::disconnectFromDevice,
        setSelectedDevice = viewModel::setSelectedDevice,
        displayNames = viewModel::displayNames
    )
}

@Composable
fun BluetoothScreenContent(
    state: BluetoothScreenState,
    advertiseButtonEnabled: Boolean,
    scanButtonEnabled: Boolean,
    onAdvertiseClick: () -> Unit,
    onScanClick: () -> Unit,
    connectToDevice: (BluetoothDevice) -> Unit,
    disconnectFromDevice: () -> Unit,
    setSelectedDevice: (BluetoothDevice) -> Unit,
    displayNames: () -> Unit
) {
    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.Bluetooth.resourceId)) },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item  {
                    Text(stringResource(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                            R.string.bluetooth_permissions_hint
                        else R.string.bluetooth_permissions_hint_old
                    ))
                }

                item {
                    Button(
                        enabled = advertiseButtonEnabled,
                        onClick = onAdvertiseClick
                    ) {
                        Text(stringResource(
                            if (state.isAdvertising) R.string.bluetooth_advertise_stop
                            else R.string.bluetooth_advertise_start
                        ))
                    }
                }

                item {
                    ScanControls(
                        scanButtonEnabled = scanButtonEnabled,
                        isScanning = state.isScanning,
                        showDisplayNamesButton = state.showDisplayNamesButton,
                        onScanClick = onScanClick,
                        displayNames = displayNames
                    )
                }

                item {
                    SelectedDeviceBlock(
                        device = state.selectedDevice,
                        connectionStatus = state.connectionStatus,
                        connectionError = state.connectionError,
                        onConnectClick = connectToDevice,
                        onDisconnectClick = disconnectFromDevice
                    )
                }

                items(state.devices, key = { it.address }) { device ->
                    BluetoothDevice(
                        deviceName = device.name,
                        deviceAddress = device.address,
                        onDeviceClick = { address ->
                            state.devices.find { it.address == address }
                                ?.let {
                                    if (state.selectedDevice?.address != it.address) {
                                        disconnectFromDevice()
                                        setSelectedDevice(it)
                                    }
                                }
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun ScanControls(
    scanButtonEnabled: Boolean,
    isScanning: Boolean,
    showDisplayNamesButton: Boolean,
    onScanClick: () -> Unit,
    displayNames: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(stringResource(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                R.string.bluetooth_scan_hint
            else R.string.bluetooth_scan_hint_old
        ))

        Button(
            enabled = scanButtonEnabled,
            onClick = onScanClick
        ) {
            Text(stringResource(
                if (isScanning) R.string.bluetooth_scan_stop
                else R.string.bluetooth_scan_start
            ))
        }

        if (showDisplayNamesButton) {
            Button(onClick = displayNames) {
                Text("Display Names")
            }
        }
    }
}

@Composable
fun SelectedDeviceBlock(
    device: BluetoothDevice?,
    onConnectClick: (BluetoothDevice) -> Unit,
    modifier: Modifier = Modifier,
    connectionStatus: BluetoothConnectionStatus = BluetoothConnectionStatus.Disconnected,
    connectionError: String? = null,
    onDisconnectClick: () -> Unit = {}
) {
    Column(modifier) {
        device?.let {
            Text(stringResource(R.string.bluetooth_selected_device))
            BluetoothDevice(
                deviceName = it.name,
                deviceAddress = it.address,
                onDeviceClick = { }
            )

            Text(stringResource(
                R.string.bluetooth_connection_status,
                connectionStatus.toString()
            ))

            connectionError?.let { error ->
                Text(text = error)
            }

            Button(
                enabled = connectionStatus != BluetoothConnectionStatus.Connecting,
                onClick = {
                    if (connectionStatus == BluetoothConnectionStatus.Connected) {
                        onDisconnectClick()
                    } else {
                        onConnectClick(it)
                    }
                }
            ) {
                Text(stringResource(
                    if (connectionStatus == BluetoothConnectionStatus.Connected)
                        R.string.bluetooth_disconnect
                    else R.string.bluetooth_connect
                ))
            }
        }
    }
}

@Composable
fun BluetoothDevice(
    deviceName: String?,
    deviceAddress: String,
    onDeviceClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDeviceClick(deviceAddress) }
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = deviceName ?: "Unknown device")
        Text(text = deviceAddress)
    }
}

@Composable
@Preview
fun BluetoothDevicePreview() {
    Android12SnippetTheme {
        BluetoothDevice(
            deviceName = "Device Name",
            deviceAddress = "Device Address",
            onDeviceClick = {}
        )
    }
}

@Composable
@Preview
fun SelectedDeviceBlockPreview() {
    Android12SnippetTheme {
        SelectedDeviceBlock(
            device = null,
            onConnectClick = {}
        )
    }
}

@Composable
@Preview
fun ScanControlsPreview() {
    Android12SnippetTheme {
        ScanControls(
            scanButtonEnabled = true,
            isScanning = false,
            showDisplayNamesButton = false,
            onScanClick = {},
            displayNames = {}
        )
    }
}

@Composable
@Preview
fun BluetoothScreenContentPreview() {
    Android12SnippetTheme {
        BluetoothScreenContent(
            state = BluetoothScreenState(),
            advertiseButtonEnabled = true,
            scanButtonEnabled = true,
            onAdvertiseClick = {},
            onScanClick = {},
            connectToDevice = {},
            disconnectFromDevice = {},
            setSelectedDevice = {},
            displayNames = {}
        )
    }
}
