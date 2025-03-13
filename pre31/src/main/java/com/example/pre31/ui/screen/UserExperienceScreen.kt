package com.example.pre31.ui.screen

import android.view.WindowManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pre31.R
import com.example.pre31.ui.components.AppBar
import com.example.pre31.ui.navigation.Screen

@Composable
fun UserExperienceScreen() {
    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.UserExperience.resourceId)) },
        content = {
            Column() {
                Text(stringResource(R.string.splash_hint))
                DisplaySizeBlock(Modifier.padding(top = 16.dp))
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

@Preview(showBackground = true)
@Composable
fun DisplaySizeBlockPreview() {
    DisplaySizeBlock()
}