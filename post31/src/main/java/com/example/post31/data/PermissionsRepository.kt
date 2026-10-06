package com.example.post31.data

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var resultLauncher: ActivityResultLauncher<Array<String>>? = null

    private val scope = CoroutineScope(Dispatchers.Default)
    private val _result = Channel<Boolean>(Channel.CONFLATED)

    suspend fun request(permission: String) = request(listOf(permission))

    suspend fun request(permissions: List<String>): Boolean {
        val missingPermissions = permissions.filter {
            context.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
        }

        if (resultLauncher == null) return false

        if (missingPermissions.isNotEmpty()) {
            resultLauncher?.launch(missingPermissions.toTypedArray())
            return _result.receive()
        } else return true
    }

    fun attach(caller: ActivityResultCaller) {
        resultLauncher = caller.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            scope.launch {
                _result.send(!it.containsValue(false))
            }
        }
    }

    fun detach() {
        resultLauncher = null
    }
}