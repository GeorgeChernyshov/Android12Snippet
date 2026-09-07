package com.example.post31.ui.screen.ux

import android.os.Build
import android.view.WindowManager
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.post31.R

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

@Preview(showBackground = true)
@Composable
fun DisplaySizeBlockPreview() {
    DisplaySizeBlock()
}