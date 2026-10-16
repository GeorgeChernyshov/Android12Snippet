package com.example.post31.ui.screen.newfeatures.ux

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
fun UserExperienceScreen() {
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
                    Text(stringResource(R.string.material_hint))
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
            }
        }
    )
}