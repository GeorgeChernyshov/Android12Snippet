package com.example.post31.interactor

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionInteractor(private val context: Context) {
    fun invoke(
        permissions: List<String>,
        onGranted: () -> Unit,
        onDenied: () -> Unit
    ) {
        val anyPermissionGranted = permissions
            .map { permission ->
                ContextCompat.checkSelfPermission(
                    context,
                    permission
                )
            }
            .any { it == PackageManager.PERMISSION_GRANTED }

        if (anyPermissionGranted)
            onGranted()
        else onDenied()
    }
}