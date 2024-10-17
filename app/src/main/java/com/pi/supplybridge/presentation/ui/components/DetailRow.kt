package com.pi.supplybridge.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val annotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Black, fontSize = MaterialTheme.typography.bodyMedium.fontSize)) {
                append("$label ")
            }
            withStyle(style = SpanStyle(color = Color.DarkGray, fontSize = MaterialTheme.typography.bodyLarge.fontSize)) {
                append(value)
            }
        }

        Text(
            text = annotatedString,
            modifier = Modifier.padding(bottom = 14.dp)
        )
    }
}
