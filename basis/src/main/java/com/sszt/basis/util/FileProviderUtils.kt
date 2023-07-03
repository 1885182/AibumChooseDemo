package com.sszt.basis.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File


/**
 * @author ：MENG
 * @date : 2019/8/3 17:27
 * @package：com.yaoqi.firecontrolgis.Utils
 * @description :
 */
internal object FileProviderUtils {
    fun getUriForFile(mContext: Context?, file: File?): Uri? {
        var fileUri: Uri? = null
        fileUri = if (Build.VERSION.SDK_INT >= 24) {
            getUriForFile24(mContext, file)
        } else {
            Uri.fromFile(file)
        }
        return fileUri
    }

    /**
     * getUriForFile@TODO getUriForFile .fileprovider 是不是和 配置的  <provider> "authorities" </provider> 一样
     * "com.sszt.grassrootsgovernance"
     */
    fun getUriForFile24(mContext: Context?, file: File?): Uri {
        return FileProvider.getUriForFile(
            mContext!!, mContext.applicationContext.packageName+ ".fileprovider",
            file!!
        )
    }

    fun setIntentDataAndType(
        mContext: Context?,
        intent: Intent,
        type: String?,
        file: File?,
        writeAble: Boolean
    ) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(getUriForFile(mContext, file), type)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type)
        }
    }
}