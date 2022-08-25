package com.gang.library.common.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class DrawableCenterTextView : AppCompatTextView {

    constructor(context: Context) : super(context)

    constructor(
        context: Context, attrs: AttributeSet?,
        defStyle: Int,
    ) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet?) : super(
        context,
        attrs
    )

    override fun onDraw(canvas: Canvas) { //在布局文件中设置TextView的四周图片，用getCompoundDrawables方法可以获取这4个位置的图片
        val drawables = compoundDrawables
        val left = drawables[0]
        if (left != null) {
            val textWidth = paint.measureText(text.toString())
            val padding = compoundDrawablePadding
            val width: Int = left.intrinsicHeight
            val bodyWidth = textWidth + width + padding
            canvas.translate((getWidth() - bodyWidth) / 2, 0f)
        }
        super.onDraw(canvas)
    }
}