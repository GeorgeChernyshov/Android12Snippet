package com.example.pre31.ui.screen

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.pre31.R
import com.example.pre31.ui.components.AppBar
import com.example.pre31.ui.navigation.Screen

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
                LocationPermissionBlock()
            }
        }
    )
}

@Composable
fun LocationPermissionBlock() {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result -> }

    Column(Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.security_request_location_hint))
        Button(onClick = {
            permissionLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ))
        }) {
            Text(stringResource(R.string.security_request_location))
        }
    }
}