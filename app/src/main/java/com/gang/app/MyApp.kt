package com.gang.app

//TODO import com.alibaba.sdk.android.push.huawei.HuaWeiRegister
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.alibaba.sdk.android.push.CloudPushService
import com.alibaba.sdk.android.push.CommonCallback
import com.alibaba.sdk.android.push.noonesdk.PushInitConfig
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory
import com.alibaba.sdk.android.push.register.MiPushRegister
import com.gang.app.common.user.Configs
import com.gang.library.BaseApp
import com.gang.library.common.user.Config
import com.gang.recycler.kotlin.manager.LayoutManager
import com.gang.tools.kotlin.ToolsConfig
import com.gang.tools.kotlin.utils.LogUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator
import com.uuzuche.lib_zxing.activity.ZXingLibrary


class MyApp : BaseApp() {

    private val TAG = "AlibabaPush"

    override fun onCreate() {
        // 是否显示日志
        ToolsConfig.isShowLog = true

        // 是否开启全局页面管理(默认开启)
        Config.activityEnabled = true

        super.onCreate()

        init()
    }

    override fun init() {
        super.init()

        struct()
        initLogger()
        preinitX5WebCore()//预加载x5内核
        Config.isOpenTBSX5QbSdk = true
        initX5Qb()
        initDisplayOpinion()//初始化ZXing扫描

        LayoutManager.instance?.init(this) // 初始化RecyclerView

        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(com.gang.tools.R.color.cBCBCBC, R.color.white)//全局设置主题颜色
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
        val config = PushInitConfig.Builder()
            .application(this)
//            .appKey("阿里云appkey")
//            .appSecret("阿里云appSecret")
            .build()
        PushServiceFactory.init(config)
        val pushService = PushServiceFactory.getCloudPushService()
        pushService.setLogLevel(if (ToolsConfig.isShowLog) CloudPushService.LOG_DEBUG else CloudPushService.LOG_OFF);
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

        // 默认使用广播的方式接收推送，设置之后会改为使用服务接收推送
        // pushService.setPushIntentService(MyMessageIntentService::class.java)

        // 设置图标可忽略
        val drawable: Drawable? = ContextCompat.getDrawable(appContext, R.mipmap.laopo)
        if (drawable != null) {
            val bitmap = (drawable as BitmapDrawable).bitmap
            // 设置通知栏图标
            pushService.setNotificationLargeIcon(bitmap)
            // 设置状态栏图标
            pushService.setNotificationSmallIcon(R.mipmap.laopo);
            LogUtils.i(TAG, "Set notification largeIcon res id to R.mipmap.icon_app_logo")
        }

//        // 注册方法会自动判断是否支持小米系统推送，如不支持会跳过注册。
        MiPushRegister.register(appContext, "小米AppId", "小米AppKey")
//        // 注册方法会自动判断是否支持华为系统推送，如不支持会跳过注册。
        //TODO HuaWeiRegister.register(appContext as Application?)

    }

    fun createNotificationChannel(): NotificationManager {
        val mNotificationManager =
            appContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        @SuppressLint("ObsoleteSdkInt")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 通知渠道的id
            val id = "1" //默认1,可设置(1-100)
            // 用户可以看到的通知渠道的名字.
            val name: CharSequence = appContext.getString(R.string.app_name)
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

    fun initDisplayOpinion() {
        // 二维码扫描
        ZXingLibrary.initDisplayOpinion(applicationContext)
    }


    companion object {
        val instance: MyApp
            get() = SingletonHolder.INSTANCE
    }

    private object SingletonHolder {
        var INSTANCE: MyApp = MyApp()
    }

}