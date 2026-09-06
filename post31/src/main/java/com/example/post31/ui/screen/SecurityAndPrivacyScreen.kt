package com.example.post31.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.post31.R
import com.example.post31.helper.AudioRecorder
import com.example.post31.interactor.PermissionInteractor
import com.example.post31.ui.components.AppBar
import com.example.post31.ui.navigation.Screen
import com.example.post31.ui.theme.Android12SnippetTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun SecurityAndPrivacyScreen() {
    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.SecurityAndPrivacy.resourceId)) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MicrophoneAccessBlock()
                CloseSystemDialogsBlock()
            }
        }
    )
}

@SuppressLint("MissingPermission")
@Composable
fun MicrophoneAccessBlock() {
    val context = LocalContext.current
    val recorder = remember { AudioRecorder() }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted)
            recorder.start()
    }

    val isRecording by recorder.isRecording.collectAsState()

    Column {
        Text(stringResource(R.string.security_mic_and_camera_toggles_description))

        Button(onClick = {
            if (isRecording)
                recorder.stop()
            else {
                PermissionInteractor(context).invoke(
                    permissions = listOf(
                        Manifest.permission.RECORD_AUDIO
                    ),
                    onGranted = { recorder.start() },
                    onDenied = {
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                )
            }
        }) {
            Text(stringResource(
                if (!isRecording)
                    R.string.security_start_recording
                else R.string.security_stop_recording
            ))
        }

        if (isRecording) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Text(stringResource(R.string.security_mic_indicator))
                Text(stringResource(R.string.security_mic_toggle))
            } else {
                Text(stringResource(R.string.security_mic_no_indicator))
            }
        }
    }
}

@Composable
@SuppressLint("MissingPermission")
fun CloseSystemDialogsBlock() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column {
        Text(stringResource(R.string.security_close_system_dialogs_description))

        Button(onClick = {
            scope.launch {
                delay(3.seconds)
                val intent = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
                context.sendBroadcast(intent)
            }
        }) {
            Text(stringResource(R.string.security_close_system_dialogs_button))
        }

        Text(stringResource(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                R.string.security_close_system_dialogs_fail
            else R.string.security_close_system_dialogs_success
        ))
    }
}

@Composable
@Preview
fun CloseSystemDialogsBlockPreview() {
    Android12SnippetTheme {
        CloseSystemDialogsBlock()
    }
}

@Composable
@Preview
fun MicrophoneAccessBlockPreview() {
    Android12SnippetTheme {
        MicrophoneAccessBlock()
    }
}

@Composable
@Preview
fun SecurityAndPrivacyScreenPreview() {
    Android12SnippetTheme {
        SecurityAndPrivacyScreen()
    }
}