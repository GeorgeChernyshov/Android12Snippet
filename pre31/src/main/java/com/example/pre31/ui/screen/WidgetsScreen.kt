package com.example.pre31.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
fun WidgetsScreen() {
    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.Widgets.resourceId)) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(stringResource(R.string.widget_screen_text))
                Text(stringResource(R.string.widget_screen_text2))
            }
        }
    )
}