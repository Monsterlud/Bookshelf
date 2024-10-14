package com.monsalud.bestsellerbookshelf.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BestSellersListScreen(onClick: (Int) -> Unit) {

    Box(
        modifier = Modifier.fillMaxSize().clickable { onClick(6789) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Best Sellers List Screen",
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}

@Composable
@Preview
fun BestSellersListScreenPreview() {
    BestSellersListScreen(onClick = {})
}