package com.lin.clauncher

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import clauncher.composeapp.generated.resources.Res
import clauncher.composeapp.generated.resources.mytest
import clauncher.composeapp.generated.resources.wall_paper
import coil3.ImageLoader
import coil3.PlatformContext
import com.lin.clauncher.util.ClientType
import com.lin.comlauncher.entity.AppInfoBaseBean
import com.lin.comlauncher.entity.ApplicationInfo
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.DensityQualifier
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getDrawableResourceBytes
import org.jetbrains.compose.resources.getSystemResourceEnvironment
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.skia.Image
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Rect
import org.jetbrains.skia.SamplingMode
import org.jetbrains.skia.Surface
import platform.Foundation.NSBundle
import platform.UIKit.UIDevice
import platform.Foundation.NSDictionary
import platform.UIKit.UIImage

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    @OptIn(ExperimentalForeignApi::class, ExperimentalResourceApi::class)
    override  suspend fun getAppList(): AppInfoBaseBean {
//        SwiftHelper.callSwiftMethod("")
        return AppInfoBaseBean().apply {
            nativeProvider?.getInstallApp()?.let {

                println("installapp=${it}")
                var resMap = Json.decodeFromString<List<Map<String,String>>>(it)
                var alist = ArrayList<ApplicationInfo>()
                resMap.forEach { list ->
//                    var image = nativeProvider?.loadImage(list.get("icon").toString())
                    imageResource(Res.drawable.mytest)
                    var drawable: DrawableResource =  Res.drawable.mytest
                    var byte = getDrawableResourceBytes(getSystemResourceEnvironment(),drawable)

                    alist.add(
                        ApplicationInfo(
                            name = "${list.get("name")}",
                            icon =  toImageBitmap(byte)
                        )
                    )
                }
                homeList.add(alist)
            }
        }


    }
    fun toImageBitmap(byteArray: ByteArray): ImageBitmap {
        val image = Image.makeFromEncoded(byteArray)

        val targetImage: Image
            val scale = 1f
            val targetH = image.height * scale
            val targetW = image.width * scale
            val srcRect = Rect.Companion.makeWH(image.width.toFloat(), image.height.toFloat())
            val dstRect = Rect.Companion.makeWH(targetW, targetH)

            targetImage = Surface.makeRasterN32Premul(targetW.toInt(), targetH.toInt()).run {
                val paint = Paint().apply { isAntiAlias = true }
                canvas.drawImageRect(image, srcRect, dstRect, SamplingMode.LINEAR, paint, true)
                makeImageSnapshot()
            }

        return targetImage.toComposeImageBitmap()
    }


}

actual fun getPlatform(): Platform = IOSPlatform()
actual fun openFolder(app:ApplicationInfo) {
}
actual fun getPlatformType(): ClientType {
    return ClientType.MOBILE_IOS
}
