package com.gang.library.common.ext.span

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.*
import android.text.style.*
import android.util.Range
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.gang.tools.kotlin.dimension.dp
import com.gang.tools.kotlin.dimension.sp
import java.util.regex.Pattern


/**
 * 富文本 Span 设置
 * 可指定范围 Range，也可以不指定，则默认全部。
 *
 * Created on 2020/9/16.
 *
 * @author o.s
 */

/**
 * 图片链接默认占位字符
 */
const val LINK_IMAGE_PLACEHOLDER = "§"

fun CharSequence?.toSpan(): SpannableStringBuilder {
    return SpannableStringBuilder(this ?: "").apply {

    }
}

fun SpannableStringBuilder?.toBold(
    vararg range: Range<Int>,
): SpannableStringBuilder? {
    this?.apply {
        val styleSpan = StyleSpan(Typeface.BOLD)
        if (range.isEmpty()) {
            setSpan(styleSpan, 0, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        } else {
            range.forEach {
                if (it.upper <= length) {
                    setSpan(styleSpan, it.lower, it.upper, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                }
            }
        }
    }
    return this
}

fun SpannableStringBuilder?.toUnderLine(
    vararg range: Range<Int>,
): SpannableStringBuilder? {
    this?.apply {
        val underLineSpan = UnderlineSpan()
        if (range.isEmpty()) {
            setSpan(underLineSpan, 0, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        } else {
            range.forEach {
                if (it.upper <= length) {
                    setSpan(underLineSpan, it.lower, it.upper, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                }
            }
        }
    }
    return this
}

fun SpannableStringBuilder?.toColor(
    vararg range: Range<Int>,
    @ColorInt color: Int,
): SpannableStringBuilder? {
    this?.apply {
        val colorSpan = ForegroundColorSpan(color)
        if (range.isEmpty()) {
            setSpan(colorSpan, 0, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        } else {
            range.forEach {
                if (it.upper <= length) {
                    setSpan(colorSpan, it.lower, it.upper, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                }
            }
        }
    }
    return this
}

/**
 * 设置超链接时，注意配置TextView、EditText属性：movementMethod = LinkMovementMethod.getInstance()
 */
fun SpannableStringBuilder?.toLink(
    vararg range: Range<Int>,
    action: ((view: View, key: String) -> Unit)? = null,
): SpannableStringBuilder? {
    this?.apply {
        if (range.isEmpty()) {
            val subStr = this.toString()
            val clickSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    action?.invoke(widget, subStr)
                }
            }
            setSpan(clickSpan, 0, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        } else {
            range.forEach {
                if (it.upper <= length) {
                    val subStr = substring(it.lower, it.upper)
                    val clickSpan = object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            action?.invoke(widget, subStr)
                        }
                    }
                    setSpan(clickSpan, it.lower, it.upper, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                }
            }
        }
    }
    return this
}

/**
 * (不带下划线)设置超链接时，注意配置TextView、EditText属性：movementMethod = LinkMovementMethod.getInstance()
 */
fun SpannableStringBuilder?.toLinkNotUnderLine(
    vararg range: Range<Int>,
    action: ((view: View, key: String) -> Unit)? = null,
    drawstate: ((ds: TextPaint) -> Unit)? = null,
): SpannableStringBuilder? {
    this?.apply {
        if (range.isEmpty()) {
            val subStr = this.toString()
            val clickSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    action?.invoke(widget, subStr)
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                    drawstate?.invoke(ds)
                }
            }
            setSpan(clickSpan, 0, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        } else {
            range.forEach {
                if (it.upper <= length) {
                    val subStr = substring(it.lower, it.upper)
                    val clickSpan = object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            action?.invoke(widget, subStr)
                        }

                        override fun updateDrawState(ds: TextPaint) {
                            ds.isUnderlineText = false
                            drawstate?.invoke(ds)
                        }
                    }
                    setSpan(clickSpan, it.lower, it.upper, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                }
            }
        }
    }
    return this
}

fun SpannableStringBuilder?.toUrl(
    url: String,
    action: ((url: String) -> Unit)? = null,
): SpannableStringBuilder? {
    this?.apply {
        val urlSpan = object : URLSpan(url) {
            override fun onClick(widget: View) {
                action?.invoke(url)
            }
        }
        setSpan(urlSpan, 0, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    }
    return this
}

fun Drawable?.toImageSpan(
    width: Int = 18.dp,
    height: Int = 18.dp,
): SpannableStringBuilder {
    val span = SpannableStringBuilder(LINK_IMAGE_PLACEHOLDER)
    this?.apply {
        setBounds(0, 0, width, height)
        val imageSpan = ImageSpan(this, ImageSpan.ALIGN_BASELINE)
        span.setSpan(imageSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    }
    return span
}

fun SpannableStringBuilder?.toTextSizeSpan(
    textSize: Int = 14.sp,
    vararg range: Range<Int>,
): SpannableStringBuilder? {
    this?.apply {
        //绝对值
        val textSpan = AbsoluteSizeSpan(textSize, true)
        if (range.isNotEmpty()) {
            range.forEach {
                if (it.upper <= length) {
                    setSpan(textSpan, it.lower, it.upper, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                }
            }
        } else {
            setSpan(textSpan, 0, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }
    }
    return this
}

fun String.getNumberTextSpan(
    @ColorInt color: Int,
    reverse: Boolean = false,
): SpannableStringBuilder {
    val span = SpannableStringBuilder(this)
    val list = getNumberRegion(reverse)
    list.forEach {
        span.setSpan(
            ForegroundColorSpan(color),
            it.lower,
            it.upper,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
    }
    return span
}

fun String.getNumberRegion(reverse: Boolean = false): List<Range<Int>> {
    val list = ArrayList<Range<Int>>()
    val pattern = Pattern.compile("\\d+")
    val matcher = pattern.matcher(this)
    val len = length

    if (reverse) {
        // 反向查找数字范围，记录除去数字的其他文本片段范围，默认从（0 至 len）
        // 例如："aaa111bbb"，start = 3, end = 6, len = 9, [Region(0, 3), Region(6, 9)]
        var start = 0
        while (matcher.find()) {
            list.add(Range(start, matcher.start()))
            start = matcher.end()
        }
        list.add(Range(start, len))
    } else {
        // 查找数字找到它的范围区间
        while (matcher.find()) {
            list.add(Range(matcher.start(), matcher.end()))
        }
    }

    return list
}

/**
 * 匹配关键词的内容设置颜色
 */
fun convertToHtml(title: String, color: String, keyword: String): Spanned? {
    val htmlKey = "<font color=\"${color}\">${keyword}</font>"
    return Html.fromHtml(title.replace(keyword, htmlKey, false), Html.FROM_HTML_MODE_LEGACY)
}

/**
 * 处理数字字体
 */
fun String.getNumberColorSizeBoldSpan(
    @ColorInt color: Int,
    textSize: Int = 13.sp,
    textStyle: Int = Typeface.NORMAL,
): SpannableStringBuilder {
    val textSpan = AbsoluteSizeSpan(textSize, false)
    val styleSpan = StyleSpan(textStyle)
    val span = SpannableStringBuilder(this)
    val list = getNumberRegion()
    list.forEach {
        span.setSpan(
            ForegroundColorSpan(color),
            it.lower,
            it.upper,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        span.setSpan(textSpan, it.lower, it.upper, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        span.setSpan(styleSpan, it.lower, it.upper, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    }
    return span
}

fun String.addStartImage(context: Context, @DrawableRes res: Int): SpannableStringBuilder {
    val str = "img $this"
    val ssb = SpannableStringBuilder(str)
    val imageSpan = ImageSpan(
        context, res, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ImageSpan.ALIGN_CENTER
        } else {
            ImageSpan.ALIGN_BASELINE
        }
    )
    ssb.setSpan(imageSpan, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return ssb
}