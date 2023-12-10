package com.example.post31

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.example.post31.ui.theme.Android12SnippetTheme

class WebViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Android12SnippetTheme {
                WebViewBlock(
                    uri = intent.data,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun WebViewBlock(
    uri: Uri?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(modifier) {
        Text(
            color = MaterialTheme.colors.onSurface,
            text = stringResource(id = R.string.web_hint_twitch)
        )

        AndroidView(
            modifier = Modifier.weight(1f),
            factory = {
                WebView(context).apply {
                    if (uri != null) {
                        loadUrl(uri.toString())
                    }
                }
            }
        )
    }
}