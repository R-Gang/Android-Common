package com.gang.library.common.ext.bind

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.PaintDrawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import com.gang.library.common.ext.Direction
import com.gang.library.common.ext.statelist.getColorStateList
import com.gang.library.common.view.setBackground
import com.gang.tools.kotlin.dimension.dp
import com.gang.tools.kotlin.dimension.dpF

/**
 * BindingAdapter 扩展
 *
 * Created on 2021/11/19.
 *
 * @author o.s
 */

/**
 * 注意：xml布局中以（dp）单位为标准
 *
 * 1，xml布局设置颜色
 *  （1）单独指定 [color] 则为纯色
 *  （2）指定 [color]（开始色）和 [endColor]（结束色）颜色不同时，则为渐变色
 *  （3）指定 [normalColor] [pressColor] ... 则为颜色选择器
 *
 * 2，xml布局设置边框：当 [strokeWidth] (dp) 大于0时，在设置颜色的情况下都可以指定边框
 * （1）[strokeColor] 则为纯色边框
 * （2）[strokeNormalColor] [strokePressColor] ... 则为颜色选择器边框
 *
 * 3，设置虚线边框：在2中指定边框时，可以通过 [dashWidth] (dp) 和 [dashGap] (dp) 设定
 *
 * 4，设置圆角，[cornerRadius] (dp) 圆角半径，并指定圆角方位 [direction] 默认全方位四角
 *
 * 5，指定渐变色时，默认从上到下 [GradientDrawable.Orientation.TOP_BOTTOM]
 */
@BindingAdapter(
    "color",
    "endColor",
    "strokeColor",

    "normalColor",
    "pressColor",
    "selectedColor",
    "activatedColor",
    "checkedColor",
    "disableColor",

    "strokeNormalColor",
    "strokePressColor",
    "strokeSelectedColor",
    "strokeActivatedColor",
    "strokeCheckedColor",
    "strokeDisableColor",

    "strokeWidth",
    "dashWidth",
    "dashGap",
    "cornerRadius",
    "direction",
    "gradientOrientation",
    requireAll = false
)
fun bindingViewBackground(
    view: View,
    @ColorInt color: Int? = null,
    @ColorInt endColor: Int? = null,
    @ColorInt strokeColor: Int? = null,
    // 颜色选择器
    @ColorInt normalColor: Int? = null,
    @ColorInt pressColor: Int? = null,
    @ColorInt selectedColor: Int? = null,
    @ColorInt activatedColor: Int? = null,
    @ColorInt checkedColor: Int? = null,
    @ColorInt disableColor: Int? = null,
    // 边框颜色选择器
    @ColorInt strokeNormalColor: Int? = null,
    @ColorInt strokePressColor: Int? = null,
    @ColorInt strokeSelectedColor: Int? = null,
    @ColorInt strokeActivatedColor: Int? = null,
    @ColorInt strokeCheckedColor: Int? = null,
    @ColorInt strokeDisableColor: Int? = null,

    strokeWidth: Int = 0,
    dashWidth: Int = 0,
    dashGap: Int = 0,
    cornerRadius: Int = 0,
    direction: Int = Direction.ALL,
    gradientOrientation: GradientDrawable.Orientation? = null
) {
    val safeDirection = if (cornerRadius > 0 && direction == 0) {
        Direction.ALL
    } else {
        direction
    }
    var colorStateList: ColorStateList? = null
    if (normalColor != null) {
        colorStateList = getColorStateList(
            normalColor = normalColor,
            pressColor = pressColor,
            selectedColor = selectedColor,
            activatedColor = activatedColor,
            checkedColor = checkedColor,
            disableColor = disableColor,
        )
    }
    var strokeColorStateList: ColorStateList? = null
    if (strokeNormalColor != null) {
        strokeColorStateList = getColorStateList(
            normalColor = strokeNormalColor,
            pressColor = strokePressColor,
            selectedColor = strokeSelectedColor,
            activatedColor = strokeActivatedColor,
            checkedColor = strokeCheckedColor,
            disableColor = strokeDisableColor,
        )
    }
    view.setBackground(
        color = color,
        endColor = endColor,
        strokeColor = strokeColor,
        colorStateList = colorStateList,
        strokeColorStateList = strokeColorStateList,
        strokeWidth = strokeWidth.dp,
        dashWidth = dashWidth.dpF,
        dashGap = dashGap.dpF,
        cornerRadius = cornerRadius.dpF,
        direction = safeDirection,
        orientation = gradientOrientation ?: GradientDrawable.Orientation.TOP_BOTTOM
    )
}

@BindingAdapter(
    "shadowRadius",
    "shadowWidth",
    "shadowAlphaSpacing",
    "shadowColor",
    "shadowSolidColor",
    requireAll = false
)
fun bindingViewShadow(
    view: View,
    shadowRadius: Int,
    shadowWidth: Int,
    shadowAlphaSpacing: Float? = null,
    @ColorInt shadowColor: Int? = null,
    @ColorInt shadowSolidColor: Int? = null
) {
//    val a = shadowColor shr 24 and 0xff
//    val r = shadowColor shr 16 and 0xff
//    val g = shadowColor shr 8 and 0xff
//    val b = shadowColor and 0xff

    val alphaSpacing = shadowAlphaSpacing ?: 0.01f
    val solidColor = shadowSolidColor ?: Color.parseColor("#ffffff")
    val color = Color.valueOf(shadowColor ?: Color.parseColor("#ebedf2"))
    val r = color.red()
    val g = color.green()
    val b = color.blue()

    val layers = arrayOfNulls<PaintDrawable>(shadowWidth + 1)
    (0 until shadowWidth).forEach {
        layers[it] = PaintDrawable().apply {
            val alpha = it * alphaSpacing
            paint.color = Color.argb(alpha, r, g, b)
            setPadding(1.dp, 1.dp, 1.dp, 1.dp)
            setCornerRadius(shadowRadius.dpF)
        }
    }
    layers[shadowWidth] = PaintDrawable().apply {
        paint.color = solidColor
        setPadding(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)
        setCornerRadius(shadowRadius.dpF)
    }

    view.background = LayerDrawable(layers)
}