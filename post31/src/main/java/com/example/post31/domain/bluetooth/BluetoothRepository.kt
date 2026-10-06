package com.example.post31.domain.bluetooth

import kotlinx.coroutines.flow.StateFlow

interface BluetoothRepository {

    val state: StateFlow<BluetoothState>

    fun dispose()
    suspend fun startAdvertising()
    suspend fun stopAdvertising()
    suspend fun startScanning()
    suspend fun stopScanning()
    suspend fun setSelectedDevice(device: BluetoothDevice)
    suspend fun connectToDevice(device: BluetoothDevice)
    suspend fun disconnectFromDevice()
    suspend fun displayNames()
}