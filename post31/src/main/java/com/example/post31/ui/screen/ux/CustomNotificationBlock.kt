package com.example.post31.ui.screen.ux

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.post31.R
import com.example.post31.helper.NotificationHelper

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
fun CustomNotificationBlockPreview() {
    CustomNotificationBlock()
}