package com.sszt.basis.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sszt.basis.ext.util.loge

class UpdateAppBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "com.sszt.broadcastreceiver.updateapp") {
            val status = intent.getStringExtra("status") ?: ""
            val path = intent.getStringExtra("path") ?: ""
            val progress = intent.getIntExtra("progress", 0)
            "$status===$progress".loge("广播接收UpdateAppBroadcastReceiver")
            mIUpdateAppBroadcastReceiverCallBack?.callBack(path,status, progress)
        }

    }

    var mIUpdateAppBroadcastReceiverCallBack: IUpdateAppBroadcastReceiverCallBack? = null

    interface IUpdateAppBroadcastReceiverCallBack {

        fun callBack(path: String,status: String, progress: Int)

    }

}