package com.example.post31.ui.screen

import android.Manifest
import android.os.Build
import android.os.PowerManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.post31.R
import com.example.post31.interactor.PermissionInteractor
import com.example.post31.service.GeolocationService
import com.example.post31.service.GeolocationService.GeolocationServiceState.Status
import com.example.post31.ui.components.AppBar
import com.example.post31.ui.navigation.Screen
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PerformanceScreen(
    locationServiceState: StateFlow<GeolocationService.GeolocationServiceState>?
) {
    val context = LocalContext.current
        .applicationContext

    val state = locationServiceState?.collectAsState()

    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.Performance.resourceId)) },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { 
                    PowerSaverModeBlock()
                }
                
                item {
                    Text(stringResource(R.string.performance_location))
                }

                item {
                    ToggleLocationUpdatesButton(
                        isServiceRunning = state?.value?.status == Status.UPDATING,
                        toggleService = {
                            if (state?.value?.status != Status.UPDATING)
                                GeolocationService.startService(context)
                            else GeolocationService.stopService(context)
                        }
                    )
                }

                item {
                    LocationTableRow(
                        location = stringResource(id = R.string.performance_location_column),
                        timestamp = stringResource(id = R.string.performance_time_column)
                    )
                }

                state?.value?.let {
                    items(it.locations) { location ->
                        LocationTableRow(
                            location = location.locationString,
                            timestamp = location.timeString
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun ToggleLocationUpdatesButton(
    isServiceRunning: Boolean,
    toggleService: () -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        toggleService()
    }

    Button(onClick = {
        PermissionInteractor(context).invoke(
            permissions = listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            onGranted = toggleService,
            onDenied = {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }
        )
    }) {
        Text(stringResource(
            if (isServiceRunning)
                R.string.performance_location_updates_stop
            else R.string.performance_location_updates_start
        ))
    }
}

@Composable
fun PowerSaverModeBlock() {
    val context = LocalContext.current
    val powerManager = context.getSystemService(PowerManager::class.java)

    Text(stringResource(R.string.performance_power_saver_hint))
    Text(stringResource(
        id = when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.P ->
                R.string.performance_power_saver_undefined

            powerManager.locationPowerSaveMode == PowerManager.LOCATION_MODE_FOREGROUND_ONLY ->
                R.string.performance_power_saver_no

            else -> R.string.performance_power_saver_yes
        }
    ))
}