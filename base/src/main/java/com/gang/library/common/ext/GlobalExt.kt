package com.gang.library.common.ext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

/**
 * 全局扩展
 *
 * Created on 2021/11/16.
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
 * 获取一个纯色、可渐变、边框、虚线边框、选择器 [GradientDrawable]
 *
 * 1，设置颜色
 * （1）单独指定 [color] 则为纯色
 * （2）指定 [color]（开始色）和 [endColor]（结束色）颜色不同时，则为渐变色
 * （3）单独指定 [colorStateList] 则为颜色选择器
 *
 * 2，设置边框：当 [strokeWidth] (px) 大于0时，在设置颜色的情况下都可以指定边框
 * （1）[strokeColor] 则为纯色边框
 * （2）[strokeColorStateList] 则为颜色选择器边框
 *
 * 3，设置虚线边框：在2中指定边框时，可以通过 [dashWidth] (px) 和 [dashGap] (px) 设定
 *
 * 4，设置圆角，[cornerRadius] (px) 圆角半径，并指定圆角方位 [direction] 默认全方位四角
 *
 * 5，指定渐变色时可以指定渐变方向 [orientation] 默认从上到下 [GradientDrawable.Orientation.TOP_BOTTOM]
 */
fun getGradientDrawable(
    @ColorInt color: Int = Color.TRANSPARENT,
    @ColorInt endColor: Int? = null,
    @ColorInt strokeColor: Int = Color.TRANSPARENT,
    colorStateList: ColorStateList? = null,
    strokeColorStateList: ColorStateList? = null,
    strokeWidth: Int = 0,
    dashWidth: Float = 0F,
    dashGap: Float = 0F,
    cornerRadius: Float = 0F,
    direction: Int = Direction.ALL,
    orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.TOP_BOTTOM,
): GradientDrawable {
    return GradientDrawable().apply {
        setOrientation(orientation)
        when {
            colorStateList != null -> {
                this.color = colorStateList
            }
            endColor != null -> {
                this.colors = intArrayOf(color, endColor)
            }
            else -> {
                this.colors = intArrayOf(color, color)
            }
        }

        if (strokeWidth > 0) {
            if (strokeColorStateList != null) {
                this.setStroke(strokeWidth, strokeColorStateList, dashWidth, dashGap)
            } else {
                this.setStroke(strokeWidth, strokeColor, dashWidth, dashGap)
            }
        }

        if (cornerRadius > 0) {
            setCorner(cornerRadius, direction)
        }
    }
}
