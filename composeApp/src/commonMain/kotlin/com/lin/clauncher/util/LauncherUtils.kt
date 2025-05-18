package com.lin.clauncher.util

import coil3.PlatformContext
import com.lin.clauncher.openFolder
import com.lin.comlauncher.entity.ApplicationInfo
import com.lin.comlauncher.entity.CellBean


object LauncherUtils {
    var TOOL_BAR_NAME =
        arrayListOf<String>("com.android.contacts", "com.android.camera", "com.android.mms", "com.android.browser")


    fun startApp( app: ApplicationInfo) {
        openFolder(app)
    }

    fun goAppDetail(context: PlatformContext, app: ApplicationInfo) {

    }

    fun addFoldToCurrentPage(list: ArrayList<ApplicationInfo>, currentPage: Int): ApplicationInfo? {
        if (list.size >= LauncherConfig.HOME_PAGE_CELL_MAX_NUM) {
            return null
        }
        var pos = list.size
        var appInfo = ApplicationInfo()
        appInfo.name = "文件夹"
        appInfo.appType = LauncherConfig.CELL_TYPE_FOLD
        appInfo.position = LauncherConfig.POSITION_TOOLBAR
        appInfo.width = LauncherConfig.HOME_CELL_WIDTH
        appInfo.height = LauncherConfig.HOME_CELL_HEIGHT
        appInfo.iconWidth = LauncherConfig.CELL_ICON_WIDTH
        appInfo.iconHeight = LauncherConfig.CELL_ICON_WIDTH
        appInfo.orignX = LauncherConfig.HOME_DEFAULT_PADDING_LEFT + (pos % 4) * LauncherConfig.HOME_CELL_WIDTH
        appInfo.orignY =
            pos / 4 * LauncherConfig.HOME_CELL_HEIGHT + LauncherConfig.DEFAULT_TOP_PADDING
        appInfo.cellPos = pos
        appInfo.pagePos = currentPage
        list.add(appInfo)
        return appInfo
    }

    fun goAppDelete(context: PlatformContext, app: ApplicationInfo) {

    }

    fun vibrator(context: PlatformContext) {

    }

    fun isToolBarApplication(packageName: String?): Boolean {
        return TOOL_BAR_NAME.contains(packageName)
    }

    fun findCurrentCell(posX: Int, posY: Int): CellBean? {
        if (posY < LauncherConfig.DEFAULT_TOP_PADDING) {
            return null
        }
        var cellX = posX / LauncherConfig.HOME_CELL_WIDTH
        var cellY = (posY - LauncherConfig.DEFAULT_TOP_PADDING) / LauncherConfig.HOME_CELL_HEIGHT
        return CellBean(cellX, cellY)
    }

    fun changeFoldPosition(list: ArrayList<ApplicationInfo>) {
        list.forEachIndexed { i, appInfo ->
            appInfo.posX = i % 4 * appInfo.width
            appInfo.posY = i / 4 * appInfo.height + 20
        }

    }
//
//    fun createFoldIcon(ai: ApplicationInfo) {
//        var imageWidth = DisplayUtils.dpToPx(LauncherConfig.CELL_ICON_WIDTH)
//        var padding = imageWidth / 4 / 4
//        var childWidth = imageWidth / 4
//        if (ai.childs.isNotEmpty()) {
//            var bmp = Bitmap.createBitmap(
//                imageWidth,
//                imageWidth, Bitmap.Config.ARGB_8888
//            )
//            var canvas = Canvas(bmp)
//            var paint = Paint()
//            paint.isAntiAlias = true
//            ai.childs.forEachIndexed { index, achild ->
//                if (index >= 9)
//                    return@forEachIndexed
//                achild.icon?.let { icon ->
//                    var childIcon = getRounderBitmap(icon, DisplayUtils.dpToPx(8).toFloat());
//                    var px = padding + (childWidth + padding) * (index % 3)
//                    var py = padding + (childWidth + padding) * (index / 3)
//                    canvas.drawBitmap(
//                        childIcon, Rect(0, 0, icon.width, icon.height),
//                        Rect(px, py, childWidth + px, childWidth + py), paint
//                    )
//                }
//
//            }
//            ai.icon = bmp
//        }
//    }
//
//    fun getRounderBitmap(oldBmp: Bitmap, rounder: Float): Bitmap {
//        var bmp = Bitmap.createBitmap(
//            oldBmp.width,
//            oldBmp.height, Bitmap.Config.ARGB_8888
//        )
//        var canvas = Canvas(bmp)
//        var paint = Paint()
//        var rect = Rect(0, 0, oldBmp.width, oldBmp.height)
//        canvas.drawRoundRect(RectF(rect), rounder, rounder, paint)
//        paint.isAntiAlias = true
//        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
//        canvas.drawBitmap(
//            oldBmp, Rect(0, 0, oldBmp.width, oldBmp.height), Rect(0, 0, oldBmp.width, oldBmp.height), paint
//        )
//        return bmp;
//    }

}