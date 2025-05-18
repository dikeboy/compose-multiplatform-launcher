package com.lin.clauncher

import com.lin.clauncher.util.ClientType
import com.lin.comlauncher.entity.AppInfoBaseBean
import com.lin.comlauncher.entity.ApplicationInfo

interface Platform {
    val name: String
    suspend fun getAppList(): AppInfoBaseBean
}

expect fun getPlatform(): Platform
expect fun getPlatformType(): ClientType
expect fun openFolder(app: ApplicationInfo)
expect fun getNativeResponse(input: Int): Int

