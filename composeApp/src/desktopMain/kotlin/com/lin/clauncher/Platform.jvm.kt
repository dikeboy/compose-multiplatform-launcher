package com.lin.clauncher

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.lin.clauncher.mac.AppIconFetcher
import com.lin.clauncher.util.ClientType
import com.lin.clauncher.win.ImageUtils
import com.lin.clauncher.win.InstalledAppInfo
import com.lin.comlauncher.entity.AppInfoBaseBean
import com.lin.comlauncher.entity.ApplicationInfo
import org.apache.commons.imaging.Imaging
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset
import javax.imageio.ImageIO


class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override suspend fun getAppList(): AppInfoBaseBean {
        return AppInfoBaseBean().apply {
            val osName = System.getProperty("os.name").lowercase()
            println("osName=$osName")
            toobarList = ArrayList()
            var alist = ArrayList<ApplicationInfo>()

            if(osName.contains("mac")){
                var bufferImg =  AppIconFetcher.getAllAppIcons()

                bufferImg.forEach { k,v ->
                    alist.add(
                        ApplicationInfo(
                            name = "${k}",
                            icon = v.toComposeImageBitmap()
                        )
                    )
                }
            }else if(osName.contains("win")){

                 var list = InstalledAppInfo().getInstalledApps()
                 System.out.println("size=${list.size}")
                list.forEach {
                    it.iconPath?.let { iconPath->
                        if(iconPath.endsWith("ico")){
                            ImageUtils.readIconImage(iconPath)?.let {bitmap->
                                alist.add(
                                    ApplicationInfo(
                                        name = String(it.name.toByteArray(Charset.forName("UTF-8"))),
                                        icon = bitmap
                                    )
                                )
                            }
                        }else if(iconPath.endsWith("exe")){
                            ImageUtils.readExeImage(iconPath)?.let {bitmap->
                                alist.add(
                                    ApplicationInfo(
                                        name = String(it.name.toByteArray(Charset.forName("UTF-8"))),
                                        icon = bitmap.toComposeImageBitmap(),
                                        pageName = it.installPath
                                    )
                                )
                            }
                        }

                    }
//                    System.out.println("name=${it.name} icon=${it.iconPath}");
                }

            }

            homeList.add(alist)

        }
    }



     fun imageResource(res: String): ImageBitmap {
        val inputStream: InputStream = object {}.javaClass.getResourceAsStream("/assets/$res")
            ?: throw IllegalArgumentException("Resource $res not found")
        val bufferedImage = ImageIO.read(inputStream)
        return bufferedImage.toComposeImageBitmap()
    }
//
}



actual fun getPlatform(): Platform = JVMPlatform()
actual fun getNativeResponse(input: Int): Int{
    return 0;
}
actual fun getPlatformType(): ClientType {
    val osName = System.getProperty("os.name").orEmpty()
    return when {
        osName.startsWith("Win") -> ClientType.DESKTOP_WINDOWS
        osName.startsWith("Mac") -> ClientType.DESKTOP_MACOS
        osName.startsWith("Linux") -> ClientType.DESKTOP_LINUX
        else -> ClientType.UNKNOWN
    }
}

actual fun openFolder(app:ApplicationInfo) {
    app.pageName?.let {
        openFolderInExplorer(it)

    }
}

fun openFolderInExplorer(folderPath: String) {
    var encodePath = folderPath.replaceFirst("\\","\\\\");

    val folder = File(encodePath)
    println("==== ${encodePath}")
    println("equal="+(encodePath=="F:\\\\game\\Cheat Engine 7.3\\"))

    if (folder.exists() && folder.isDirectory) {
        // 获取平台特定命令
        val os = System.getProperty("os.name").lowercase()
        val command = when {
            os.contains("win") -> arrayOf("explorer.exe", folder.absolutePath)
            os.contains("nix") || os.contains("nux") -> arrayOf("xdg-open", folder.absolutePath)
            os.contains("mac") -> arrayOf("open", folder.absolutePath)
            else -> throw UnsupportedOperationException("不支持的操作系统")
        }

        try {
            // 执行命令
            Runtime.getRuntime().exec(command)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } else {
        println("文件夹不存在: $folderPath")
    }
}