package com.lin.clauncher.mac

import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO


object AppIconFetcher {

    fun getInstalledApplications(fileList: MutableList<String>, path: String, start: Int, deep: Int) {
        // 查找/Applications目录下的所有.app文件
        val file = File(path)
        val files = file.listFiles()
        for (f in files) {
            if (f.absolutePath.endsWith(".app")) {
                if(!fileList.contains(f.getAbsolutePath()))
                    fileList.add(f.getAbsolutePath());
            }
            else if (f.isDirectory) {
                if (start >= deep) {
                    continue
                }
                getInstalledApplications(fileList, f.absolutePath, start + 1, deep)
            }
        }
    }

    // 获取应用程序的图标路径
    fun getAppIconPath(appPath: String): String? {
        // 构建可能的图标路径
        val possibleIconNames = arrayOf(
            "AppIcon.icns",
            "app.icns",
            "application.icns",
            "icon.icns"
        )

        val resourcesPath = "$appPath/Contents/Resources/"

        for (iconName in possibleIconNames) {
            val iconFile = File(resourcesPath + iconName)
            if (iconFile.exists()) {
                return iconFile.absolutePath
            }
        }

        return null
    }

    // 将.icns转换为PNG格式
    @Throws(Exception::class)
    fun convertIcnsToPng(icnsPath: String?): BufferedImage {
        // 创建临时文件
        val tempPng = File.createTempFile("icon", ".png")
        tempPng.deleteOnExit()

        // 使用sips命令转换格式
        val pb = ProcessBuilder(
            "sips",
            "-s", "format", "png",
            icnsPath,
            "--out", tempPng.absolutePath
        )

        val process = pb.start()
        try {
            process.waitFor()
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
        // 读取生成的PNG文件
        return ImageIO.read(tempPng)
    }

    // 获取所有应用的图标
    @Throws(IOException::class)
    fun getAllAppIcons(): Map<String, BufferedImage> {
        val icons: MutableMap<String, BufferedImage> = HashMap()
        val fileList = ArrayList<String>()
        getInstalledApplications(fileList,"/Applications",0,1);
        getInstalledApplications(fileList,"/System/Applications",0,1);

        for (app in fileList) {
            val name = app.substring(app.lastIndexOf("/") + 1, app.lastIndexOf("."))
            val iconPath = getAppIconPath(app)
            if (iconPath != null) {
                try {
                    val icon = convertIcnsToPng(iconPath)
                    icons[name] = icon
                } catch (e: Exception) {
                    System.err.println("无法转换图标: " + app + " - " + e.message)
                }
            }
        }

        return icons
    }
}