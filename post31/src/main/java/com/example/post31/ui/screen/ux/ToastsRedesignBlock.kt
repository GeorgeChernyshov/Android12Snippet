package com.example.post31.ui.screen.ux

import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.post31.R
import com.example.post31.ui.theme.Android12SnippetTheme

@Composable
fun ToastsRedesignBlock() {
    val context = LocalContext.current

    Column {
        Text(stringResource(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                R.string.ux_toasts_new
            else R.string.ux_toasts_old
        ))

        Text(stringResource(R.string.ux_toasts_hint))

        Button(onClick = {
            Toast.makeText(
                context,
                "This is a toast with a very long text. This text should be sufficient to cover several lines.",
                Toast.LENGTH_LONG
            ).show()
        }) {
            Text(stringResource(id = R.string.ux_toasts_button))
        }
    }
}

@Composable
@Preview
fun ToastsRedesignBlockPreview() {
    Android12SnippetTheme {
        ToastsRedesignBlock()
    }
}