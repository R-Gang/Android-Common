package com.gang.app.data

import android.content.Context
import android.util.Log
import com.alibaba.sdk.android.push.MessageReceiver
import com.alibaba.sdk.android.push.notification.CPushMessage
import com.gang.app.common.utils.NotificationUtil.Companion.buildNotification

/**
 * @author: 正纬
 * @since: 15/4/9
 * @version: 1.1
 * @feature: 用于接收推送的通知和消息
 */
class MyMessageReceiver : MessageReceiver() {
    /**
     * 推送通知的回调方法
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    public override fun onNotification(
        context: Context,
        title: String,
        summary: String,
        extraMap: Map<String, String>,
    ) {
        // TODO 处理推送通知
        if (null != extraMap) {
            for ((key, value) in extraMap) {
                Log.i(REC_TAG, "onNotification @Get diy param : Key=$key , Value=$value")
            }
        } else {
            Log.i(REC_TAG, "onNotification @收到通知 && 自定义消息为空")
        }
        Log.i(REC_TAG, "onNotification 收到一条推送通知 ： $title, summary:$summary")
        val cPushMessage = CPushMessage()
        cPushMessage.title = title
        cPushMessage.content = summary
        buildNotification(context, cPushMessage)
    }

    /**
     * 应用处于前台时通知到达回调。注意:该方法仅对自定义样式通知有效,相关详情请参考https://help.aliyun.com/document_detail/30066.html?spm=5176.product30047.6.620.wjcC87#h3-3-4-basiccustompushnotification-api
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     * @param openType
     * @param openActivity
     * @param openUrl
     */
    override fun onNotificationReceivedInApp(
        context: Context,
        title: String,
        summary: String,
        extraMap: Map<String, String>,
        openType: Int,
        openActivity: String,
        openUrl: String,
    ) {
        Log.i(REC_TAG,
            "onNotificationReceivedInApp ：  : $title : $summary  $extraMap : $openType : $openActivity : $openUrl")
    }

    /**
     * 推送消息的回调方法
     *
     * @param context
     * @param cPushMessage
     */
    public override fun onMessage(context: Context, cPushMessage: CPushMessage) {
        Log.i(REC_TAG,
            "onMessage 收到一条推送消息 ： " + cPushMessage.title + ", content:" + cPushMessage.content)
        buildNotification(context, cPushMessage)
    }

    /**
     * 从通知栏打开通知的扩展处理
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    public override fun onNotificationOpened(
        context: Context,
        title: String,
        summary: String,
        extraMap: String,
    ) {
        Log.i(REC_TAG, "onNotificationOpened ：  : $title : $summary : $extraMap")
    }

    /**
     * 通知删除回调
     *
     * @param context
     * @param messageId
     */
    public override fun onNotificationRemoved(context: Context, messageId: String) {
        Log.i(REC_TAG, "onNotificationRemoved ： $messageId")
    }

    /**
     * 无动作通知点击回调。当在后台或阿里云控制台指定的通知动作为无逻辑跳转时,通知点击回调为onNotificationClickedWithNoAction而不是onNotificationOpened
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    override fun onNotificationClickedWithNoAction(
        context: Context,
        title: String,
        summary: String,
        extraMap: String,
    ) {
        Log.i(REC_TAG, "onNotificationClickedWithNoAction ：  : $title : $summary : $extraMap")
    }

    companion object {
        // 消息接收部分的LOG_TAG
        const val REC_TAG = "MyMessageReceiver"
    }
}