package com.gang.app.common.utils

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import com.alibaba.sdk.android.push.notification.CPushMessage
import com.gang.app.MyApplication
import com.gang.app.R
import com.gang.app.common.user.ToUIEvent
import com.gang.app.ui.activity.MainActivity
import com.gang.library.common.EventBus
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by haoruigang
 * 2018/12/20 10:17
 */
class NotificationUtil {

    companion object {

        var clickNotificationCode = 888

        /**
         * 接受到对应消息后，消息的弹出处理
         */
        public fun buildNotification(context: Context?, message: CPushMessage) {
            val notificationManager: NotificationManager =
                MyApplication.instance.createNotificationChannel()
            val remoteViews = RemoteViews(context?.packageName, R.layout.custom_notification)
            remoteViews.setImageViewResource(R.id.m_icon, R.mipmap.laopo)
            remoteViews.setTextViewText(R.id.m_title, message.getTitle())
            remoteViews.setTextViewText(R.id.m_text, message.getContent())
            remoteViews.setTextViewText(R.id.text, SimpleDateFormat("HH:mm").format(Date()))
            val builder: Notification.Builder =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Notification.Builder(context, context?.packageName)
                } else {
                    Notification.Builder(context)
                }

            // 通知栏点击打开方式

            val intent = Intent(context, MainActivity::class.java)
            intent.action = Intent.ACTION_MAIN
            // appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED //关键的一步，设置启动模式
            val contentIntent = PendingIntent.getActivity(
                context,
                clickNotificationCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            builder.setContentIntent(contentIntent)
            EventBus.postSticky(ToUIEvent(ToUIEvent.MESSAGE_EVENT, 0))
            builder.setContent(remoteViews)
                .setContentTitle(message.getTitle())
                .setContentText(message.getContent())
                .setSmallIcon(R.mipmap.laopo)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true)
            builder.build().flags = Notification.FLAG_AUTO_CANCEL
            notificationManager.notify(message.hashCode(), builder.build())
        }
    }
}