package com.lin.clauncher.win

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

class InstalledAppInfo {
    class AppInfo internal constructor(var name: String, var iconPath: String?,var installPath:String?) {
        override fun equals(other: Any?): Boolean {
            if (other is AppInfo) {
                if (other.name == name && other.iconPath == iconPath)
                    return true;
            }
            return false;
        }
    }

    fun getInstalledApps(): MutableList<AppInfo> {
        val appList: MutableList<AppInfo> = ArrayList()
        try {
            // 查询注册表命令
            val command =
                "reg query \"HKLM\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\" /s /f DisplayName /t REG_SZ"
            val process = Runtime.getRuntime().exec(command)
            System.out.println(command);

            BufferedReader(
                InputStreamReader(process.inputStream, StandardCharsets.UTF_16)
            ).use { reader ->
                var line: String?
                while ((reader.readLine().also { line = it }) != null) {
//                    var mLine = String(line!!.toByteArray(Charset.forName("UTF-16")));
                    var mLine = String(line!!.toByteArray(Charset.forName("UTF-16")), charset("GBK"))
//                     mLine = String(line!!.toByteArray(Charset.forName("UTF-8")));
                    var nameList = extractAppNames(mLine);
                    nameList.forEach { name ->
                        var str = name.split("____");
                        val name: String = str[0];
                        val iconPath = getIconPath(str[1])
                        if (iconPath != null && (iconPath.endsWith(".exe") || iconPath.endsWith(".ico"))) {
                            var installPath = getInstallLocation(str[1]);
                            var appInfo = AppInfo(name, iconPath,installPath)
                            if (!appList.contains(appInfo)) {
                                appList.add(appInfo)
                            }
                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return appList
    }

    fun extractAppNames(input: String): MutableList<String> {
        val names: MutableList<String> = ArrayList()
        val regex = "DisplayName\\s+REG_SZ\\s+(.+)"
        val pattern = Pattern.compile(regex)
        val lines = input.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var currentKey = "";

        for (line in lines) {
            if (line.contains("Uninstall")) {
                currentKey = line.substring(line.indexOf("Uninstall") + 10)
            }
            val matcher = pattern.matcher(line.trim { it <= ' ' })
            if (matcher.find()) {
                names.add(matcher.group(1) + "____" + currentKey)
            }
        }
        return names
    }

    private fun getIconPath(appKey: String): String? {
        try {
            // 查询图标路径
            val command =
                "reg query \"HKLM\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\$appKey\" /v DisplayIcon"
            val process = Runtime.getRuntime().exec(command)
            BufferedReader(
                InputStreamReader(process.inputStream, StandardCharsets.UTF_16)
            ).use { reader ->
                var line: String = ""
                if (reader != null) {
                    while ((reader.readLine()?.also { line = it }) != null) {

                        line = String(line.toByteArray(Charset.forName("UTF-16")));
//                        var mLine = String(line!!.toByteArray(Charset.forName("UTF-16")), charset("GBK"))
                        if (line.contains("REG_SZ")) {
                            line = line.substring(line.indexOf("REG_SZ") + 6);
                            line = line.trim()
                            if (line.startsWith("\""))
                                line = line.substring(1)
                            if (line.contains(".ico") || line.contains(".exe")) {
                                line = line.substring(0, line.lastIndexOf(".") + 4)
                            }
                            return line;
                        }
                    }
                }

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


    private fun getInstallLocation(appKey: String): String? {
        try {
            // 查询图标路径
            val command =
                "reg query \"HKLM\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\$appKey\" /v InstallLocation"
            val process = Runtime.getRuntime().exec(command)
            BufferedReader(
                InputStreamReader(process.inputStream, StandardCharsets.UTF_16)
            ).use { reader ->
                var line: String = ""
                if (reader != null) {
                    while ((reader.readLine()?.also { line = it }) != null) {

//                        line = String(line.toByteArray(Charset.forName("UTF-16")));
                        line= String(line!!.toByteArray(Charset.forName("UTF-16")), charset("UTF-8"))
                        if (line.contains("REG_SZ")) {
                            line = line.substring(line.indexOf("REG_SZ") + 6);
                            line = line.trim()
                            if (line.startsWith("\""))
                                line = line.substring(1)
                            if(line.contains('\n')){
                                line = line.subSequence(0,line.indexOf("\n")).toString()
                           }
                            line = line.replace("\r","")

                            return line;
                        }
                    }
                }

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}