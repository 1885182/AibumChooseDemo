package com.sszt.basis.util

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

import java.util.*

/**
 *
 * @param time 2021/6/15
 * @param des NotificationBarUtil.kt 通知栏显示的通知
 * @param author Meng
 *
 */
object NotificationBarUtil {

    /**
     * @param contex 上下文
     * @param title 标题
     * @param content 内容
     * @param smallIcon ，默认小图标
     * @param notificationChannel 版本大于等于Android8.0 需要设置的推送渠道
     * @param clazz 点击通知跳转的Activity
     */
    fun <T> sendNotification(
        contex: Context,
        title: String,
        content: String,
        smallIcon: Int,
        notificationChannelId: String,
        notificationChannelName: String,
        clazz: Class<T>
    ) {
        val intent = Intent()
        intent.setClass(contex, clazz)
        //点击通知带值跳转到Activity
        intent.putExtra("msgNotification", content)
        val pendingIntent =
            PendingIntent.getActivity(contex, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notifyManager =
            contex.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        var notification = NotificationCompat.Builder(contex)

        val random = Random().nextInt(99999)
        Log.e("TAG", "sendNotification: $random")
        //大于Android8.0要设置通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 传入参数：通道ID，通道名字，通道优先级（类似曾经的 builder.setPriority()）
            val channel = NotificationChannel(
                notificationChannelId,
                notificationChannelName,
                NotificationManager.IMPORTANCE_HIGH
            )


            //最后在 notificationManager 中创建该通知渠道
            notifyManager.createNotificationChannel(channel)
            notification = NotificationCompat.Builder(contex, notificationChannelId)
        }
        notification.setAutoCancel(true) // 设置该通知优先级
        notification.setPriority(Notification.PRIORITY_MAX)
        notification.setSmallIcon(smallIcon)
        notification.setContentTitle(title)
        notification.setContentText(content)

        notification.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        notification.setWhen(System.currentTimeMillis()) // 向通知添加声音、闪灯和振动效果
        notification.setDefaults(Notification.DEFAULT_VIBRATE or Notification.DEFAULT_ALL or Notification.DEFAULT_SOUND)
        notification.setContentIntent(pendingIntent)
        notifyManager.notify(random, notification.build())
    }

}