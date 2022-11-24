package com.gang.library.common.ext.statelist

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.gang.tools.kotlin.utils.getColor
import com.gang.tools.kotlin.utils.getCompatDrawable
import com.gang.tools.kotlin.utils.getDrawable

/**
 * 状态选择器扩展
 *
 * Created on 2020/6/9.
 *
 * @author o.s
 */

/**
 * 颜色状态表
 */
fun Context.getColorStateList(
    @ColorRes normalColorRes: Int = android.R.color.transparent,
    @ColorRes pressColorRes: Int? = null,
    @ColorRes selectedColorRes: Int? = null,
    @ColorRes activatedColorRes: Int? = null,
    @ColorRes checkedColorRes: Int? = null,
    @ColorRes disableColorRes: Int? = null,
    @ColorInt normalColor: Int? = null,
    @ColorInt pressColor: Int? = null,
    @ColorInt selectedColor: Int? = null,
    @ColorInt activatedColor: Int? = null,
    @ColorInt checkedColor: Int? = null,
    @ColorInt disableColor: Int? = null,
): ColorStateList {
    return getColorStateList(
        normalColor = normalColor ?: getColor(normalColorRes),
        pressColor = pressColor ?: pressColorRes?.let { getColor(it) },
        selectedColor = selectedColor ?: selectedColorRes?.let { getColor(it) },
        activatedColor = activatedColor ?: activatedColorRes?.let { getColor(it) },
        checkedColor = checkedColor ?: checkedColorRes?.let { getColor(it) },
        disableColor = disableColor ?: disableColorRes?.let { getColor(it) },
    )
}

/**
 * 颜色状态表
 */
fun View.getColorStateList(
    @ColorRes normalColorRes: Int = android.R.color.transparent,
    @ColorRes pressColorRes: Int? = null,
    @ColorRes selectedColorRes: Int? = null,
    @ColorRes activatedColorRes: Int? = null,
    @ColorRes checkedColorRes: Int? = null,
    @ColorRes disableColorRes: Int? = null,
    @ColorInt normalColor: Int? = null,
    @ColorInt pressColor: Int? = null,
    @ColorInt selectedColor: Int? = null,
    @ColorInt activatedColor: Int? = null,
    @ColorInt checkedColor: Int? = null,
    @ColorInt disableColor: Int? = null,
): ColorStateList {
    return getColorStateList(
        normalColor = normalColor ?: getColor(normalColorRes),
        pressColor = pressColor ?: pressColorRes?.let { getColor(it) },
        selectedColor = selectedColor ?: selectedColorRes?.let { getColor(it) },
        activatedColor = activatedColor ?: activatedColorRes?.let { getColor(it) },
        checkedColor = checkedColor ?: checkedColorRes?.let { getColor(it) },
        disableColor = disableColor ?: disableColorRes?.let { getColor(it) },
    )
}

/**
 * Drawable状态表
 */
fun Context.getDrawableStateList(
    @DrawableRes normalRes: Int? = null,
    @DrawableRes pressedRes: Int? = null,
    @DrawableRes selectedRes: Int? = null,
    @DrawableRes activatedRes: Int? = null,
    @DrawableRes checkedRes: Int? = null,
    @DrawableRes disableRes: Int? = null,
    normal: Drawable? = null,
    pressed: Drawable? = null,
    selected: Drawable? = null,
    activated: Drawable? = null,
    checked: Drawable? = null,
    disable: Drawable? = null,
): StateListDrawable {
    return getDrawableStateList(
        normal = normal ?: getCompatDrawable(normalRes),
        pressed = pressed ?: getCompatDrawable(pressedRes),
        selected = selected ?: getCompatDrawable(selectedRes),
        activated = activated ?: getCompatDrawable(activatedRes),
        checked = checked ?: getCompatDrawable(checkedRes),
        disable = disable ?: getCompatDrawable(disableRes),
    )
}

/**
 * Drawable状态表
 */
fun View.getDrawableStateList(
    @DrawableRes normalRes: Int? = null,
    @DrawableRes pressedRes: Int? = null,
    @DrawableRes selectedRes: Int? = null,
    @DrawableRes activatedRes: Int? = null,
    @DrawableRes checkedRes: Int? = null,
    @DrawableRes disableRes: Int? = null,
    normal: Drawable? = null,
    pressed: Drawable? = null,
    selected: Drawable? = null,
    activated: Drawable? = null,
    checked: Drawable? = null,
    disable: Drawable? = null,
): StateListDrawable {
    return getDrawableStateList(
        normal = normal ?: getDrawable(normalRes),
        pressed = pressed ?: getDrawable(pressedRes),
        selected = selected ?: getDrawable(selectedRes),
        activated = activated ?: getDrawable(activatedRes),
        checked = checked ?: getDrawable(checkedRes),
        disable = disable ?: getDrawable(disableRes),
    )
}

/**
 * 颜色状态表
 */
fun getColorStateList(
    @ColorInt normalColor: Int,
    @ColorInt pressColor: Int? = null,
    @ColorInt selectedColor: Int? = null,
    @ColorInt activatedColor: Int? = null,
    @ColorInt checkedColor: Int? = null,
    @ColorInt disableColor: Int? = null,
): ColorStateList {
    //初始化，默认可用样式
    val states = Array(6) { State.ENABLE.value }
    val colors = IntArray(6) { normalColor }

    pressColor?.apply {
        states[0] = State.PRESSED.value
        colors[0] = this
    }
    selectedColor?.apply {
        states[1] = State.SELECTED.value
        colors[1] = this
    }
    activatedColor?.apply {
        states[2] = State.ACTIVATED.value
        colors[2] = this
    }
    checkedColor?.apply {
        states[3] = State.CHECKED.value
        colors[3] = this
    }
    disableColor?.apply {
        states[4] = State.DISABLE.value
        colors[4] = this
    }

    return ColorStateList(states, colors)
}

/**
 * Drawable状态表
 */
fun getDrawableStateList(
    normal: Drawable? = null,
    pressed: Drawable? = null,
    selected: Drawable? = null,
    activated: Drawable? = null,
    checked: Drawable? = null,
    disable: Drawable? = null,
): StateListDrawable {
    val sld = StateListDrawable()
    checked?.run {
        sld.addState(State.CHECKED.value, this)
    }
    activated?.run {
        sld.addState(State.ACTIVATED.value, this)
    }
    pressed?.run {
        sld.addState(State.PRESSED.value, this)
    }
    selected?.run {
        sld.addState(State.SELECTED.value, this)
    }
    disable?.run {
        sld.addState(State.DISABLE.value, this)
    }
    sld.addState(State.ENABLE.value, normal)
    return sld
}

/**
 * State状态类型枚举
 */
enum class State(val value: IntArray) {
    DISABLE(intArrayOf(-android.R.attr.state_enabled)),
    ENABLE(intArrayOf(android.R.attr.state_enabled)),
    PRESSED(intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)),
    SELECTED(intArrayOf(android.R.attr.state_selected, android.R.attr.state_enabled)),
    CHECKED(intArrayOf(android.R.attr.state_checked, android.R.attr.state_enabled)),
    ACTIVATED(intArrayOf(android.R.attr.state_activated, android.R.attr.state_enabled)),
}