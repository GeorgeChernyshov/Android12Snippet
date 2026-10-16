package com.example.post31

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.post31.data.PermissionsRepository
import com.example.post31.service.GeolocationService
import com.example.post31.interactor.permission.PermissionInteractor
import com.example.post31.ui.AppViewModel
import com.example.post31.ui.LocalBackHandler
import com.example.post31.ui.LocalPermissionInteractor
import com.example.post31.ui.navigation.Screen
import com.example.post31.ui.screen.MainScreen
import com.example.post31.ui.screen.changes.BackPressChangesScreen
import com.example.post31.ui.screen.changes.bluetooth.BluetoothScreen
import com.example.post31.ui.screen.changes.ForegroundServiceLaunchScreen
import com.example.post31.ui.screen.newfeatures.MaterialScreen
import com.example.post31.ui.screen.changes.PerformanceScreen
import com.example.post31.ui.screen.changes.PermissionPVScreen
import com.example.post31.ui.screen.changes.SecurityAndPrivacyScreen
import com.example.post31.ui.screen.newfeatures.NewFeaturesScreen
import com.example.post31.ui.screen.newfeatures.ux.UserExperienceScreen
import com.example.post31.ui.screen.newfeatures.WidgetsScreen
import com.example.post31.ui.theme.Android12SnippetTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var permissionInteractor: PermissionInteractor
    @Inject lateinit var permissionsRepository: PermissionsRepository

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
        if ((intent.flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0)
            Log.i(TAG, "Launched from history")

        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        bindService(
            Intent(this.applicationContext, GeolocationService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )

        permissionsRepository.attach(this)

        setContent {
            val viewModel: AppViewModel = viewModel()

            CompositionLocalProvider(
                LocalPermissionInteractor provides permissionInteractor,
                LocalBackHandler provides {
                    if (!viewModel.navigateBack())
                        finish()
                }
            ) {
                App(
                    viewModel = viewModel,
                    binder = { binder }
                )
            }
        }
    }

    override fun onDestroy() {
        this.unbindService(serviceConnection)
        Log.i(TAG, "onDestroy called")
        permissionsRepository.detach()
        super.onDestroy()
    }

    companion object {
        private const val TAG = "Android12Snippet-post31"
    }
}

@Composable
fun App(
    viewModel: AppViewModel,
    binder: () -> GeolocationService.GeolocationServiceBinder?
) {
    Android12SnippetTheme(
        dynamicColor = viewModel.dynamicColorEnabled.value
    ) {
        val context = LocalContext.current
        when (viewModel.currentScreen.value) {
            Screen.BackPressChanges -> BackPressChangesScreen(
                onNextClick = { viewModel.navigateTo(Screen.Main) }
            )

            Screen.BehaviorChanges -> {}

            Screen.Bluetooth -> BluetoothScreen(
                onNextClick = {
                    viewModel.setCurrentScreen(Screen.Widgets)
                }
            )

            Screen.ForegroundServiceLaunch -> ForegroundServiceLaunchScreen(
                onNextClick = {
                    viewModel.setCurrentScreen(Screen.Bluetooth)
                }
            )

            Screen.Main -> MainScreen(onButtonClick = viewModel::navigateTo)

            Screen.Material -> MaterialScreen(
                dynamicColorEnabled = viewModel.dynamicColorEnabled.value,
                toggleDynamicColor = {
                    viewModel.setDynamicColorEnabled(
                        !viewModel.dynamicColorEnabled.value
                    )
                },
                onNextClick = {
                    viewModel.navigateTo(Screen.Widgets)
                }
            )

            Screen.NewFeatures -> NewFeaturesScreen(
                onButtonClick = viewModel::navigateTo
            )

            Screen.UserExperience -> UserExperienceScreen()

            Screen.Performance -> PerformanceScreen(
                locationServiceState = binder()?.state,
                onNextClick = {
                    viewModel.setCurrentScreen(Screen.SecurityAndPrivacy)
                }
            )

            Screen.PermissionPV -> PermissionPVScreen(
                onNextClick = {
                    viewModel.setCurrentScreen(Screen.BackPressChanges)
                }
            )

            Screen.SecurityAndPrivacy -> SecurityAndPrivacyScreen(
                onNextClick = {
                    viewModel.setCurrentScreen(Screen.PermissionPV)
                }
            )

            Screen.Widgets -> WidgetsScreen()
        }
    }
}
