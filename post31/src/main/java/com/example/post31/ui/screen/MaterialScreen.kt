package com.example.post31.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
fun MaterialScreen(
    dynamicColorEnabled: Boolean,
    toggleDynamicColor: () -> Unit,
    onNextClick: () -> Unit
) {
    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.Material.resourceId)) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(stringResource(R.string.material_hint))

                Text(
                    if (dynamicColorEnabled)
                        stringResource(R.string.material_dynamic_colors_on)
                    else stringResource(R.string.material_dynamic_colors_off)
                )

                Button(onClick = toggleDynamicColor) {
                    Text(stringResource(R.string.material_dynamic_colors_button))
                }

                Button(onClick = onNextClick) {
                    Text(stringResource(R.string.button_next))
                }
            }
        }
    )
}

@Composable
@Preview
fun MaterialScreenPreview() {
    Android12SnippetTheme {
        MaterialScreen(
            dynamicColorEnabled = true,
            toggleDynamicColor = {},
            onNextClick = {}
        )
    }
}