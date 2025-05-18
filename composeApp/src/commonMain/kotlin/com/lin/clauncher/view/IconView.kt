package com.lin.clauncher.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.lin.clauncher.util.LauncherConfig
import com.lin.clauncher.util.LauncherUtils
import com.lin.clauncher.util.LogUtils
import com.lin.comlauncher.entity.ApplicationInfo

@Composable
fun IconView(
    it: ApplicationInfo,
    offsetX: MutableState<Dp>, offsetY: MutableState<Dp>,
    dragUpState: MutableState<Boolean>,
    foldOpen: MutableState<MutableList<ApplicationInfo>>
) {

    var posX = it.posX
    var posY = it.posY
//    it.imageBitmap = rememberAsyncImagePainter(Image(bitmap = it.icon!!,"desc") )
//    if (it.isDrag) {
//        LogUtils.e("1111111111posx=${it.posX}  posy=${it.posY}")
//    }
    if(dragUpState.value){

    }
    var ox = offsetX.value
    var oy = offsetY.value
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
                .size(it.width.dp, it.height.dp)
                .offset(posX.dp, posY.dp)
                .alpha(if (it.isDrag) 0f else 1f)
                .background(Color.Transparent)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    if (it.isDrag) {
                        return@clickable
                    }
                    LogUtils.e("CLICK childs ${it.childs.size}")
                    if (it.appType == LauncherConfig.CELL_TYPE_FOLD) {
                        foldOpen.value = it.childs
                    } else {
                        LauncherUtils.startApp(it)
                    }
                }) {
        IconViewDetail(it, it.showText)
    }
}

@Composable
fun IconViewDetail(it: ApplicationInfo, showText: Boolean = true) {
    if (it.appType == LauncherConfig.CELL_TYPE_APP) {
        it.icon?.let { icon ->
            Image(
                bitmap = icon,
                contentDescription = it.pageName,
                modifier = Modifier
                        .size(it.iconWidth.dp, it.iconHeight.dp)
                        .clip(RoundedCornerShape(8.dp))
            )
        }
    } else if (it.appType == LauncherConfig.CELL_TYPE_FOLD) {

        Box(
            modifier = Modifier
                    .size(it.iconWidth.dp, it.iconHeight.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0.3f, 0.3f, 0.3f, 0.2f))
        ) {
            it.icon?.let { icon ->
                Image(
                    bitmap = icon,
                    contentDescription = it.pageName,
                    modifier = Modifier
                            .size(it.iconWidth.dp, it.iconHeight.dp)
                            .clip(RoundedCornerShape(4.dp))
                )
            }
        }

    }
    if (showText) {
        Text(
            text = it.name ?: "",
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontSize = 13.sp,
            modifier = Modifier.padding(4.dp, 10.dp, 4.dp, 0.dp)
        )
    }
}