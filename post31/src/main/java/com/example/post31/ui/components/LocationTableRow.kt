package com.example.post31.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.post31.ui.theme.Android12SnippetTheme

@Composable
fun LocationTableRow(
    location: String,
    timestamp: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = location,
            modifier = Modifier.weight(0.5f)
        )

        Text(
            text = timestamp,
            modifier = Modifier.weight(0.5f)
        )
    }
}

@Preview
@Composable
fun LocationTableRowPreview() {
    Android12SnippetTheme {
        LocationTableRow(
            location = "location",
            timestamp = "timestamp"
        )
    }
}