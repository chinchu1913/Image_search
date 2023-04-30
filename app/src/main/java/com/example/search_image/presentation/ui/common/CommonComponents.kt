package com.example.search_image.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.search_image.R

@Composable
fun NotConnectedComponent() {
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colors.error)
            .padding(8.dp),
    ) {
        Text(
            text = stringResource(
                id = R.string.text_no_connectivity
            ),
            color = White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun ConnectedComponent() {
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colors.secondaryVariant)
            .padding(8.dp)
    ) {
        Text(
            text = stringResource(
                id = R.string.text_connectivity
            ),
            color = White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
