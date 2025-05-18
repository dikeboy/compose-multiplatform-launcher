package com.lin.clauncher

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import clauncher.composeapp.generated.resources.Res
import clauncher.composeapp.generated.resources.mytest
import clauncher.composeapp.generated.resources.wall_paper
import coil3.util.DebugLogger
import coil3.util.Logger
import com.lin.clauncher.util.ClientType
import com.lin.comlauncher.entity.AppInfoBaseBean
import com.lin.comlauncher.entity.ApplicationInfo
import kotlinx.browser.document
import kotlinx.coroutines.suspendCancellableCoroutine
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.getDrawableResourceBytes
import org.jetbrains.compose.resources.getSystemResourceEnvironment
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.Uint8ClampedArray
import org.khronos.webgl.set
import org.w3c.dom.HTMLImageElement
import org.w3c.files.Blob
import org.w3c.dom.Image
import org.w3c.dom.ImageData
import org.w3c.dom.url.URL
import org.w3c.fetch.Response
import org.w3c.files.BlobPropertyBag
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
    override suspend fun getAppList(): AppInfoBaseBean {
        return AppInfoBaseBean().apply {
            var alist = ArrayList<ApplicationInfo>()
                alist.add(
                    ApplicationInfo(
                    name="test",
                    icon = getImageResource("")
                )
            )
            print(" add finish")
            homeList.add(alist)
        }
    }

     @OptIn(ExperimentalResourceApi::class)
     suspend fun getImageResource(res: String): ImageBitmap? {
         var drawable: DrawableResource =  Res.drawable.mytest
         var byte = getDrawableResourceBytes(getSystemResourceEnvironment(),drawable)
         return byteArrayToImageBitmap(byte);
    }

    suspend fun byteArrayToImageBitmap(
        byteArray: ByteArray,
        mimeType: String = "image/png"
    ): ImageBitmap? {
        val blob = Blob(toUint8Array(byteArray), BlobPropertyBag(type = mimeType))
        println("blog ${blob.size}  type=${blob}")
        window.createImageBitmap(blob).then(onFulfilled = {
          println("onFulFilled ${it}")
          null},
          onRejected = {
              println("onRejected $it")
              null
          })
        return null;
//        var jsArr = getResourceBytes("images/logo-facebook.png")
//         window.createImageBitmap(ImageData(Uint8ClampedArray(buffer = jsArr,0,jsArr.byteLength),0,0)).then(onFulfilled = {
//            println("onFulFilled ${it}")
//            null},
//            onRejected = {
//                println("onRejected $it")
//                null
//            })
//        return null;

    }

    // 工具函数：读取资源文件为 ByteArray
    suspend fun getResourceBytes(path: String): ArrayBuffer {
        val response = window.fetch(path).await<Response>()
        if (!response.ok) throw Exception("资源未找到: $path")
        var arrayBuffer =  response.arrayBuffer().await<ArrayBuffer>()
        return arrayBuffer
    }

    fun toUint8Array( byteArray: ByteArray): JsArray<JsAny?> {
        val jsArray =  ArrayList<JsAny>()
        byteArray.forEachIndexed { index, byte ->
            // 将有符号 Byte 转换为无符号数值
            jsArray.add((byte.toInt() and 0xFF).toJsNumber())
//            jsArray.add((byte.toInt()).toJsNumber())
        }
        return  jsArray.toJsArray()
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