package com.example.post31.ui.screen.ux

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.Window
import android.view.WindowManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.post31.R
import com.example.post31.helper.NotificationHelper
import com.example.post31.ui.components.AppBar
import com.example.post31.ui.navigation.Screen

@Composable
fun UserExperienceScreen(onNextClick: () -> Unit) {
    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.UserExperience.resourceId)) },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(stringResource(R.string.splash_hint))
                }

                item {
                    ScrollDemoBlock()
                }

                item {
                    WebIntentBlock()
                }

                item {
                    ImmersiveModeBlock()
                }

                item {
                    DisplaySizeBlock()
                }

                item {
                    CustomNotificationBlock()
                }

                item {
                    ToastsRedesignBlock()
                }

                item {
                    Button(onClick = onNextClick) {
                        Text(stringResource(R.string.button_next))
                    }
                }
            }
        }
    )
}