package com.gang.library

import android.content.Context
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import androidx.multidex.MultiDexApplication
import com.gang.library.common.user.Config
import com.gang.tools.kotlin.utils.initToolsUtils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.tencent.smtt.sdk.QbSdk


open class BaseApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    open fun init() {
        // 初始化实用工具类
        initToolsUtils(this)

    }

    //解决NetworkOnMainThreadException异常
    fun struct() {
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

    fun initLogger(isShowLog: Boolean = false) {
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return isShowLog
            }
        })
    }

    fun preinitX5WebCore() {
        //预加载x5内核
        if (!QbSdk.isTbsCoreInited()) {
            QbSdk.preInit(applicationContext, null) // 设置X5初始化完成的回调接口
        }
    }

    fun initX5Qb() {
        if (Config.isOpenTBSX5QbSdk) {
            val cb = object : QbSdk.PreInitCallback {
                override fun onViewInitFinished(arg0: Boolean) {
                    //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                    Log.e("PreInitCallback", " onViewInitFinished is $arg0")
                }

                override fun onCoreInitFinished() {}
            }
            //x5内核初始化接口
            QbSdk.initX5Environment(applicationContext, cb)
        }
    }

    companion object {
        const val TAG = "BaseApp"

        /**
         * 获取系统上下文
         */
        lateinit var appContext: Context


    }
}