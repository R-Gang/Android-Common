package com.gang.app.common.utils

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.RemoteViews
import com.alibaba.sdk.android.ams.common.global.AmsGlobalHolder.getPackageName
import com.alibaba.sdk.android.push.notification.CPushMessage
import com.gang.app.MyApplication
import com.gang.app.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Muzik
 * 2018/12/20 10:17
 */
class NotificationUtil {

    companion object {

        /**
         * 接受到对应消息后，消息的弹出处理
         */
        public fun buildNotification(context: Context?, message: CPushMessage) {
            val notificationManager: NotificationManager = MyApplication.createNotificationChannel()
            val remoteViews = RemoteViews(getPackageName(), R.layout.custom_notification)
            remoteViews.setImageViewResource(R.id.m_icon, R.mipmap.icon_empty)
            remoteViews.setTextViewText(R.id.m_title, message.getTitle())
            remoteViews.setTextViewText(R.id.m_text, message.getContent())
            remoteViews.setTextViewText(R.id.text, SimpleDateFormat("HH:mm").format(Date()))
            val builder: Notification.Builder
            builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder(context, getPackageName())
            } else {
                Notification.Builder(context)
            }

            // 通知栏点击打开方式
//            val uid = U.getPreferences("uid", "") as String
//            val token = U.getPreferences("token", "") as String
//            if (StrUtils.isNotEmpty(uid)) {
//                UserManager.getInstance().autoLogin(context, uid, token) { contentIntent ->
//                    builder.setContentIntent(contentIntent)
//                    EventBus.getDefault().post(ToUIEvent(ToUIEvent.STATE_EVENT, 0))
//                    builder.setContent(remoteViews)
//                        .setContentTitle(message.getTitle())
//                        .setContentText(message.getContent())
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setDefaults(Notification.DEFAULT_VIBRATE)
//                        .setPriority(Notification.PRIORITY_DEFAULT)
//                        .setAutoCancel(true)
//                    builder.build().flags = Notification.FLAG_AUTO_CANCEL
//                    notificationManager.notify(message.hashCode(), builder.build())
//                }
//            }
        }
    }
}