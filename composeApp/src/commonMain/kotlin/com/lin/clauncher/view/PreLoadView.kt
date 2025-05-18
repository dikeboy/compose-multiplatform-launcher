package com.lin.clauncher.view

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun InitView(width: Dp, height: Dp) {

    val state = rememberScrollState()

    Row(
        modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .horizontalScroll(
                    state,
                    flingBehavior = pagerFlingBehavior(
                        state, 0
                    )
                ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = "init...")
    }

}