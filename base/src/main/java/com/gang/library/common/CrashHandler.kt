package com.gang.library.common

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Looper
import android.util.Log
import android.widget.Toast
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 全局异常捕获
 */
class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {
    /**
     * 系统默认UncaughtExceptionHandler
     */
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null
    /**
     * context
     */
    private var mContext: Context? = null
    /**
     * 存储异常和参数信息
     */
    private val paramsMap: MutableMap<String, String> =
        HashMap()
    /**
     * 格式化时间
     */
    private val format =
        SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
    private val TAG = "TEST" //this.getClass().getSimpleName();
    fun init(context: Context?) {
        mContext = context
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        //设置该CrashHandler为系统默认的
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * uncaughtException 回调函数
     */
    override fun uncaughtException(
        thread: Thread,
        ex: Throwable
    ) {
        Log.e(TAG, "uncaughtException...", ex)
        if (!handleException(ex) && mDefaultHandler != null) { //如果自己没处理交给系统处理
            mDefaultHandler!!.uncaughtException(thread, ex)
        } else { //自己处理
            try { //延迟3秒杀进程
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
                Log.e(TAG, "error : ", e)
            }
            //退出程序
            AppManager.appManager?.AppExit(mContext)
        }
    }

    /**
     * 收集错误信息.发送到服务器
     *
     * @return 处理了该异常返回true, 否则false
     */
    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) {
            return false
        }
        //收集设备参数信息
        collectDeviceInfo(mContext)
        //添加自定义信息
        addCustomInfo()
        //使用Toast来显示异常信息
        object : Thread() {
            override fun run() {
                Looper.prepare()
                Toast.makeText(mContext, "程序开小差了呢..", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }
        }.start()
        //保存日志文件
//        saveCrashInfo2File(ex);
        return true
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    fun collectDeviceInfo(ctx: Context?) { //获取versionName,versionCode
        try {
            val pm = ctx!!.packageManager
            val pi =
                pm.getPackageInfo(ctx.packageName, PackageManager.GET_ACTIVITIES)
            if (pi != null) {
                val versionName =
                    if (pi.versionName == null) "null" else pi.versionName
                val versionCode = pi.versionCode.toString() + ""
                paramsMap["versionName"] = versionName
                paramsMap["versionCode"] = versionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "an error occured when collect package info", e)
        }
        //获取所有系统信息
        val fields = Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                paramsMap[field.name] = field[null].toString()
            } catch (e: Exception) {
                Log.e(TAG, "an error occured when collect crash info", e)
            }
        }
    }

    /**
     * 添加自定义参数
     */
    private fun addCustomInfo() {}

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private fun saveCrashInfo2File(ex: Throwable): String? {
        val sb = StringBuffer()
        for ((key, value) in paramsMap) {
            sb.append("$key=$value\n")
        }
        val writer: Writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        sb.append(result)
        try {
            val timestamp = System.currentTimeMillis()
            val time = format.format(Date())
            val fileName = "crash-$time-$timestamp.log"
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                val path =
                    Environment.getExternalStorageDirectory().absolutePath + "/crash/"
                Log.i("TEST", path)
                val dir = File(path)
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                val fos = FileOutputStream(path + fileName)
                fos.write(sb.toString().toByteArray())
                fos.close()
            }
            return fileName
        } catch (e: Exception) {
            Log.e(TAG, "an error occured while writing file...", e)
        }
        return null
    }

    companion object {
        private var mInstance: CrashHandler? = null
        /**
         * 获取CrashHandler实例
         */
        @get:Synchronized
        val instance: CrashHandler?
            get() {
                if (null == mInstance) {
                    mInstance = CrashHandler()
                }
                return mInstance
            }
    }
}