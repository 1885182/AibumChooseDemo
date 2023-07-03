package com.sszt.basis.ext.download

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.FileUtils
import com.sszt.basis.util.OpenFileUtils
import com.sszt.basis.weight.DialogView
import com.sszt.basis.ext.toast
import kotlinx.coroutines.launch
import java.io.File
import com.sszt.basis.ext.lookBigPicture


/**
 * 下载
 * @receiver FragmentActivity
 * @param url String 下载地址
 * @param saveName String 存储名称
 * @param isOpen Boolean 下载成功后是否直接打开
 * @param isJudge Boolean 是否判断文件已经存在
 * @param callBack  status：状态 pro：下载进度
 */
fun FragmentActivity.downloadDialogEx(
    url: String,
    saveName: String,
    isOpen: Boolean = true,
    isJudge: Boolean = true,
    callBack: (status: String, pro: Int) -> Unit
) {

    if (url.isNullOrEmpty()) {
        toast("${url}文件地址解析错误")
        return
    }
    val splitList = url.split(".")

    if (splitList[splitList.size - 1].contains(Regex("^jpg|png|gif|jpeg\$"))) {
        this.lookBigPicture(url)
        return
    }

    val fileExists = FileUtils.isFileExists(getDownloadFilePath(saveName))
    if (fileExists && isJudge) {
        DialogView.showConfirmCancelBtnDialog(
            content = "文件已经存在，是否重新下载？",
            title = "提示",
            cancelText = "重新下载",
            confirmText = "打开文件"
        ) {
            if (it == "重新下载") {
                downFileEx(url, saveName, isOpen, callBack)
            } else {
                OpenFileUtils.openFile(
                    this@downloadDialogEx,
                    FileUtils.getFileByPath("${getDownloadFilePath("fileDown")}/${saveName}")
                )
            }
        }
    } else {
        if (DownLoadPool.getPathFromKey(getDownloadFilePath(saveName)).isNullOrEmpty()) {
            downFileEx(url, saveName, isOpen, callBack)
        } else {
            DialogView.showConfirmCancelBtnDialog(
                content = "文件已下载一部分，是否继续下载？",
                title = "提示",
                cancelText = "重新下载",
                confirmText = "继续下载"
            ) {
                if (it == "重新下载") {
                    DownLoadPool.remove(url)
                }
                downFileEx(url, saveName, isOpen, callBack)
            }
        }
    }


}


fun FragmentActivity.getDownloadFilePath(fileName: String): String =
    "${cacheDir.absolutePath}/${fileName}"


/**
 * 下载
 * @receiver FragmentActivity
 * @param url String 下载地址
 * @param saveName String 存储名称
 * @param isOpen Boolean 下载成功后是否直接打开
 * @param callBack  status：状态 pro：下载进度
 */
fun FragmentActivity.downFileEx(
    url: String,
    saveName: String,
    isOpen: Boolean = true,
    callBack: (status: String, pro: Int) -> Unit
) {

    if (url.isNullOrEmpty()) {
        toast("${url}文件地址解析错误")
        return
    }


    lifecycleScope.launch {
        DownLoadManager.downLoad(saveName,
            url,
            getDownloadFilePath("fileDown"),
            saveName,
            true,
            object : OnDownLoadListener {
                override fun onDownLoadPrepare(key: String) {
                    callBack.invoke("Prepare", -1)
                }

                override fun onDownLoadError(key: String, throwable: Throwable) {
                    toast("下载失败：${throwable.message}")

                    callBack.invoke("Error", -2)
                    DownLoadPool.remove(key)
                }

                override fun onDownLoadSuccess(key: String, path: String, size: Long) {


                    callBack.invoke("Success", 200)

                    if (isOpen) {
                        OpenFileUtils.openFile(this@downFileEx, File(path))
                    }
                    DownLoadPool.remove(key)
                    toast("下载成功${path}")

                }

                override fun onDownLoadPause(key: String) {
                    callBack.invoke("Pause", -3)
                }

                override fun onUpdate(
                    key: String,
                    progress: Int,
                    read: Long,
                    count: Long,
                    done: Boolean
                ) {
                    callBack.invoke("Update", progress)
                }
            })
    }
}