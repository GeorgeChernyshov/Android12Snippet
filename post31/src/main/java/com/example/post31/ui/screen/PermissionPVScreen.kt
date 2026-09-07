package com.example.post31.ui.screen

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.post31.R
import com.example.post31.ui.components.AppBar
import com.example.post31.ui.navigation.Screen
import com.example.post31.ui.theme.Android12SnippetTheme

@Composable
fun PermissionPVScreen(onNextClick: () -> Unit) {
    val pm = LocalContext.current.packageManager
    var found by remember { mutableStateOf<Boolean?>(null) }

    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.PermissionPV.resourceId)) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(stringResource(R.string.permissions_pv_hint_1))
                Text(stringResource(R.string.permissions_pv_hint_2))
                Text(stringResource(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                        R.string.permissions_pv_should_not_find
                    else R.string.permissions_pv_should_find
                ))

                Button(onClick = {
                    found = pm.getAllPermissionGroups(0)
                        .flatMap { pm.queryPermissionsByGroup(it.name, 0) }
                        .any { it.name == "com.example.pre31.PermissionToEatDonuts" }
                }) {
                    Text(stringResource(R.string.permissions_pv_button))
                }

                when (found) {
                    true -> Text(stringResource(R.string.permissions_pv_permission_found))
                    false -> {
                        Text(stringResource(R.string.permissions_pv_permission_not_found))
                        Text(stringResource(R.string.permissions_pv_permission_not_found2))
                    }

                    null -> {}
                }

                Button(onClick = onNextClick) {
                    Text(stringResource(R.string.button_next))
                }
            }
        }
    )
}

@Composable
@Preview
fun PermissionPVScreenPreview() {
    Android12SnippetTheme {
        PermissionPVScreen(onNextClick = {})
    }
}