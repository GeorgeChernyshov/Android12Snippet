package com.example.post31.ui.screen.newfeatures

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
import com.example.post31.R
import com.example.post31.ui.components.AppBar
import com.example.post31.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewFeaturesScreen(onButtonClick: (Screen) -> Unit) {
    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.NewFeatures.resourceId)) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = { onButtonClick(Screen.Material) }) {
                    Text(stringResource(id = R.string.label_user_interface))
                }

                Button(onClick = { onButtonClick(Screen.UserExperience) }) {
                    Text(stringResource(id = Screen.UserExperience.resourceId))
                }
            }
        }
    )
}