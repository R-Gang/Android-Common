package com.gang.library.common.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.gang.library.R
import com.gang.tools.kotlin.dimension.dpF

/**
 * @ClassName: haoruigang
 * @Description: 平滑圆角(不规则圆角)
 * @Author: haoruigang
 * @CreateDate: 2021/10/11 10:59
 */
class SquircleImageView : AppCompatImageView {

    private var borderWidth //不规则圆角边框宽度
            = 1.dpF
    private var borderColor //不规则圆角边框颜色
            = 0x20000000

    // 资源部分
    private var clipPath: Path? = null
    private var clipPaint: Paint? = null

    // 边框部分
    private val borderPath = Path()
    private var borderPaint: Paint? = null

    constructor(context: Context?) : super(context!!) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        // Why not clipToOutline? Because clipping isn't supported for arbitrary paths ¯\_(ツ)_/¯
        setLayerType(
            LAYER_TYPE_HARDWARE,
            null
        )
        // important so that CLEAR xfermode doesn't put a hole through the entire window
        clipPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        clipPaint?.color = -0x1000000
        clipPaint?.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        val types = context.obtainStyledAttributes(attrs, R.styleable.SquircleImageView)
        try {
            //不规则圆角边框宽度
            borderWidth =
                types.getDimensionPixelOffset(R.styleable.SquircleImageView_siv_border_width, 1)
                    .toFloat()
            //不规则圆角边框颜色
            borderColor =
                types.getColor(R.styleable.SquircleImageView_siv_border_color, Color.TRANSPARENT)
        } finally {
            types.recycle() //TypeArray用完需要recycle
        }
        borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        borderPaint?.color = borderColor
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        clipPath = Path()
        clipPath?.moveTo(0.0f, 100.0f)
        clipPath?.cubicTo(0.0f, 33.0f, 33.0f, 0.0f, 100.0f, 0.0f)
        clipPath?.cubicTo(167.0f, 0.0f, 200.0f, 33.0f, 200.0f, 100.0f)
        clipPath?.cubicTo(200.0f, 167.0f, 167.0f, 200.0f, 100.0f, 200.0f)
        clipPath?.cubicTo(33.0f, 200.0f, 0.0f, 167.0f, 0.0f, 100.0f)
        clipPath?.close()
        val m = Matrix()
        m.setScale(w / 200f, h / 200f, 0f, 0f)
        clipPath?.transform(m)
        m.setScale((w - borderWidth.dpF) / w,
            (w - borderWidth.dpF) / h,
            w / 2f,
            h / 2f) // 边框
        clipPath?.transform(m, borderPath)
        clipPath?.toggleInverseFillType()
        borderPath.toggleInverseFillType()
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (clipPath != null) {
            borderPaint?.let { canvas.drawPath(borderPath, it) }
            clipPaint?.let { canvas.drawPath(clipPath!!, it) }
        }
    }
}