package com.example.pre31.ui.screen

import android.content.Intent
import android.net.Uri
import android.view.WindowManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
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
fun WebIntentScreen() {
    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.WebIntent.resourceId)) },
        content = {
            Column() {
                WebIntentBlock(Modifier.padding(top = 16.dp))
                DisplaySizeBlock(Modifier.padding(top = 16.dp))
            }
        }
    )
}

@Composable
fun WebIntentBlock(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(modifier) {
        Button(
            onClick = {
                context.startActivity(
                    createWebIntent("https://www.google.com/search?q=random+number+generator")
                )
            }
        ) {
            Text(text = stringResource(id = R.string.web_open_google))
        }

        Button(
            modifier = Modifier.padding(top = 16.dp),
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
fun WebIntentBlockPreview(modifier: Modifier = Modifier) {
    WebIntentBlock()
}

@Preview(showBackground = true)
@Composable
fun DisplaySizeBlockPreview() {
    DisplaySizeBlock()
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