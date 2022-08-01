package com.library.kotlin.dimension

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import com.gang.library.BaseApplication
import com.gang.library.common.utils.LogUtils
import com.gang.library.common.utils.initAndroidCommon
import java.lang.reflect.Method

/**
 * Dimension 相关扩展
 *
 * Created on 2021/3/5.
 *
 * @author o.s
 */


/**
 * 获取屏幕的密度
 */
inline val densityDpi
    get() = Resources.getSystem().displayMetrics.densityDpi

inline val density
    get() = Resources.getSystem().displayMetrics.density

/**
 * 屏幕宽高
 */
inline val screenWidth
    get() = Resources.getSystem().displayMetrics.widthPixels

inline val screenHeight
    get() = Resources.getSystem().displayMetrics.heightPixels

/**
 * 获得屏幕宽高
 * ======
 * 不包含虚拟按键
 * @return
 */
var screenArray = IntArray(2)
    get() {
        val outMetrics = initAndroidCommon().resources.displayMetrics
        val iArray = IntArray(2)
        iArray[0] = outMetrics.widthPixels
        iArray[1] = outMetrics.heightPixels
        return iArray
    }

/**
 * 获取屏幕原始尺寸宽高度
 * ======
 * 包括虚拟功能键高度
 */
var screenDpiArray = IntArray(2)
    get() {
        val iArray = IntArray(2)
        val windowManager =
            initAndroidCommon().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        val c: Class<*>
        try {
            c = Class.forName("android.view.Display")
            val method: Method = c.getMethod("getRealMetrics", DisplayMetrics::class.java)
            method.invoke(display, displayMetrics)
            val iArray = IntArray(2)
            iArray[0] = displayMetrics.widthPixels
            iArray[1] = displayMetrics.heightPixels
            return iArray
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return iArray
    }

/**
 * 状态栏宽高
 */
inline val statusBarWidth
    get() = Resources.getSystem().getIdentifier("status_bar_width", "dimen", "android").let {
        Resources.getSystem().getDimensionPixelSize(it)
    }

inline val statusBarHeight
    get() = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android").let {
        var height = Resources.getSystem().getDimensionPixelSize(it)
        if (height <= 0) {
            height = try {
                val clazz = Class.forName("com.android.internal.R\$dimen")
                val obj = clazz.newInstance()
                val h = clazz.getField("status_bar_height")[obj].toString().toInt()
                Resources.getSystem().getDimensionPixelSize(h)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                0
            }
        }
        height
    }

/**
 * 获取虚拟导航栏(NavigationBar)的高度，可能未显示
 */
inline val navigationBarHeight
    get() = Resources.getSystem().getIdentifier("navigation_bar_height", "dimen", "android").let {
        if (it > 0) {
            LogUtils.d(BaseApplication.TAG, "--显示虚拟导航了--")
            Resources.getSystem().getDimensionPixelSize(it)
        } else {
            LogUtils.d(BaseApplication.TAG, "--没有虚拟导航 或者虚拟导航隐藏--")
            0
        }
    }

/**
 * 判断当前虚拟按键是否显示
 * ===
 * 大部分手机来说，基本上是可以实现需求
 * @return true(显示虚拟导航栏)，false(不显示或不支持虚拟导航栏)
 */
fun showNavigationBar(): Boolean {
    var hasNavigationBar = false
    val rs: Resources = initAndroidCommon().resources
    val id: Int = rs.getIdentifier("config_showNavigationBar", "bool", "android")
    if (id > 0) {
        hasNavigationBar = rs.getBoolean(id)
    }
    return hasNavigationBar
}

/**
 * 判断虚拟导航栏是否显示
 *
 * @return true(显示虚拟导航栏)，false(不显示或不支持虚拟导航栏)
 */
fun checkNavigationBarShow(): Boolean {
    var hasNavigationBar = showNavigationBar()
    try {
        val systemPropertiesClass = Class.forName("android.os.SystemProperties")
        val m = systemPropertiesClass.getMethod("get", String::class.java)
        val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
        //判断是否隐藏了底部虚拟导航
        var navigationBarIsMin = 0
        navigationBarIsMin = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Settings.System.getInt(initAndroidCommon().contentResolver,
                getDeviceInfo(), 0)
        } else {
            Settings.Global.getInt(initAndroidCommon().contentResolver,
                getDeviceInfo(), 0)
        }
        if ("1" == navBarOverride || 1 == navigationBarIsMin) {
            hasNavigationBar = false
        } else if ("0" == navBarOverride) {
            hasNavigationBar = true
        }
    } catch (e: java.lang.Exception) {
    }
    return hasNavigationBar
}

/**
 * 底部导航的高度
 */
fun getBottomStatusHeight(): Int {
    val totalHeight = screenDpiArray[1]
    val contentHeight = screenArray[1]
    LogUtils.d(BaseApplication.TAG, "--显示虚拟导航了--")
    return totalHeight - contentHeight
}

/**
 * 获取虚拟按键的高度
 *
 * @param context
 * @return
 */
/*@Deprecated("通过计算得出的虚拟按键高度，弃用")
inline val navigationBarHeight
    get = if (checkNavigationBarShow()) {
        getBottomStatusHeight()
    } else {
        LogUtils.d(BaseApplication.TAG, "--没有虚拟导航 或者虚拟导航隐藏--")
        0
    }*/

/**
 * 获取设备信息（目前支持几大主流的全面屏手机，亲测华为、小米、oppo、魅族、vivo、三星都可以）
 *
 * @return
 */
fun getDeviceInfo(): String {
    val brand = Build.BRAND
    if (TextUtils.isEmpty(brand)) return "navigationbar_is_min"
    return if (brand.equals("HUAWEI", ignoreCase = true) || "HONOR" == brand) {
        "navigationbar_is_min"
    } else if (brand.equals("XIAOMI", ignoreCase = true)) {
        "force_fsg_nav_bar"
    } else if (brand.equals("VIVO", ignoreCase = true)) {
        "navigation_gesture_on"
    } else if (brand.equals("OPPO", ignoreCase = true)) {
        "navigation_gesture_on"
    } else if (brand.equals("samsung", ignoreCase = true)) {
        "navigationbar_hide_bar_enabled"
    } else {
        "navigationbar_is_min"
    }
}


/**
 * 将 dp, sp, pt, in, mm 转换成 px
 */
inline val Int.dp
    get() = dpF.toInt()

inline val Int.dpF
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        toFloat(),
        Resources.getSystem().displayMetrics
    )

inline val Float.dp
    get() = dpF.toInt()

inline val Float.dpF
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

inline val Int.sp
    get() = spF.toInt()

inline val Int.spF
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        toFloat(),
        Resources.getSystem().displayMetrics
    )

inline val Float.sp
    get() = spF.toInt()

inline val Float.spF
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        toFloat(),
        Resources.getSystem().displayMetrics
    )

inline val Int.mm
    get() = mmF.toInt()

inline val Int.mmF
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_MM,
        toFloat(),
        Resources.getSystem().displayMetrics
    )

inline val Float.mm
    get() = mmF.toInt()

inline val Float.mmF
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_MM,
        this,
        Resources.getSystem().displayMetrics
    )

/**
 * 将 px 转换成 dp, sp, pt, in, mm
 */
inline val Int.toDP
    get() = toDPF.toInt()

inline val Int.toDPF
    get() = this / Resources.getSystem().displayMetrics.density

inline val Float.toDP
    get() = toDPF.toInt()

inline val Float.toDPF
    get() = this / Resources.getSystem().displayMetrics.density

inline val Int.toSP
    get() = toMMF.toInt()

inline val Int.toSPF
    get() = this / Resources.getSystem().displayMetrics.scaledDensity

inline val Float.toSP
    get() = toSPF.toInt()

inline val Float.toSPF
    get() = this / Resources.getSystem().displayMetrics.scaledDensity

inline val Int.toMM
    get() = toMMF.toInt()

inline val Int.toMMF
    get() = this / Resources.getSystem().displayMetrics.xdpi * (1.0f / 25.4f)

inline val Float.toMM
    get() = toMMF.toInt()

inline val Float.toMMF
    get() = this / Resources.getSystem().displayMetrics.xdpi * (1.0f / 25.4f)



/**
 * dp转px
 */
fun dip2px(dpValue: Float): Float {
    val scale = initAndroidCommon().resources.displayMetrics.density
    return (dpValue * scale + 0.5f)
}

/**
 * px转dp
 */
fun px2dip(pxValue: Float): Float {
    val scale = initAndroidCommon().resources.displayMetrics.density
    return (pxValue / scale + 0.5f)
}

/**
 * px转sp
 */
fun px2sp(spValue: Float): Float {
    val fontScale = initAndroidCommon().resources.displayMetrics.scaledDensity
    return (spValue / fontScale + 0.5f)
}

/**
 * sp转px
 */
fun sp2px(spValue: Float): Float {
    val fontScale = initAndroidCommon().resources.displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f)
}
