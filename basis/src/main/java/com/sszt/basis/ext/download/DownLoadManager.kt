package com.sszt.basis.ext.download

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Looper
import androidx.fragment.app.FragmentActivity
import com.hjq.toast.ToastUtils
import com.permissionx.guolindev.PermissionX
import com.sszt.basis.ext.util.logi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * @author : hgj
 * @date   : 2020/7/13
 */

object DownLoadManager {
    private val retrofitBuilder by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.baidu.com")
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS).build()
            ).build()
    }

    /**
     *开始下载
     * @param tag String 标识
     * @param url String  下载的url
     * @param savePath String 保存的路径
     * @param saveName String 保存的名字
     * @param reDownload Boolean 如果文件已存在是否需要重新下载 默认不需要重新下载
     * @param loadListener OnDownLoadListener
     */
    suspend fun downLoad(
        tag: String,
        url: String,
        savePath: String,
        saveName: String,
        reDownload: Boolean = false,
        loadListener: OnDownLoadListener
    ) {

        withContext(Dispatchers.IO) {
            doDownLoad(tag, url, savePath, saveName, reDownload, loadListener, this)
        }

    }

    /**
     * 取消下载
     * @param key String 取消的标识
     */
    fun cancel(key: String) {
        val path = DownLoadPool.getPathFromKey(key)
        if (path != null) {
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
        }
        DownLoadPool.remove(key)
    }

    /**
     * 暂停下载
     * @param key String 暂停的标识
     */
    fun pause(key: String) {
        val listener = DownLoadPool.getListenerFromKey(key)
        listener?.onDownLoadPause(key)
        DownLoadPool.pause(key)
    }

    /**
     * 取消所有下载
     */
    fun doDownLoadCancelAll() {
        DownLoadPool.getListenerMap().forEach {
            cancel(it.key)
        }
    }

    /**
     * 暂停所有下载
     */
    fun doDownLoadPauseAll() {
        DownLoadPool.getListenerMap().forEach {
            pause(it.key)
        }
    }

    /**
     *下载
     * @param tag String 标识
     * @param url String  下载的url
     * @param savePath String 保存的路径
     * @param saveName String 保存的名字
     * @param reDownload Boolean 如果文件已存在是否需要重新下载 默认不需要重新下载
     * @param loadListener OnDownLoadListener
     * @param coroutineScope CoroutineScope 上下文
     */
    private suspend fun doDownLoad(
        tag: String,
        url: String,
        savePath: String,
        saveName: String,
        reDownload: Boolean,
        loadListener: OnDownLoadListener,
        coroutineScope: CoroutineScope
    ) {
        //判断是否已经在队列中
        val scope = DownLoadPool.getScopeFromKey(tag)
        if (scope != null && scope.isActive) {
            "已经在队列中".logi()
            return
        } else if (scope != null && !scope.isActive) {
            "key $tag 已经在队列中 但是已经不再活跃 remove".logi()
            DownLoadPool.removeExitSp(tag)
        }

        if (saveName.isEmpty()) {
            withContext(Dispatchers.Main) {
                loadListener.onDownLoadError(tag, Throwable("save name is Empty"))
            }
            return
        }

        if (Looper.getMainLooper().thread == Thread.currentThread()) {
            withContext(Dispatchers.Main) {
                loadListener.onDownLoadError(tag, Throwable("current thread is in main thread"))
            }
            return
        }

        val file = File("$savePath/$saveName")
        val currentLength = if (!file.exists()) {
            0L
        } else {
            ShareDownLoadUtil.getLong(tag, 0)
        }
        if (file.exists() && currentLength == 0L && !reDownload) {
            //文件已存在了
            loadListener.onDownLoadSuccess(tag, file.path, file.length())
            return
        }
        "startDownLoad current $currentLength".logi()

        try {
            //添加到pool
            DownLoadPool.add(tag, coroutineScope)
            DownLoadPool.add(tag, "$savePath/$saveName")
            DownLoadPool.add(tag, loadListener)

            withContext(Dispatchers.Main) {
                loadListener.onDownLoadPrepare(key = tag)
            }
            val response = retrofitBuilder.create(DownLoadService::class.java)
                .downloadFile("bytes=$currentLength-", url)
            val responseBody = response.body()
            if (responseBody == null) {
                "responseBody is null".logi()
                withContext(Dispatchers.Main) {
                    loadListener.onDownLoadError(
                        key = tag,
                        throwable = Throwable("${response.code()}>>>${response.message()}")
                    )
                }
                DownLoadPool.remove(tag)
                return
            }
            FileTool.downToFile(
                tag,
                savePath,
                saveName,
                currentLength,
                responseBody,
                loadListener
            )
        } catch (throwable: Throwable) {
            withContext(Dispatchers.Main) {
                loadListener.onDownLoadError(key = tag, throwable = throwable)
            }
            DownLoadPool.remove(tag)
        }
    }

    /**
     *
     *
     * @param mContext
     * @param serviceName
     * 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    fun isServiceWork(mContext: Context, serviceName: String): Boolean {
        var isWork = false
        val myAM = mContext
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val myList: List<ActivityManager.RunningServiceInfo> =
            myAM.getRunningServices(Int.MAX_VALUE)
        if (myList.size <= 0) {
            return false
        }
        for (i in myList.indices) {
            val mName: String = myList[i].service.getClassName().toString()
            if (mName == serviceName) {
                isWork = true
                break
            }
        }
        return isWork
    }


}


