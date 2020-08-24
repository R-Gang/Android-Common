package com.gang.library.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.TextView

@SuppressLint("AppCompatCustomView")
class DrawableCenterTextView : TextView {
    constructor(context: Context?) : super(context) { // TODO Auto-generated constructor stub
    }

    constructor(
        context: Context?, attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) { // TODO Auto-generated constructor stub
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) { // TODO Auto-generated constructor stub
    }

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