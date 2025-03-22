package com.example.post31.ui.screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.Display
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.post31.R
import com.example.post31.helper.NotificationHelper
import com.example.post31.ui.components.AppBar
import com.example.post31.ui.navigation.Screen

@Composable
fun UserExperienceScreen(onNextClick: () -> Unit) {
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
                    Button(onClick = onNextClick) {
                        Text(stringResource(R.string.button_next))
                    }
                }
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
                R.string.ux_overscroll_stretch
            else R.string.ux_overscroll_glow

            Text(stringResource(id = stringId))
        }
    }
}

@Composable
fun WebIntentBlock(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.web_title),
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.h6
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Column(Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.web_request_association))
                Button(onClick = {
                    val intent = Intent(
                        Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS,
                        Uri.parse("package:${context.packageName}")
                    )

                    context.startActivity(intent)
                }) {
                    Text(stringResource(R.string.web_request_association_button))
                }
            }
        }

        Column {
            Text(stringResource(R.string.web_open_google_hint))
            Button(
                onClick = {
                    context.startActivity(
                        createWebIntent("https://www.google.com/search?q=random+number+generator")
                    )
                }
            ) {
                Text(text = stringResource(id = R.string.web_open_google))
            }
        }

        Column {
            Text(text = stringResource(id =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    R.string.web_open_twitch_hint_post31
                else R.string.web_open_twitch_hint_pre31
            ))

            Button(
                onClick = {
                    context.startActivity(
                        createWebIntent("https://www.twitch.tv/piemka")
                    )
                }
            ) {
                Text(text = stringResource(id = R.string.web_open_twitch))
            }
        }
    }
}

@Composable
fun ImmersiveModeBlock(modifier: Modifier = Modifier) {
    val window = (LocalContext.current as Activity).window

    Column(modifier) {
        Text(text = stringResource(id = R.string.ux_immersive_mode_title))
        Text(text = stringResource(id =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                R.string.ux_immersive_mode_hint_post31
            else R.string.ux_immersive_mode_hint_pre31
        ))

        Button(
            onClick = {
                enterImmersiveMode(
                    window,
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
                )
            }
        ) {
            Text(text = stringResource(id = R.string.ux_immersive_mode_show_bars_by_touch))
        }

        Button(
            onClick = {
                enterImmersiveMode(
                    window,
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
                )
            }
        ) {
            Text(text = stringResource(id = R.string.ux_immersive_mode_show_bars_by_swipe))
        }

        Button(
            onClick = {
                enterImmersiveMode(
                    window,
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                )
            }
        ) {
            Text(text = stringResource(id = R.string.ux_immersive_mode_show_transient_bars))
        }

        Button(onClick = { exitImmersiveMode(window) }) {
            Text(text = stringResource(id = R.string.ux_immersive_mode_exit))
        }
    }
}

@Composable
fun DisplaySizeBlock(modifier: Modifier = Modifier) {
    val windowManager = LocalContext.current.getSystemService(WindowManager::class.java)
    
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        Column(modifier) {
            Text(text = stringResource(id = R.string.ux_display_metrics_title))

            Text(text = "Width = ${windowManager.currentWindowMetrics.bounds.width()}")
            
            Text(text = "Height = ${windowManager.currentWindowMetrics.bounds.height()}")
        }   
    }
}

@Composable
fun CustomNotificationBlock() {
    val context = LocalContext.current
    val notificationHelper = NotificationHelper(context)

    Column {
        Text(stringResource(R.string.ux_custom_notification_hint))

        Button(onClick = { notificationHelper.showCustomNotification() }) {
            Text(stringResource(R.string.ux_custom_notification_button))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScrollDemoBlockPreview() {
    ScrollDemoBlock()
}

@Preview(showBackground = true)
@Composable
fun WebIntentBlockPreview(modifier: Modifier = Modifier) {
    WebIntentBlock()
}

@Preview(showBackground = true)
@Composable
fun ImmersiveModeBlockPreview() {
    ImmersiveModeBlock()
}

@Preview(showBackground = true)
@Composable
fun DisplaySizeBlockPreview() {
    DisplaySizeBlock()
}

@Preview(showBackground = true)
@Composable
fun CustomNotificationBlockPreview() {
    CustomNotificationBlock()
}

fun createWebIntent(
    uriString: String
) = Intent.createChooser(
    Intent(
        Intent.ACTION_VIEW,
        Uri.parse(uriString)
    ),
    "Open in"
)

fun enterImmersiveMode(window: Window, behavior: Int) {
    val windowInsetsController =
        WindowCompat.getInsetsController(window, window.decorView)
    // Configure the behavior of the hidden system bars.
    windowInsetsController.systemBarsBehavior = behavior
    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
}

fun exitImmersiveMode(window: Window) {
    WindowCompat.getInsetsController(window, window.decorView)
        .show(WindowInsetsCompat.Type.systemBars())
}