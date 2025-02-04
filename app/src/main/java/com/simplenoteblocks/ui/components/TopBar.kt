package com.simplenoteblocks.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    rightSide: @Composable () -> Unit,
    leftSide: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
        ) {
            rightSide()
        }
        leftSide()
    }
}