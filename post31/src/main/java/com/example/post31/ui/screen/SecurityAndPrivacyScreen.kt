package com.example.post31.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.post31.ui.components.AppBar
import com.example.post31.ui.navigation.Screen

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

            }
        }
    )
}