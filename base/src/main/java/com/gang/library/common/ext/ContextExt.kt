package com.gang.library.common.ext

import android.Manifest
import android.app.ActivityManager
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.RequiresPermission
import com.gang.library.BaseApp
import org.jetbrains.anko.activityManager
import org.jetbrains.anko.clipboardManager
import org.jetbrains.anko.telephonyManager
import java.io.Closeable
import java.io.InputStream
import java.util.*

/**
 * Context相关扩展:
 *
 * Created on 2020/6/5.
 *
 * @author o.s
 */


/**
 * 获取一个纯色、可渐变、边框、虚线边框、选择器 [GradientDrawable]
 *
 * 1，设置颜色
 * （1）单独指定 [colorRes]/[color] 则为纯色
 * （2）指定 [colorRes]/[color]（开始色）和 [endColorRes]/[endColor]（结束色）颜色不同时，则为渐变色
 * （3）单独指定 [colorStateList] 则为颜色选择器
 *
 * 2，设置边框：当 [strokeWidth] (px) 大于0时，在设置颜色的情况下都可以指定边框
 * （1）[strokeColorRes] 则为纯色边框
 * （2）[strokeColorStateList] 则为颜色选择器边框
 *
 * 3，设置虚线边框：在2中指定边框时，可以通过 [dashWidth] (px) 和 [dashGap] (px) 设定
 *
 * 4，设置圆角，[cornerRadius] (px) 圆角半径，并指定圆角方位 [direction] 默认全方位四角
 *
 * 5，指定渐变色时可以指定渐变方向 [orientation] 默认从上到下 [GradientDrawable.Orientation.TOP_BOTTOM]
 */
fun Context.getShapeDrawable(
    @ColorRes colorRes: Int = android.R.color.transparent,
    @ColorRes endColorRes: Int? = null,
    @ColorRes strokeColorRes: Int = android.R.color.transparent,
    @ColorInt color: Int? = null,
    @ColorInt endColor: Int? = null,
    @ColorInt strokeColor: Int? = null,
    colorStateList: ColorStateList? = null,
    strokeColorStateList: ColorStateList? = null,
    strokeWidth: Int = 0,
    dashWidth: Float = 0F,
    dashGap: Float = 0F,
    cornerRadius: Float = 0F,
    direction: Int = Direction.ALL,
    orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.TOP_BOTTOM,
): GradientDrawable {
    return getGradientDrawable(
        color = color ?: getColor(colorRes),
        endColor = endColor ?: endColorRes?.let { getColor(it) },
        strokeColor = strokeColor ?: getColor(strokeColorRes),
        colorStateList = colorStateList,
        strokeColorStateList = strokeColorStateList,
        strokeWidth = strokeWidth,
        dashWidth = dashWidth,
        dashGap = dashGap,
        cornerRadius = cornerRadius,
        direction = direction,
        orientation = orientation
    )
}

/**
 * 启动app
 */
fun Context.openApp(pkgName: String) {
    packageManager.getLeanbackLaunchIntentForPackage(pkgName)?.run {
        startActivity(this)
    }
}

/**
 * 使用浏览器（外部）访问指定的url页面
 */
fun Context.openBrowser(url: String) {
    try {//url有可能不是http或https开头的网址
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        ).run {
            startActivity(this)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

/**
 * app设置页
 */
fun Context.openAppSetting(pkgName: String = packageName) {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", pkgName, null)
    ).run {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(this)
    }
}

/**
 * app通知设置页
 */
fun Context.gotoPushSet() {
    val intent = Intent()
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
            // android 8.0引导
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("android.provider.extra.APP_PACKAGE", packageName)
        }
        else -> {
            // android 5.0-7.0
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("app_package", packageName)
            intent.putExtra("app_uid", applicationInfo.uid)
        }
    }
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}

/**
 * 启动一个Activity
 */
fun Context.startActivity(clazz: Class<*>, bundle: Bundle? = null) {
    val intent = Intent(this, clazz)
    bundle?.let {
        intent.putExtras(it)
    }
    startActivity(intent)
}

/**
 * 卸载apk
 */
fun Context.uninstallApk(pkgName: String) {
    Intent(
        Intent.ACTION_DELETE
    ).run {
        data = Uri.parse("package:$pkgName")
        startActivity(this)
    }
}

/**
 * 判断某apk是否安装
 */
fun Context.isApkInstalled(pkgName: String?): Boolean {
    if (pkgName?.isNotEmpty() == true) {
        val list = packageManager.getInstalledPackages(0)
        list.forEach {
            if (pkgName == it.packageName) {
                return true
            }
        }
    }
    return false
}

/**
 * 发送邮件
 */
fun Context.sendEmail(email: String, subject: String?, text: String?) {
    Intent(
        Intent.ACTION_SENDTO,
        Uri.parse("mailto:$email")
    ).run {
        subject?.let { putExtra(Intent.EXTRA_SUBJECT, subject) }
        text?.let { putExtra(Intent.EXTRA_TEXT, text) }
        startActivity(this)
    }
}

/**
 * 拷贝文本到粘贴板
 */
fun Context.copyToClipboard(text: String, label: String = "KTX") {
    val clipData = ClipData.newPlainText(label, text)
    clipboardManager?.setPrimaryClip(clipData)
}

/**
 * 获取粘贴板上的数据
 */
fun Context.getTextFromClipboard(): String {
    return clipboardManager?.primaryClip?.run {
        if (itemCount > 0) {
            getItemAt(0)?.coerceToText(this@getTextFromClipboard).toString()
        } else {
            ""
        }
    } ?: ""
}

/**
 * 打开 Assets 中的 Properties 配置文件
 */
fun Context.openAssets(name: String): Properties? {
    var ins: InputStream? = null
    return try {
        ins = assets.open(name)
        val p = Properties()
        p.load(ins)
        p
    } catch (e: Throwable) {
        null
    } finally {
        ins.safeClose()
    }
}

/**
 * 通用安全关闭
 */
fun Closeable?.safeClose() {
    this?.apply {
        try {
            close()
        } catch (e: Throwable) {
        } finally {
        }
    }
}

/**
 * 获取当前device网络状态
 */
val Context.internetAvailable: Boolean
    get() = (getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.run {
        getNetworkCapabilities(activeNetwork)?.run {
            when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } ?: false
    } ?: false

/**
 * 获取网络类型
 */
@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
fun Context.getNetworkType(): Int {
    return when (telephonyManager?.networkType) {
        TelephonyManager.NETWORK_TYPE_NR -> 5 // 5G
        TelephonyManager.NETWORK_TYPE_LTE -> 4 // 4G
        TelephonyManager.NETWORK_TYPE_IWLAN -> 1 // WIFI
        TelephonyManager.NETWORK_TYPE_UMTS,
        TelephonyManager.NETWORK_TYPE_EVDO_0,
        TelephonyManager.NETWORK_TYPE_EVDO_A,
        TelephonyManager.NETWORK_TYPE_HSDPA,
        TelephonyManager.NETWORK_TYPE_HSUPA,
        TelephonyManager.NETWORK_TYPE_HSPA,
        TelephonyManager.NETWORK_TYPE_EVDO_B,
        TelephonyManager.NETWORK_TYPE_EHRPD,
        TelephonyManager.NETWORK_TYPE_HSPAP,
        TelephonyManager.NETWORK_TYPE_TD_SCDMA,
        -> 3 // 3G
        TelephonyManager.NETWORK_TYPE_GPRS,
        TelephonyManager.NETWORK_TYPE_EDGE,
        TelephonyManager.NETWORK_TYPE_CDMA,
        TelephonyManager.NETWORK_TYPE_GSM, // 移动2G制式
        TelephonyManager.NETWORK_TYPE_1xRTT,
        TelephonyManager.NETWORK_TYPE_IDEN,
        -> 2 // 2G
        TelephonyManager.NETWORK_TYPE_UNKNOWN -> 0 // UNKNOWN
        else -> 0 // UNKNOWN
    }
}

/**
 * 可用内存（M）
 */
val Context.availMemory: Int
    get() = activityManager.run {
        val info = ActivityManager.MemoryInfo()
        getMemoryInfo(info)
        (info.availMem / (1024 * 1024)).toInt()
    } ?: 0


/**
 * 获取当前device网络状态
 */
fun Context.isInternetAvailable(): Boolean {
    var result = false
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    cm?.getNetworkCapabilities(cm.activeNetwork)?.run {
        result = when {
            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
    return result
}

/**
 * 获取当前deviceId手机唯一标识
 */
fun Context.getDeviceId(): String {
    //IMEI（imei）
    val tm = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val imei = tm.deviceId
    return imei
}

fun getDeviceToken(): String {
    var androidId = Settings.System.getString(BaseApp.appContext.contentResolver,
        Settings.Secure.ANDROID_ID)
    if (TextUtils.isEmpty(androidId)) {
        androidId = Settings.Secure.getString(BaseApp.appContext.contentResolver,
            Settings.Secure.ANDROID_ID)
    }
    return if (Build.VERSION.SDK_INT >= 29) androidId else androidId + Build.SERIAL

}
