package com.example.post31.ui.screen.ux

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.post31.R

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

@Preview(showBackground = true)
@Composable
fun ScrollDemoBlockPreview() {
    ScrollDemoBlock()
}