package com.gang.library.common.view

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.gang.library.common.ext.Direction
import com.gang.library.common.ext.getGradientDrawable
import com.gang.library.common.ext.statelist.getColorStateList
import com.gang.tools.kotlin.utils.getColor

/**
 * View相关扩展：
 *
 * 创建者: hrg
 * 创建时间: 2020/6/8 10:35 AM
 */

/**
 * View设置选择器，指定圆角 [cornerRadius] (px)，边框 [strokeWidth] (px)
 * 外部调用时指定对应的（dp, dpF）更为直观
 */
fun View.setSelector(
    @ColorRes pressColor: Int,
    @ColorRes normalColor: Int,
    @ColorRes pressStrokeColor: Int? = null,
    @ColorRes normalStrokeColor: Int? = null,
    cornerRadius: Float = 0F,
    strokeWidth: Int = 0,
): View {
    val color =
        getColorStateList(normalColor = getColor(normalColor), pressColor = getColor(pressColor))
    var strokeColor: ColorStateList? = null
    if (normalStrokeColor != null && pressStrokeColor != null) {
        strokeColor =
            getColorStateList(normalColor = getColor(normalStrokeColor),
                pressColor = getColor(pressStrokeColor))
    } else if (normalStrokeColor != null) {
        strokeColor =
            getColorStateList(normalColor = getColor(normalStrokeColor),
                pressColor = getColor(pressColor))
    } else if (pressStrokeColor != null) {
        strokeColor =
            getColorStateList(normalColor = getColor(normalColor),
                pressColor = getColor(pressStrokeColor))
    }
    val gd = GradientDrawable()

    gd.color = color
    if (strokeColor != null) {
        gd.setStroke(strokeWidth, strokeColor)
    }
    if (cornerRadius > 0) {
        gd.cornerRadius = cornerRadius
    }
    background = gd

    return this
}

/**
 * 给 [View] 设置背景：纯色、可渐变、边框、虚线边框、选择器 [GradientDrawable]
 *
 * 1，设置颜色
 *  （1）单独指定 [colorRes]/[color] 则为纯色
 *  （2）指定 [colorRes]/[color]（开始色）和 [endColorRes]/[endColor]（结束色）颜色不同时，则为渐变色
 *  （3）单独指定 [colorStateList] 则为颜色选择器
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
fun View.setBackground(
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
): View {
    background = getShapeDrawable(
        colorRes = colorRes,
        endColorRes = endColorRes,
        strokeColorRes = strokeColorRes,
        color = color,
        endColor = endColor,
        strokeColor = strokeColor,
        colorStateList = colorStateList,
        strokeColorStateList = strokeColorStateList,
        strokeWidth = strokeWidth,
        dashWidth = dashWidth,
        dashGap = dashGap,
        cornerRadius = cornerRadius,
        direction = direction,
        orientation = orientation
    )
    return this
}

/**
 * 给 [View] 设置前景：纯色、可渐变、边框、虚线边框、选择器 [GradientDrawable]
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
fun View.setForeground(
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
): View {
    foreground = getShapeDrawable(
        colorRes = colorRes,
        endColorRes = endColorRes,
        strokeColorRes = strokeColorRes,
        color = color,
        endColor = endColor,
        strokeColor = strokeColor,
        colorStateList = colorStateList,
        strokeColorStateList = strokeColorStateList,
        strokeWidth = strokeWidth,
        dashWidth = dashWidth,
        dashGap = dashGap,
        cornerRadius = cornerRadius,
        direction = direction,
        orientation = orientation
    )
    return this
}

/**
 * 给 [ImageView] 设置 [Drawable]：纯色、可渐变、边框、虚线边框、选择器 [GradientDrawable]
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
fun ImageView.setImageDrawable(
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
): ImageView {
    setImageDrawable(
        getShapeDrawable(
            colorRes = colorRes,
            endColorRes = endColorRes,
            strokeColorRes = strokeColorRes,
            color = color,
            endColor = endColor,
            strokeColor = strokeColor,
            colorStateList = colorStateList,
            strokeColorStateList = strokeColorStateList,
            strokeWidth = strokeWidth,
            dashWidth = dashWidth,
            dashGap = dashGap,
            cornerRadius = cornerRadius,
            direction = direction,
            orientation = orientation
        )
    )
    return this
}

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
fun View.getShapeDrawable(
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