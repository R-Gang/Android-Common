package com.gang.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import anet.channel.util.Utils
import com.alibaba.sdk.android.push.CommonCallback
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory
import com.gang.app.common.user.Configs
import com.gang.library.BaseApplication
import com.gang.library.common.user.Config
import com.lzy.okgo.OkGo
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator
import com.zhy.http.okhttp.OkHttpUtils


class MyApplication : BaseApplication() {

    private val TAG = "AlibabaPush"

    override fun onCreate() {
        // 是否显示日志
        Config.isShowLog = true

        // 阿里云配置参数
        Config.accessKeyId = ""
        Config.accessKeySecret = ""
        val ossBucket = ""
        Config.ENDPOINT = "http://oss-cn-beijing.aliyuncs.com/"
        Config.OSS_URL = "https://$ossBucket.oss-cn-beijing.aliyuncs.com/"

        super.onCreate()

        init()
    }

    private fun init() {

        struct()

        // 版本更新
        if (Configs.isOpenVersionUpdate) {
            // okhttp-utils
            OkHttpUtils.getInstance()
                .init(this)
                .debug(true, "okHttp")
                .timeout(20 * 1000)
            OkGo.getInstance().init(this)
        }

        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.cBCBCBC, R.color.white)//全局设置主题颜色
            ClassicsHeader(context).setDrawableSize(20f)//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(object : DefaultRefreshFooterCreator {
            override fun createRefreshFooter(
                context: Context,
                layout: RefreshLayout,
            ): RefreshFooter {
                //指定为经典Footer，默认是 BallPulseFooter
                return ClassicsFooter(context).setDrawableSize(20f)
            }
        })

        /**
         * 为了兼容5.0以下使用vector图标
         */
//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
//        }

        // 阿里云推送
        if (Configs.isOpenAlibabaPush) {
            initCloudChannel(this)
        }
    }

    /**
     * 初始化云推送通道
     *
     * @param applicationContext
     */
    private fun initCloudChannel(applicationContext: Context) {
        // 创建notificaiton channel
        createNotificationChannel()
        PushServiceFactory.init(applicationContext)
        val pushService = PushServiceFactory.getCloudPushService()
        pushService.register(applicationContext, object : CommonCallback {
            override fun onSuccess(response: String) {
                Log.i(TAG, "init cloudchannel success")
            }

            override fun onFailed(errorCode: String, errorMessage: String) {
                Log.e(
                    TAG,
                    "init cloudchannel failed -- errorcode:$errorCode -- errorMessage:$errorMessage"
                )
            }
        })
    }


    companion object {

        fun createNotificationChannel(): NotificationManager {
            val mNotificationManager =
                Utils.getAppContext().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // 通知渠道的id
                val id: String = Utils.getAppContext().getPackageName()
                // 用户可以看到的通知渠道的名字.
                val name: CharSequence = Utils.getAppContext().getString(R.string.app_name)
                // 用户可以看到的通知渠道的描述
                val description = "notification description"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel(id, name, importance)
                // 配置通知渠道的属性
                mChannel.description = description
                // 设置通知出现时的闪灯（如果 android 设备支持的话）
                mChannel.enableLights(true)
                mChannel.lightColor = Color.RED
                // 设置通知出现时的震动（如果 android 设备支持的话）
                mChannel.enableVibration(true)
                mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                //最后在notificationmanager中创建该通知渠道
                mNotificationManager.createNotificationChannel(mChannel)
            }
            return mNotificationManager
        }

    }
}