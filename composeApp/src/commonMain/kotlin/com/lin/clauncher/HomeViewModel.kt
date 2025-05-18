package com.lin.clauncher

import androidx.lifecycle.ViewModel
import com.lin.clauncher.util.LauncherConfig
import com.lin.comlauncher.entity.AppInfoBaseBean
import com.lin.comlauncher.entity.ApplicationInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *@Author:        donghaolin
 *@CreateDate:    2025/5/14
 */
class HomeViewModel :ViewModel(){
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count
    var mAppInfoBaseBean: AppInfoBaseBean? = null

    suspend fun loadApp(width:Int,height:Int) {
        try {
            print("start load app width =$width height = $height")
            // 同步加载图片
            withContext(Dispatchers.Default) {
                val homeList = getPlatform().getAppList()
                var mList = ArrayList<ApplicationInfo>()
                var appBase = AppInfoBaseBean()
                LauncherConfig.HOME_WIDTH = width
                LauncherConfig.HOME_HEIGHT = height
                LauncherConfig.HOME_CELL_WIDTH = (width - LauncherConfig.HOME_DEFAULT_PADDING_LEFT * 2) / 4
                LauncherConfig.HOME_HEIGHT  = 100;
                LauncherConfig.CELL_ICON_WIDTH = 40
                LauncherConfig.HOME_TOOLBAR_START = height-100
                homeList.homeList.getOrNull(0)?.forEachIndexed { index, applicationInfo ->
                    if (index % 16 == 0) {
                        mList = ArrayList<ApplicationInfo>()
                        appBase.homeList.add(mList)
                    }
                    var pos = index %16
                    applicationInfo.width =  LauncherConfig.HOME_CELL_WIDTH
                    applicationInfo.height =    LauncherConfig.HOME_HEIGHT
                    applicationInfo.posX =  (pos %4)* LauncherConfig.HOME_CELL_WIDTH+LauncherConfig.HOME_CELL_LEFT_PADDING
                    applicationInfo.posY = (pos /4)*LauncherConfig.HOME_HEIGHT+LauncherConfig.DEFAULT_TOP_PADDING
                    applicationInfo.appType = LauncherConfig.CELL_TYPE_APP
                    applicationInfo.iconWidth  =  LauncherConfig.CELL_ICON_WIDTH
                    applicationInfo.iconHeight =  LauncherConfig.CELL_ICON_WIDTH
                    applicationInfo.position =  LauncherConfig.POSITION_HOME
                    applicationInfo.showText = true
                    mList.add(applicationInfo)
                }
                println("bitmap=${homeList.homeList.getOrNull(0)?.size}")
                mAppInfoBaseBean = appBase
            }
//                print("loadImage ${bitmap?.homeList?.size}")
            println("load Success")
            // 加载完成后回到主线程更新 UI
            withContext(Dispatchers.Main) {
                // 这里可以将 Bitmap 显示在 ImageView 中
                // 示例代码仅打印 Bitmap 宽高
//                    println("Image width: ${bitmap?.width}, height: ${bitmap?.height}")
            }
            _count.value++
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun decrement() {
        _count.value--
    }
}