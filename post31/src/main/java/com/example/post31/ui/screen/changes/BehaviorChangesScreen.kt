package com.example.post31.ui.screen.changes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.post31.ui.components.AppBar
import com.example.post31.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BehaviorChangesScreen(onButtonClick: (Screen) -> Unit) {
    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.NewFeatures.resourceId)) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = { onButtonClick(Screen.Bluetooth) }) {
                    Text(stringResource(id = Screen.Bluetooth.resourceId))
                }

                Button(onClick = { onButtonClick(Screen.BackPressChanges) }) {
                    Text(stringResource(id = Screen.BackPressChanges.resourceId))
                }

                Button(onClick = { onButtonClick(Screen.ForegroundServiceLaunch) }) {
                    Text(stringResource(id = Screen.ForegroundServiceLaunch.resourceId))
                }

                Button(onClick = { onButtonClick(Screen.Performance) }) {
                    Text(stringResource(id = Screen.Performance.resourceId))
                }

                Button(onClick = { onButtonClick(Screen.PermissionPV) }) {
                    Text(stringResource(id = Screen.PermissionPV.resourceId))
                }

                Button(onClick = { onButtonClick(Screen.SecurityAndPrivacy) }) {
                    Text(stringResource(id = Screen.SecurityAndPrivacy.resourceId))
                }
            }
        }
    )
}