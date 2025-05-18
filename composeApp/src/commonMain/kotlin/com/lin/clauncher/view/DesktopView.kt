package com.lin.clauncher.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.lin.clauncher.HomeViewModel
import com.lin.comlauncher.entity.AppInfoBaseBean
import com.lin.comlauncher.entity.AppManagerBean
import com.lin.comlauncher.entity.ApplicationInfo

/**
 *@Author:        donghaolin
 *@CreateDate:    2025/5/14
 */
@Composable
fun DesktopView(lists: AppInfoBaseBean, viewModel: HomeViewModel, version: MutableState<Int>, width: Dp, height: Dp) {

    val state = rememberLazyListState()
    var foldOpenState = remember { mutableStateOf<MutableList<ApplicationInfo>>(mutableListOf()) }
//    var scrollWidth = remember { mutableStateOf(0) }

    val coroutineScope = rememberCoroutineScope()
    val coroutineAnimScope = rememberCoroutineScope()

    var dragInfoState = remember { mutableStateOf<ApplicationInfo?>(null) }
    var dragUpState = remember {
        mutableStateOf(false)
    }

    var offsetX = remember { mutableStateOf(0.dp) }
    var offsetY = remember { mutableStateOf(0.dp) }
    var currentSelect = remember { mutableStateOf(0) }
    var animFinish = remember { mutableStateOf(false) }
    var appManagerState = remember { mutableStateOf<AppManagerBean?>(null) }

    var homeList = lists.homeList
    var toolBarList = lists.toobarList

    //draw dot
    var dotWidth = 8
    var indicationDot = homeList.size * dotWidth + (homeList.size - 1) * 6
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
                .width(width = indicationDot.dp)
                .height(height = height)
                .offset(
                    (width - indicationDot.dp) / 2, (height.value - 100).dp
                )
    ) {
        homeList.forEachIndexed { index, arrayList ->
            Box(
                modifier = Modifier
                        .size(dotWidth.dp)
                        .clip(CircleShape)
                        .background(Color(if (currentSelect.value == index) 0x22000000 else 0x66000000))
            )
        }
    }
    // draw toolbar
    lists.toobarList.let { applist ->
        MyBasicColumn(
            modifier = Modifier
                    .zIndex(zIndex = 0f)
        )
        {
            applist.forEachIndexed { index, it ->
                IconView(
                    it = it,
                    offsetX = offsetX,
                    offsetY = offsetY,
                    dragUpState = dragUpState,
                    foldOpen = foldOpenState
                )
            }
        }
//
    }


    var pos = offsetX.value
    LazyRow(
        modifier = Modifier
                .offset(0.dp, 0.dp)
                .width(width = width)
                .height(height = height)
//                .pointerInteropFilter {
//                    if (it.action == MotionEvent.ACTION_DOWN) {
//                        appManagerState.value = null
//                    }
//                    false
//                }
                .pointerInput(0) {
                    println("long press")
                    detectLongPress(
                        toolList = toolBarList,
                        homeList = homeList,
                        currentSel = currentSelect,
                        coroutineScope = coroutineScope,
                        coroutineAnimScope = coroutineAnimScope,
                        dragInfoState = dragInfoState,
                        animFinish = animFinish,
                        offsetX = offsetX,
                        offsetY = offsetY,
                        dragUpState = dragUpState,
                        state = state,
                        foldOpen = foldOpenState,
                        appManagerState = appManagerState
                    )
                },
        state = state,
        flingBehavior = pagerLazyFlingBehavior(
            state,
            lists.homeList.size
        )
    ) {
        currentSelect.value = state.firstVisibleItemIndex
        lists.homeList.let { homeList ->
            if (homeList.size == 0)
                return@let

            lists.homeList.forEachIndexed { index, applist ->
                item {
                    Column(
                        modifier = Modifier
                                .width(width = width)
                                .height(height = height)
                                .offset(0.dp, 0.dp)

                    ) {
                        MyBasicColumn {
                            applist.forEach {
                                IconView(
                                    it = it,
                                    offsetX = offsetX,
                                    offsetY = offsetY,
                                    dragUpState = dragUpState,
                                    foldOpen = foldOpenState
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    //draw fold
    if (foldOpenState.value.size > 0) {
        Box(
            modifier = Modifier
                    .size(width, height)
                    .clickable {
                        foldOpenState.value = mutableListOf()
                    })
        {
            Box(
                modifier = Modifier
                        .size(width - 20.dp, 320.dp)
                        .offset(10.dp, (height - 320.dp) / 2)
                        .clip(RoundedCornerShape(8.dp))
                        .pointerInput(0) {
                            detectLongPress(
                                toolList = toolBarList,
                                homeList = homeList,
                                currentSel = currentSelect,
                                coroutineScope = coroutineScope,
                                coroutineAnimScope = coroutineAnimScope,
                                dragInfoState = dragInfoState,
                                animFinish = animFinish,
                                offsetX = offsetX,
                                offsetY = offsetY,
                                dragUpState = dragUpState,
                                state = state,
                                foldOpen = foldOpenState,
                                appManagerState = appManagerState
                            )
                        }
                        .background(Color(0.3f, 0.3f, 0.3f, 0.8f))
            ) {
                foldOpenState.value.forEach {
                    IconView(
                        it = it,
                        offsetX = offsetX,
                        offsetY = offsetY,
                        dragUpState = dragUpState,
                        foldOpen = foldOpenState
                    )
                }
            }
        }
    }

    //app more info
    appManagerState.value?.let {
        MoreInfoView(
            homeList = homeList,
            currentSel = currentSelect,
            appManagerState = appManagerState,
            coroutineScope = coroutineScope,
            coroutineAnimScope = coroutineAnimScope,
            offsetX = offsetX,
            offsetY = offsetY
        )
    }

    //current drag app
    if (dragUpState.value) {
        dragInfoState.value?.let {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                        .size(it.width.dp, it.height.dp)
                        .offset(it.posX.dp, it.posY.dp)
            ) {
//                LogUtils.e("dragUp = ${dragUpState.value}")
                IconViewDetail(it = it)
            }
        }
    }
}