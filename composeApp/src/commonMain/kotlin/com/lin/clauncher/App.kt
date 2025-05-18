package com.lin.clauncher

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import clauncher.composeapp.generated.resources.Res
import clauncher.composeapp.generated.resources.wall_paper
import com.lin.clauncher.view.DesktopView
import com.lin.clauncher.view.InitView
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    var viewModel = HomeViewModel()
    println("init app")
    MaterialTheme {
        println("init theme")
        val coroutineScope = rememberCoroutineScope()

        val count by viewModel.count.collectAsState()
        var showContent by remember { mutableStateOf(false) }
        var versionInt = remember {
            mutableStateOf(0)
        }
        println("load app ${count}")

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            BoxWithConstraints {
                 var screenWidth = maxWidth
                 var screenHeight = maxHeight
                LaunchedEffect(Unit) {
                    viewModel.loadApp(screenWidth.value.toInt(),screenHeight.value.toInt())
                }

                Image(
                    modifier = Modifier.width(screenWidth)
                            .height(screenHeight)
                            .fillMaxSize(),
                    painter = painterResource(Res.drawable.wall_paper),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                viewModel.mAppInfoBaseBean?.let {
                    DesktopView(it, HomeViewModel(), versionInt, screenWidth, screenHeight)
                }
                if (viewModel.mAppInfoBaseBean == null) {
                    InitView(screenWidth, screenHeight)
                }
            }
        }



    }
}