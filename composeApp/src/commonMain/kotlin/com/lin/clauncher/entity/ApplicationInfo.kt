package com.lin.comlauncher.entity

import androidx.compose.ui.graphics.ImageBitmap
import coil3.compose.AsyncImagePainter

class ApplicationInfo(
    var name: String? = null,
    var pageName: String? = null,
    var activityName: String? = null,
    var icon: ImageBitmap? = null,
    var posX: Int = 0,
    var posY: Int = 0,
    var width: Int = 0,
    var height: Int = 0,
    var isDrag: Boolean = false,
    var orignX: Int = 0,
    var orignY: Int = 0,
    var needMoveX: Int = 0,
    var needMoveY: Int = 0,
    var posFx: Float = 0f,
    var posFy: Float = 0f,
    var cellPos: Int = 0,
    var isAnimi: Boolean = false,
    var position: Int = 0,
    var iconWidth: Int = 0,
    var iconHeight: Int = 0,
    var dragInfo: ApplicationInfo? = null,
    var showText: Boolean = true,
    var imageBitmap: AsyncImagePainter? = null,
    var pagePos: Int = 0,
    var appType: Int = 0,
    var childs: ArrayList<ApplicationInfo> = ArrayList()
) {
    override fun toString(): String {
        return "${name}: position=${position}"
    }
}

class AppInfoBaseBean(
    var homeList: ArrayList<ArrayList<ApplicationInfo>> = ArrayList(),
    var toobarList: ArrayList<ApplicationInfo> = ArrayList(),
)

data class AppPos(
    var x: Int = 0,
    var y: Int = 0,
    var appName: String? = null
)

data class CellBean(
    var x: Int = 0,
    var y: Int = 0,
    var page: Int = 0
)

class AppOrignBean(
    var name: String? = null,
    var activityName: String?,
    var packageName: String? = "",
    var drawable: ImageBitmap?,
    var appType: Int = 0,
)

class AppManagerBean(
    var startX: Int = 0,
    var startY: Int = 0,
    var applicationInfo: ApplicationInfo
)