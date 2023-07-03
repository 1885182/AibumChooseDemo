package com.sszt.basis.service


import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import com.sszt.basis.ext.download.DownLoadManager
import com.sszt.basis.ext.download.DownLoadPool
import com.sszt.basis.ext.download.OnDownLoadListener
import com.sszt.basis.ext.toast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference


class UpdateAppService : Service() {

    companion object {
        var downLoadIsStart = false
        var mContext: WeakReference<Context>? = null
    }

    private val mBinder = UpdateAppServiceBinder()
     var downCallbackProgress: IDownCallback? = null

    override fun onBind(intent: Intent?): IBinder {
        mContext = WeakReference(applicationContext)
        return mBinder
    }

    override fun unbindService(conn: ServiceConnection) {
        super.unbindService(conn)
        mJob?.cancel()
    }


    var mJob: Job? = null
    override fun onDestroy() {
        mJob?.cancel()
        super.onDestroy()

    }

    fun updateApp(url: String, destFileDir: String, destFileName: String) {
        mJob = GlobalScope.launch {

            DownLoadManager.downLoad(
                "updateApp",
                url,
                destFileDir,
                destFileName,
                true,
                object : OnDownLoadListener {
                    override fun onDownLoadPrepare(key: String) {

                        downLoadIsStart = false
                    }

                    override fun onDownLoadError(key: String, throwable: Throwable) {

                        toast("下载失败${throwable.message}")
                        mJob?.cancel()
                        downCallbackProgress?.error(throwable.message)
                        val intent = Intent();
                        //设置广播的名字（设置Action）
                        intent.setAction("com.sszt.broadcastreceiver.updateapp");
                        //携带数据
                        intent.putExtra("status", "下载失败");
                        //发送广播（无序广播）
                        mContext?.get()?.sendBroadcast(intent)
                        DownLoadPool.remove(key)
                    }

                    override fun onDownLoadSuccess(key: String, path: String, size: Long) {


                        mJob?.cancel()
                        downLoadIsStart = false

                        toast("下载完成${path}")
                        stopSelf()
                        downCallbackProgress?.success(path)
                        val intent = Intent();
                        //设置广播的名字（设置Action）
                        intent.setAction("com.sszt.broadcastreceiver.updateapp");
                        //携带数据
                        intent.putExtra("status", "下载完成");
                        intent.putExtra("path", path);
                        //发送广播（无序广播）
                        mContext?.get()?.sendBroadcast(intent)
                        DownLoadPool.remove(key)

                    }

                    override fun onDownLoadPause(key: String) {

                        downLoadIsStart = false
                    }

                    override fun onUpdate(
                        key: String,
                        progress: Int,
                        read: Long,
                        count: Long,
                        done: Boolean
                    ) {
                        downCallbackProgress?.process(progress)

                        downLoadIsStart = true

                        val intent = Intent();
                        //设置广播的名字（设置Action）
                        intent.setAction("com.sszt.broadcastreceiver.updateapp");
                        //携带数据
                        intent.putExtra("status", "正在下载");
                        intent.putExtra("progress", progress);
                        //发送广播（无序广播）
                        mContext?.get()?.sendBroadcast(intent);


                    }
                })
        }


    }


    //用于Activity和service通讯
    inner class UpdateAppServiceBinder : Binder() {
        fun getService() = UpdateAppService()

    }

    interface IDownCallback {
        fun process(i: Int)
        fun success(path: String?)
        fun error(errMsg: String?)
    }


}