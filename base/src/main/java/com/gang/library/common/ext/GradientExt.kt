package com.gang.library.common.ext

import android.graphics.drawable.GradientDrawable

/**
 * 渐变色 Drawable 扩展
 *
 * Created on 2021/11/16.
 *
 * @author o.s
 */

/**
 * 处理 GradientDrawable 圆角
 * [cornerRadius] 圆角
 * [direction] 方向
 */
fun GradientDrawable.setCorner(
    cornerRadius: Float = 0F,
    direction: Int = Direction.ALL,
) {
    if (cornerRadius > 0) {
        if (direction == Direction.ALL) {
            this.cornerRadius = cornerRadius
        } else {
            val rs = floatArrayOf(0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F)
            if (direction and Direction.LEFT_TOP == Direction.LEFT_TOP) {
                rs[0] = cornerRadius
                rs[1] = cornerRadius
            }
            if (direction and Direction.RIGHT_TOP == Direction.RIGHT_TOP) {
                rs[2] = cornerRadius
                rs[3] = cornerRadius
            }
            if (direction and Direction.RIGHT_BOTTOM == Direction.RIGHT_BOTTOM) {
                rs[4] = cornerRadius
                rs[5] = cornerRadius
            }
            if (direction and Direction.LEFT_BOTTOM == Direction.LEFT_BOTTOM) {
                rs[6] = cornerRadius
                rs[7] = cornerRadius
            }
            this.cornerRadii = rs
        }
    }
}