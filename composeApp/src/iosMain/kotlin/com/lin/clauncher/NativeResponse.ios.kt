package com.lin.clauncher

import androidx.compose.ui.graphics.ImageBitmap
import platform.UIKit.UIImage

/**
 *@Author:        donghaolin
 *@CreateDate:    2025/5/16
 */
// instead of adding directly here 100 in ios, we will get this from swift
actual fun getNativeResponse(input: Int): Int {
    //return input + 100
     nativeProvider?.getInstallApp()
        ?: throw IllegalStateException("NativeResponseProvider not set")
    return 0;
}

// Create a native response provider interface
interface NativeResponseProvider {
    fun getInstallApp(): String?
    fun loadImage(named:String): UIImage?
}


// Add this variable to store the Swift implementation
 var nativeProvider: NativeResponseProvider? = null

// Add this function to be called from Swift
fun setNativeResponseProvider(provider: NativeResponseProvider) {
    nativeProvider = provider
}
