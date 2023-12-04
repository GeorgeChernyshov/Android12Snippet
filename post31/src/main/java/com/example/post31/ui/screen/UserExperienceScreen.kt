package com.example.post31.ui.screen

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.post31.R
import com.example.post31.ui.components.AppBar
import com.example.post31.ui.navigation.Screen

@Composable
fun UserExperienceScreen() {
    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.UserExperience.resourceId)) },
        content = {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(16.dp)
            ) {
                ScrollDemoBlock()
            }
        }
    )
}

@Composable
fun ScrollDemoBlock() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .verticalScroll(rememberScrollState())
    ) {
        repeat(10) {
            val stringId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                R.string.user_experience_overscroll_stretch
            else R.string.user_experience_overscroll_glow

            Text(stringResource(id = stringId))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScrollDemoBlockPreview() {
    ScrollDemoBlock()
}