package com.example.post31.ui.screen

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.IBinder
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.post31.MainActivity
import com.example.post31.R
import com.example.post31.service.SimpleForegroundService
import com.example.post31.ui.components.AppBar
import com.example.post31.ui.navigation.Screen
import com.example.post31.ui.theme.Android12SnippetTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ForegroundServiceLaunchScreen() {
    val context = LocalContext.current as MainActivity
    val lifecycleOwner = LocalLifecycleOwner.current
    var binder by remember { mutableStateOf<SimpleForegroundService.SimpleServiceBinder?>(null) }
    val coroutineScope = rememberCoroutineScope()

    fun scheduleProperServiceStart() {
        SimpleForegroundService.scheduleStart(
            context = context,
            delayMillis = SERVICE_START_DELAY_MILLIS
        )

        context.moveTaskToBack(false)
    }

    val exactAlarmPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (SimpleForegroundService.canScheduleExactAlarms(context)) {
            scheduleProperServiceStart()
        }
    }

    val serviceConnection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                lifecycleOwner.lifecycleScope.launch {
                    binder = p1 as SimpleForegroundService.SimpleServiceBinder?
                }
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                lifecycleOwner.lifecycleScope.launch {
                    binder = null
                }
            }
        }
    }

    val serviceState = binder?.state?.collectAsState()

    DisposableEffect(Unit) {
        context.bindService(
            Intent(context, SimpleForegroundService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )

        onDispose {
            context.unbindService(serviceConnection)
        }
    }

    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.ForegroundServiceLaunch.resourceId)) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(stringResource(R.string.fg_launch_hint))

                Button(onClick = {
                    if (serviceState?.value?.status == SimpleForegroundService.SimpleServiceState.Status.STARTED)
                        SimpleForegroundService.stopService(context)
                    else {
                        coroutineScope.launch {
                            context.moveTaskToBack(false)
                            delay(SERVICE_START_DELAY_MILLIS)
                            SimpleForegroundService.startService(context)
                        }
                    }
                }) {
                    Text(stringResource(
                        if (serviceState?.value?.status == SimpleForegroundService.SimpleServiceState.Status.STARTED)
                            R.string.fg_launch_stop
                        else R.string.fg_launch_start
                    ))
                }

                Text(stringResource(R.string.fg_launch_hint_2))
                Text(stringResource(R.string.fg_launch_alarm_permission))

                Button(onClick = {
                    if (serviceState?.value?.status == SimpleForegroundService.SimpleServiceState.Status.STARTED)
                        SimpleForegroundService.stopService(context)
                    else if (SimpleForegroundService.canScheduleExactAlarms(context)) {
                        scheduleProperServiceStart()
                    } else {
                        exactAlarmPermissionLauncher.launch(
                            Intent(
                                Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                                Uri.parse("package:${context.packageName}")
                            )
                        )
                    }
                }) {
                    Text(stringResource(
                        if (serviceState?.value?.status == SimpleForegroundService.SimpleServiceState.Status.STARTED)
                            R.string.fg_launch_stop
                        else R.string.fg_launch_start_proper
                    ))
                }
            }
        }
    )
}

private const val SERVICE_START_DELAY_MILLIS = 10_000L

@Composable
@Preview
fun ForegroundServiceLaunchScreenPreview() {
    Android12SnippetTheme {
        ForegroundServiceLaunchScreen()
    }
}