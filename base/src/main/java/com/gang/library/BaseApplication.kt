package com.gang.library

import android.app.Application
import android.content.Context
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import com.apkfuns.logutils.LogUtils
import com.gang.library.common.user.Config
import com.lzy.okgo.OkGo
import com.tencent.smtt.sdk.QbSdk
import com.zhy.http.okhttp.OkHttpUtils


open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        init()
    }

    private fun init() { //
        // 初始化Logger 是否开启日志
        LogUtils.getLogConfig()
            .configAllowLog(Config.isShowLog)
            .configTagPrefix(resources.getString(R.string.app_name))
            .configShowBorders(true)
            .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}")

        struct()

        val cb = object : QbSdk.PreInitCallback {
            override fun onViewInitFinished(arg0: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.e("PreInitCallback", " onViewInitFinished is $arg0")
            }

            override fun onCoreInitFinished() {}
        }
        //x5内核初始化接口
        QbSdk.initX5Environment(applicationContext, cb)


        // okhttp-utils
        OkHttpUtils.getInstance()
            .init(this)
            .debug(true, "okHttp")
            .timeout(20 * 1000)
        OkGo.getInstance().init(this)
    }

    //解决NetworkOnMainThreadException异常
    private fun struct() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork() // or
                .detectAll()
                // for
                // all
                // detectable
                // problems
                .permitAll()
                .penaltyLog().build()
        )
        StrictMode.setVmPolicy(
            VmPolicy.Builder()
                .detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
                .penaltyLog() // 打印logcat
                .penaltyDeath().build()
        )
    }

    companion object {
        private const val TAG = "BaseApplication"

        /**
         * 获取系统上下文
         */
        lateinit var appContext: Context
            private set
    }
}