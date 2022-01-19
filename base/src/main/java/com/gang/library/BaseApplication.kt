package com.gang.library

import android.content.Context
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import androidx.multidex.MultiDexApplication
import com.apkfuns.logutils.LogUtils
import com.gang.library.common.user.Config
import com.lzy.okhttputils.OkHttpUtils
import com.tencent.smtt.sdk.QbSdk
import com.uuzuche.lib_zxing.activity.ZXingLibrary
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


open class BaseApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    open fun init() { //

        // 初始化Logger 是否开启日志
        LogUtils.getLogConfig()
            .configAllowLog(Config.isShowLog)
            .configTagPrefix(resources.getString(R.string.app_name))
            .configShowBorders(true)
            .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}")

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

    fun initVersionupdate() {
        // 版本更新
        if (Config.isOpenVersionUpdate) {
            // okhttp-utils
            timeout(20 * 1000).debug("okHttp", true)
        }
    }

    fun timeout(timeout: Long): OkHttpUtils {
        OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.MILLISECONDS)
            .readTimeout(timeout, TimeUnit.MILLISECONDS)
            .writeTimeout(timeout, TimeUnit.MILLISECONDS)
        return OkHttpUtils.getInstance()
    }

    fun initDisplayOpinion() {
        // 二维码扫描
        ZXingLibrary.initDisplayOpinion(applicationContext)
    }

    companion object {
        const val TAG = "BaseApplication"

        /**
         * 获取系统上下文
         */
        lateinit var appContext: Context


    }
}