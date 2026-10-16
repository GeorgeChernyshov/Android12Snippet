package com.example.post31.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.post31.ui.LocalBackHandler
import com.example.post31.ui.theme.Android12SnippetTheme

@Composable
fun AppBar(name: String, modifier: Modifier = Modifier) {
    val backHandler = LocalBackHandler.current

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        IconButton(onClick = backHandler) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Text(
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineMedium,
            text = name,
            modifier = modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppBarPreview() {
    Android12SnippetTheme {
        AppBar("Title")
    }
}