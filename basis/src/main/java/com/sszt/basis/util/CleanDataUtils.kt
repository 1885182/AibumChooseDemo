package com.sszt.basis.util

import android.content.Context
import android.os.Environment
import java.io.File
import java.math.BigDecimal

object CleanDataUtils {
    /**
     * Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
     * Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     */
    /**
     * 获取缓存值
     */
    fun getTotalCacheSize(context: Context): String {

        val ss = context.externalCacheDir
        val sp = ss?.absolutePath

        var cacheSize = getFolderSize(context.getCacheDir())
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir())
        }
        return getFormatSize(cacheSize.toDouble())
    }

    /**
     * 清除所有缓存
     */
    fun clearAllCache(context: Context) {
        deleteDir(context.cacheDir)
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir())
            //TODO 有网页清理时注意排错，是否存在/data/data/应用package目录下找不到database文件夹的问题
            context.deleteDatabase("webview.db")
            context.deleteDatabase("webviewCache.db")
        }
    }

    /**
     * 删除某个文件
     */
    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory()) {
            val children: Array<String> = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            return dir.delete()
        }
        return if (dir != null) {
            dir.delete()
        } else {
            false
        }
    }

    /**
     * 获取文件
     */
    private fun getFolderSize(file: File?): Long {
        var size: Long = 0
        if (file != null) {
            val fileList: Array<File> = file.listFiles()
            if (fileList != null && fileList.size > 0) {
                for (i in fileList.indices) {
                    // 如果下面还有文件
                    if (fileList[i].isDirectory()) {
                        size = size + getFolderSize(fileList[i])
                    } else {
                        size = size + fileList[i].length()
                    }
                }
            }
        }
        return size
    }

    /**
     * 格式化单位
     */
    private fun getFormatSize(size: Double): String {
        val kiloByte = size / 1024
        val megaByte = kiloByte / 1024
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(java.lang.Double.toString(megaByte))
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString().toString() + "MB"
        }
        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(java.lang.Double.toString(gigaByte))
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString().toString() + "GB"
        }
        val result4: BigDecimal = BigDecimal.valueOf(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
            .toString() + "TB"
    }
}