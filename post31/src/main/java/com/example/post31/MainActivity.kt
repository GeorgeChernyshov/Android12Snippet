package com.example.post31

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.post31.ui.AppViewModel
import com.example.post31.ui.navigation.Screen
import com.example.post31.ui.screen.UserExperienceScreen
import com.example.post31.ui.theme.Android12SnippetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}

@Composable
fun App(
    viewModel: AppViewModel = viewModel()
) {
    Android12SnippetTheme {
        val context = LocalContext.current
        when (viewModel.currentScreen.value) {
            is Screen.UserExperience -> UserExperienceScreen()
        }
    }
}