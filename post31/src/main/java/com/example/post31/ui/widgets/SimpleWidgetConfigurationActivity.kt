package com.example.post31.ui.widgets

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import com.example.post31.R
import com.example.post31.ui.theme.Android12SnippetTheme
import com.example.post31.ui.widgets.SimpleWidget.Companion.TITLE_PREFS_KEY

class SimpleWidgetConfigurationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        val resultValue = Intent().putExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            appWidgetId
        )

        setResult(RESULT_CANCELED, resultValue)

        val currentTitle = getSharedPreferences(WIDGETS_PREFS, MODE_PRIVATE)
            .getString(
                TITLE_PREFS_KEY + appWidgetId,
                getString(R.string.appwidget_text)
            )

        setContent {
            Android12SnippetTheme {
                Configuration(
                    save = { str ->
                        getSharedPreferences(WIDGETS_PREFS, MODE_PRIVATE).edit {
                            putString(TITLE_PREFS_KEY + appWidgetId, str)
                            apply()
                        }

                        updateAppWidget(this, AppWidgetManager.getInstance(this), appWidgetId)
                        setResult(RESULT_OK, resultValue)
                        finish()
                    },
                    initialValue = currentTitle.orEmpty()
                )
            }
        }
    }
}

@Composable
fun Configuration(
    save: (String) -> Unit,
    initialValue: String = "",
) {
    var str by remember { mutableStateOf(initialValue) }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextField(
                value = str,
                onValueChange = { str = it },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { save(str) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.widget_configuration_save))
            }
        }
    }
}

@Composable
@Preview
fun ConfigurationPreview() {
    Android12SnippetTheme {
        Configuration(save = {})
    }
}