package com.gang.library.common.utils.notch

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import com.apkfuns.logutils.LogUtils
import com.gang.library.BaseApplication.Companion.appContext
import com.gang.library.common.utils.notch.callback.NotchCallback

/**
 * 判断是否是允许全屏界面内容显示到刘海区域的刘海屏机型：
 */
object CutoutUtil {
    private var sAllowDisplayToCutout: Boolean? = null
    /**
     * 是否为允许全屏界面显示内容到刘海区域的刘海屏机型（与AndroidManifest中配置对应）
     */
    fun allowDisplayToCutout(): Boolean {
        return if (sAllowDisplayToCutout == null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) { // 9.0系统全屏界面默认会保留黑边，不允许显示内容到刘海区域
                return false.also { sAllowDisplayToCutout = it }
            }
            val context = appContext
            if (hasCutout_Huawei(context)) {
                return true.also { sAllowDisplayToCutout = it }
            }
            if (hasCutout_OPPO(context)) {
                return true.also { sAllowDisplayToCutout = it }
            }
            if (hasCutout_VIVO(context)) {
                return true.also { sAllowDisplayToCutout = it }
            }
            if (hasCutout_XIAOMI(context)) {
                true.also { sAllowDisplayToCutout = it }
            } else false.also { sAllowDisplayToCutout = it }
        } else {
            sAllowDisplayToCutout!!
        }
    }

    /**
     * 是否是华为刘海屏机型
     */
    private fun hasCutout_Huawei(context: Context): Boolean {
        return if (!Build.MANUFACTURER.equals("HUAWEI", ignoreCase = true)) {
            false
        } else try {
            val cl = context.classLoader
            val HwNotchSizeUtil =
                cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            if (HwNotchSizeUtil != null) {
                val get = HwNotchSizeUtil.getMethod("hasNotchInScreen")
                return get.invoke(HwNotchSizeUtil) as Boolean
            }
            false
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 是否是oppo刘海屏机型
     */
    private fun hasCutout_OPPO(context: Context): Boolean {
        return if (!Build.MANUFACTURER.equals("oppo", ignoreCase = true)) {
            false
        } else context.packageManager
            .hasSystemFeature("com.oppo.feature.screen.heteromorphism")
    }

    /**
     * 是否是vivo刘海屏机型
     */
    private fun hasCutout_VIVO(context: Context): Boolean {
        return if (!Build.MANUFACTURER.equals("vivo", ignoreCase = true)) {
            false
        } else try {
            val cl = context.classLoader
            @SuppressLint("PrivateApi") val ftFeatureUtil =
                cl.loadClass("android.util.FtFeature")
            if (ftFeatureUtil != null) {
                val get =
                    ftFeatureUtil.getMethod("isFeatureSupport", Int::class.javaPrimitiveType)
                return get.invoke(ftFeatureUtil, 0x00000020) as Boolean
            }
            false
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 是否是小米刘海屏机型
     */
    private fun hasCutout_XIAOMI(context: Context): Boolean {
        return if (!Build.MANUFACTURER.equals("xiaomi", ignoreCase = true)) {
            false
        } else try {
            val cl = context.classLoader
            @SuppressLint("PrivateApi") val SystemProperties =
                cl.loadClass("android.os.SystemProperties")
            val paramTypes = arrayOfNulls<Class<*>?>(2)
            paramTypes[0] = String::class.java
            paramTypes[1] = Int::class.javaPrimitiveType
            val getInt = SystemProperties.getMethod("getInt", *paramTypes)
            //参数
            val params = arrayOfNulls<Any>(2)
            params[0] = "ro.miui.notch"
            params[1] = 0
            getInt.invoke(SystemProperties, *params) as Int == 1
        } catch (e: Exception) {
            false
        }
    }

    // Google刘海屏适配 >= 28才能调用到
    @TargetApi(28)
    fun getNotchParams(activity: Activity, callback: NotchCallback) {
        val decorView = activity.window.decorView
        decorView.post(Runnable {
            var isNotch = false
            val rootWindowInsets = decorView.rootWindowInsets
            if (rootWindowInsets == null) {
                LogUtils.e("TAG", "rootWindowInsets为空了")
                callback.Notch(isNotch)
                return@Runnable
            }
            val displayCutout = rootWindowInsets.displayCutout
            LogUtils.e(
                "TAG",
                "安全区域距离屏幕左边的距离 SafeInsetLeft:" + displayCutout!!.safeInsetLeft
            )
            LogUtils.e(
                "TAG",
                "安全区域距离屏幕右部的距离 SafeInsetRight:" + displayCutout.safeInsetRight
            )
            LogUtils.e(
                "TAG",
                "安全区域距离屏幕顶部的距离 SafeInsetTop:" + displayCutout.safeInsetTop
            )
            LogUtils.e(
                "TAG",
                "安全区域距离屏幕底部的距离 SafeInsetBottom:" + displayCutout.safeInsetBottom
            )
            val rects =
                displayCutout.boundingRects
            isNotch = if (rects == null || rects.size == 0) {
                LogUtils.e("TAG", "不是刘海屏")
                false
            } else {
                LogUtils.e("TAG", "刘海屏数量:" + rects.size)
                for (rect in rects) {
                    LogUtils.e("TAG", "刘海屏区域：$rect")
                }
                true
            }
            callback.Notch(isNotch)
        })
    }
}