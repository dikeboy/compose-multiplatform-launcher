package com.lin.clauncher

import android.os.Build
import com.lin.clauncher.util.ClientType
import com.lin.comlauncher.entity.AppInfoBaseBean
import com.lin.comlauncher.entity.ApplicationInfo

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"

    override suspend fun getAppList(): AppInfoBaseBean {
        var appInfoBaseBean = AppInfoBaseBean().apply {
            toobarList = ArrayList()
            var alist = ArrayList<ApplicationInfo>()
            MainActivity.mActivity?.getAllApps()?.let { allApps->
                allApps.forEach {
                    alist.add(
                        ApplicationInfo(
                            name = "${it.name}",
                            icon =it.drawable,
                            pageName =it.packageName
                        )
                    )
                }
            }

            homeList.add(alist)
        }


        return appInfoBaseBean

    }
}

actual fun getPlatform(): Platform = AndroidPlatform()
actual fun getPlatformType(): ClientType {
    return ClientType.MOBILE_ANDROID
}
actual fun getNativeResponse(input: Int): Int{
    return 0;
}

actual fun openFolder(app:ApplicationInfo) {
}