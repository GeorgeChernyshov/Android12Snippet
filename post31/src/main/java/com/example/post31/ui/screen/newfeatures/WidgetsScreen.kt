package com.example.post31.ui.screen.newfeatures

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.post31.R
import com.example.post31.ui.components.AppBar
import com.example.post31.ui.navigation.Screen
import com.example.post31.ui.theme.Android12SnippetTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WidgetsScreen() {
    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.Widgets.resourceId)) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(stringResource(R.string.widget_screen_text))
                Text(stringResource(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                        R.string.widget_screen_text2_new
                    else R.string.widget_screen_text2_old
                ))

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Text(stringResource(R.string.widget_screen_text3))
                }

                Text(stringResource(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                        R.string.widget_screen_text4_new
                    else R.string.widget_screen_text4_old
                ))

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Text(stringResource(R.string.widget_screen_text5))
                }
            }
        }
    )
}

@Composable
@Preview
fun WidgetsScreenPreview() {
    Android12SnippetTheme {
        WidgetsScreen()
    }
}