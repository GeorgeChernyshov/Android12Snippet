package com.example.post31.data

import com.example.post31.data.bluetooth.BluetoothRepositoryImpl
import com.example.post31.domain.bluetooth.BluetoothRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBluetoothRepository(
        bluetoothRepositoryImpl: BluetoothRepositoryImpl
    ): BluetoothRepository
}