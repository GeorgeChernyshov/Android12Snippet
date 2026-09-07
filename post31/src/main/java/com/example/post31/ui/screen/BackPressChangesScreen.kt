package com.example.post31.ui.screen

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.post31.R
import com.example.post31.ui.components.AppBar
import com.example.post31.ui.navigation.Screen
import com.example.post31.ui.theme.Android12SnippetTheme

@Composable
fun BackPressChangesScreen() {
    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.BackPressChanges.resourceId)) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(stringResource(R.string.back_press_changes_hint1))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Text(stringResource(R.string.back_press_new1))
                    Text(stringResource(R.string.back_press_new2))
                    Text(stringResource(R.string.back_press_new3))
                } else {
                    Text(stringResource(R.string.back_press_old1))
                    Text(stringResource(R.string.back_press_old2))
                    Text(stringResource(R.string.back_press_old3))
                    Text(stringResource(R.string.back_press_old4))
                }
            }
        }
    )
}

@Composable
@Preview
fun BackPressChangesScreenPreview() {
    Android12SnippetTheme {
        BackPressChangesScreen()
    }
}