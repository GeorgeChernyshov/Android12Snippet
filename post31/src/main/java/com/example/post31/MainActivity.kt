package com.example.post31

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.post31.service.GeolocationService
import com.example.post31.ui.AppViewModel
import com.example.post31.ui.navigation.Screen
import com.example.post31.ui.screen.PerformanceScreen
import com.example.post31.ui.screen.UserExperienceScreen
import com.example.post31.ui.screen.WidgetsScreen
import com.example.post31.ui.theme.Android12SnippetTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var binder: GeolocationService.GeolocationServiceBinder? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            lifecycleScope.launch {
                binder = p1 as GeolocationService.GeolocationServiceBinder?
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            lifecycleScope.launch {
                binder = null
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        bindService(
            Intent(this.applicationContext, GeolocationService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )

        setContent { App(binder = { binder }) }
    }

    override fun onDestroy() {
        this.unbindService(serviceConnection)
        super.onDestroy()
    }
}

@Composable
fun App(
    viewModel: AppViewModel = viewModel(),
    binder: () -> GeolocationService.GeolocationServiceBinder?
) {
    Android12SnippetTheme {
        val context = LocalContext.current
        when (viewModel.currentScreen.value) {
            Screen.UserExperience -> UserExperienceScreen(
                onNextClick = {
                    viewModel.setCurrentScreen(Screen.Performance)
                }
            )

            Screen.Performance -> PerformanceScreen(binder()?.state)
            Screen.Widgets -> WidgetsScreen()
        }
    }
}