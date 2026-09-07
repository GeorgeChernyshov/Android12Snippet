package com.example.post31.ui.screen.ux

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.post31.R
import androidx.core.net.toUri

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

@Preview(showBackground = true)
@Composable
fun WebIntentBlockPreview() {
    WebIntentBlock()
}

fun createWebIntent(
    uriString: String
) = Intent.createChooser(
    Intent(
        Intent.ACTION_VIEW,
        uriString.toUri()
    ),
    "Open in"
)