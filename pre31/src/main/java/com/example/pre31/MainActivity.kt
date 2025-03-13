package com.example.pre31

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pre31.ui.AppViewModel
import com.example.pre31.ui.navigation.Screen
import com.example.pre31.ui.screen.UserExperienceScreen
import com.example.pre31.ui.theme.Android12SnippetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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