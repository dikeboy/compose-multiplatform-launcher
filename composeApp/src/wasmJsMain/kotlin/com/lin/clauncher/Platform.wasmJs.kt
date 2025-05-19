package com.lin.clauncher

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import clauncher.composeapp.generated.resources.Res
import clauncher.composeapp.generated.resources.mytest
import com.lin.clauncher.util.ClientType
import com.lin.comlauncher.entity.AppInfoBaseBean
import com.lin.comlauncher.entity.ApplicationInfo
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.getDrawableResourceBytes
import org.jetbrains.compose.resources.getSystemResourceEnvironment
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Rect
import org.jetbrains.skia.SamplingMode
import org.jetbrains.skia.Surface
import org.khronos.webgl.ArrayBuffer
import org.w3c.fetch.Response
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
    @OptIn(ExperimentalResourceApi::class)
    override suspend fun getAppList(): AppInfoBaseBean {
        var map = mutableMapOf("facebook" to "logo-facebook.png",
            "gmail" to "logo-gmail.png", "google" to "logo-google.png",
            "youtube" to "logo-youtube.png")
        return AppInfoBaseBean().apply {
            var alist = ArrayList<ApplicationInfo>()
              map.forEach {
                  var drawable: DrawableResource =  readDrawableByte(it.value)
                  var byte = getDrawableResourceBytes(getSystemResourceEnvironment(),drawable)
                  alist.add(
                      ApplicationInfo(
                          name=it.key,
                          icon = toImageBitmap(byte)
                      ))
              }
            homeList.add(alist)
        }
    }

    fun toImageBitmap(byteArray: ByteArray): ImageBitmap {
        val image = org.jetbrains.skia.Image.makeFromEncoded(byteArray)
        val targetImage: org.jetbrains.skia.Image
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

    @OptIn(InternalResourceApi::class)
    fun readDrawableByte(imageName:String ):DrawableResource{
        return DrawableResource(
            "drawable:$imageName",
            setOf(
                org.jetbrains.compose.resources.ResourceItem(setOf(),
                    "images/$imageName", -1, -1),
            )
        )
    }
}

actual fun getPlatform(): Platform = WasmPlatform()
actual fun getPlatformType(): ClientType {
    return ClientType.WEB
}
actual fun getNativeResponse(input: Int): Int {
    return 0;
}
actual fun openFolder(app:ApplicationInfo) {
}
