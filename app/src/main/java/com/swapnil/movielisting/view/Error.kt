package com.swapnil.movielisting.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ErrorView(modifier: Modifier = Modifier, text: String) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Text(
            text = text
        )
    }
}

@Preview
@Composable
private fun ErrorViewPreview () {
    ErrorView(modifier = Modifier.fillMaxSize(), text = "Error occurred")
}