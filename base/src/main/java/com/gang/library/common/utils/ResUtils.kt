package com.gang.library.common.utils

import android.content.Context
import android.graphics.*
import android.view.View
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.gang.library.common.user.Config
import com.gang.library.ui.interfaces.Setter
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException

/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.app.common.utils
 * @ClassName:      ResourcesUtils
 * @Description:    获取资源
 * @Author:         haoruigang
 * @CreateDate:     2020/8/3 17:28
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/3 17:28
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
/**
 * 获取字符串
 *
 * @param id
 * @param obj
 * @return
 */
fun getString(@StringRes id: Int, obj: Array<Any?>): String {
    val string: String = initAndroidCommon().resources.getString(id)
    return if (obj.isNotEmpty()) String.format(string, *obj) else string
}

fun getStrings(@ArrayRes id: Int): Array<String> {
    return initAndroidCommon().resources.getStringArray(id)
}

/**
 * 文字中添加图片
 *
 * @param textView
 * @param imgResId
 * @param index
 * @param padding
 */
fun setTvaddDrawable(
    textView: TextView, @DrawableRes imgResId: Int,
    index: Int,
    padding: Int,
) {
    if (imgResId == -1) {
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
    } else {
        textView.setCompoundDrawablesWithIntrinsicBounds(
            if (index == 1) imgResId else 0,
            if (index == 2) imgResId else 0,
            if (index == 3) imgResId else 0,
            if (index == 4) imgResId else 0
        )
        textView.compoundDrawablePadding = padding
    }
}

/**
 * 获取assets下的文件
 */
fun getAssetFile(url: String): String {
    return "file:///android_asset/$url"
}

/**
 * 读取assets中的txt文件
 *
 * @return
 */
fun readAssetsText(context: Context, fileName: String?): String {
    val sb = StringBuffer("")
    try {
        val inputStream = context.assets.open(fileName!!)
        var inputStreamReader: InputStreamReader? = null
        try {
            inputStreamReader = InputStreamReader(inputStream, "UTF-8")
        } catch (e1: UnsupportedEncodingException) {
            LogUtils.tag("readAssetsText=$e1")
        }
        val reader = BufferedReader(inputStreamReader)
        var line: String?
        try {
            while (reader.readLine().also { line = it } != null) {
                sb.append(line)
                sb.append("\n")
            }
        } catch (e: IOException) {
            LogUtils.tag("readAssetsText=$e")
        }
    } catch (e1: IOException) {
    }
    return sb.toString()
}

/**
 * 转换图片成圆形
 *
 * @param bitmap 传入Bitmap对象
 * @return
 */
fun toRoundBitmap(bitmap: Bitmap): Bitmap {
    var width = bitmap.width
    var height = bitmap.height
    val roundPx: Float
    val left: Float
    val top: Float
    val right: Float
    val bottom: Float
    val dst_left: Float
    val dst_top: Float
    val dst_right: Float
    val dst_bottom: Float
    if (width <= height) {
        roundPx = width / 2.toFloat()
        left = 0f
        top = 0f
        right = width.toFloat()
        bottom = width.toFloat()
        height = width
        dst_left = 0f
        dst_top = 0f
        dst_right = width.toFloat()
        dst_bottom = width.toFloat()
    } else {
        roundPx = height / 2.toFloat()
        val clip = (width - height) / 2.toFloat()
        left = clip
        right = width - clip
        top = 0f
        bottom = height.toFloat()
        width = height
        dst_left = 0f
        dst_top = 0f
        dst_right = height.toFloat()
        dst_bottom = height.toFloat()
    }
    val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val paint = Paint()
    val src =
        Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    val dst = Rect(
        dst_left.toInt(),
        dst_top.toInt(),
        dst_right.toInt(),
        dst_bottom.toInt()
    )
    val rectF = RectF(dst)
    paint.isAntiAlias = true // 设置画笔无锯齿
    canvas.drawARGB(0, 0, 0, 0) // 填充整个Canvas
    // 以下有两种方法画圆,drawRounRect和drawCircle
    canvas.drawRoundRect(
        rectF,
        roundPx,
        roundPx,
        paint
    ) // 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
    // canvas.drawCircle(roundPx, roundPx, roundPx, paint);
    paint.xfermode =
        PorterDuffXfermode(PorterDuff.Mode.SRC_IN) // 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
    canvas.drawBitmap(bitmap, src, dst, paint) // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle
    return output
}

//全局字体
val typefaceAll: Typeface by lazy {
    Typeface.createFromAsset(initAndroidCommon().assets,
        Config.typefaceAll)
}

fun <T : View?, V> applyV(
    list: List<T>,
    setter: Setter<in T, V>, value: V,
) {
    var i = 0
    val count = list.size
    while (i < count) {
        setter[list[i], value] = i
        i++
    }
}