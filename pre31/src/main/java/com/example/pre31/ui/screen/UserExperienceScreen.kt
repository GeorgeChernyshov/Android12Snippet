package com.example.pre31.ui.screen

import android.view.WindowManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pre31.R
import com.example.pre31.helper.NotificationHelper
import com.example.pre31.ui.components.AppBar
import com.example.pre31.ui.navigation.Screen

@Composable
fun UserExperienceScreen() {
    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.UserExperience.resourceId)) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(stringResource(R.string.splash_hint))
                DisplaySizeBlock()
                CustomNotificationBlock()
            }
        }
    )
}

@Composable
fun DisplaySizeBlock(modifier: Modifier = Modifier) {
    val windowManager = LocalContext.current.getSystemService(WindowManager::class.java)

    Column(modifier) {
        Text(text = stringResource(id = R.string.ux_display_metrics_title))

        Text(text = "Width = ${windowManager.defaultDisplay.width}")

        Text(text = "Height = ${windowManager.defaultDisplay.height}")
    }
}

@Composable
fun CustomNotificationBlock() {
    val context = LocalContext.current
    val notificationHelper = NotificationHelper(context)

    Column {
        Text(stringResource(R.string.ux_custom_notification_hint))

        Button(onClick = { notificationHelper.showCustomNotification() }) {
            Text(stringResource(R.string.ux_custom_notification_button))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DisplaySizeBlockPreview() {
    DisplaySizeBlock()
}

@Preview(showBackground = true)
@Composable
fun CustomNotificationBlockPreview() {
    CustomNotificationBlock()
}