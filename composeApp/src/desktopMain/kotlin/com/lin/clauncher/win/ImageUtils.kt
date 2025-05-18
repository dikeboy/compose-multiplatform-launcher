package com.lin.clauncher.win

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.apache.commons.imaging.Imaging
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import javax.swing.filechooser.FileSystemView


object ImageUtils {
    fun readExeImage(path: String): BufferedImage? {
        val file = File(path)
        val icon = FileSystemView.getFileSystemView()
                .getSystemIcon(file)
        if(icon==null){
            return null;
        }
        // 获取图标的尺寸
        val width = icon.iconWidth
        val height = icon.iconHeight

        // 创建透明背景的 BufferedImage
        val image = BufferedImage(
            width,
            height,
            BufferedImage.TYPE_INT_ARGB // 保留透明度
        )

        // 获取 Graphics2D 上下文
        val g2 = image.createGraphics()

        // 启用抗锯齿
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )

        // 绘制图标到 BufferedImage
        icon.paintIcon(null, g2, 0, 0)

        // 释放资源
        g2.dispose()

        return image
    }
    fun readIconImage(path:String): ImageBitmap?{
        try {
            val file = File(path)
            val images = Imaging.getAllBufferedImages(file)
            images.sortBy { it.width }
            if(images.size>0){
                return  images[images.size-1].toComposeImageBitmap()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null;
    }
}