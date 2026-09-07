package com.example.post31.ui.screen.ux

import android.app.Activity
import android.os.Build
import android.view.Window
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.post31.R

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

@Preview(showBackground = true)
@Composable
fun ImmersiveModeBlockPreview() {
    ImmersiveModeBlock()
}

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