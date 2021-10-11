package com.gang.library.ui.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * @ClassName: haoruigang
 * @Description: 平滑圆角(不规则圆角)
 * @Author: haoruigang
 * @CreateDate: 2021/10/11 10:59
 */
class SquircleImageView : AppCompatImageView {
    private var clipPath: Path? = null
    private val borderPath = Path()
    private var clipPaint: Paint? = null
    private var borderPaint: Paint? = null

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        init()
    }

    private fun init() {
        // Why not clipToOutline? Because clipping isn't supported for arbitrary paths ¯\_(ツ)_/¯
        setLayerType(
            LAYER_TYPE_HARDWARE,
            null
        ) // important so that CLEAR xfermode doesn't put a hole through the entire window
        clipPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        clipPaint!!.color = -0x1000000
        clipPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        borderPaint!!.color = 0x20000000
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        clipPath = Path()
        clipPath!!.moveTo(0.0f, 100.0f)
        clipPath!!.cubicTo(0.0f, 33.0f, 33.0f, 0.0f, 100.0f, 0.0f)
        clipPath!!.cubicTo(167.0f, 0.0f, 200.0f, 33.0f, 200.0f, 100.0f)
        clipPath!!.cubicTo(200.0f, 167.0f, 167.0f, 200.0f, 100.0f, 200.0f)
        clipPath!!.cubicTo(33.0f, 200.0f, 0.0f, 167.0f, 0.0f, 100.0f)
        clipPath!!.close()
        val m = Matrix()
        m.setScale(w / 200f, h / 200f, 0f, 0f)
        clipPath!!.transform(m)
        m.setScale((w - dp(1f)).toFloat() / w, (w - dp(1f)).toFloat() / h, w / 2f, h / 2f)
        clipPath!!.transform(m, borderPath)
        clipPath!!.toggleInverseFillType()
        borderPath.toggleInverseFillType()
    }

    fun dp(dp: Float): Int {
        return Math.round(dp * context.resources.displayMetrics.density)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (clipPath != null) {
            canvas.drawPath(borderPath, borderPaint!!)
            canvas.drawPath(clipPath!!, clipPaint!!)
        }
    }
}