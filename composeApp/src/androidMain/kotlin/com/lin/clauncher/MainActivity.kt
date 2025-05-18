package com.lin.clauncher

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import coil3.asImage
import com.lin.comlauncher.entity.AppOrignBean

class MainActivity : ComponentActivity() {
    companion object{
        var mActivity:MainActivity?=null
    }
    var permissionManager = PermissionManager(101)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var arrayPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            arrayOf(Manifest.permission.QUERY_ALL_PACKAGES, Manifest.permission.VIBRATE)
        else arrayOf(Manifest.permission.VIBRATE)
        permissionManager.checkPermission(this, arrayPermission) {
            var width = resources.displayMetrics.widthPixels
            var height = getScreenHeight3(this)

            mActivity=this

//            var height = resources.displayMetrics.heightPixels+ImmersionBar.getStatusBarHeight(this)
        }
        setContent {
            App()
        }
    }

    fun getAllApps():MutableList<AppOrignBean>{
        var orignList = mutableListOf<AppOrignBean>()
        var intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        packageManager.queryIntentActivities(intent, 0)?.forEach {
            (it.activityInfo.loadIcon(packageManager) as? BitmapDrawable)?.let { drawable ->
                orignList.add(
                    AppOrignBean(
                        name = it.loadLabel(packageManager).toString(),
                        activityName = it.activityInfo.name,
                        packageName = it.activityInfo.packageName,
                        drawable = drawable.bitmap.asImageBitmap(),
//                    appType = LauncherConfig.CELL_TYPE_APP
                    )
                )
            }
        }
        return orignList

    }


    fun getScreenHeight3(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val defaultDisplay = windowManager.defaultDisplay
        val outPoint = Point()
        defaultDisplay.getRealSize(outPoint)
        return outPoint.y
    }

}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}