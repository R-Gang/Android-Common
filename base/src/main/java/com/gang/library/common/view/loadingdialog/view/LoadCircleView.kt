package com.gang.library.common.view.loadingdialog.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.gang.library.common.utils.U

/**
 * Created by xiasuhuei321 on 2017/5/15.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */
class LoadCircleView @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(mContext, attrs, defStyleAttr) {
    val TAG = javaClass.simpleName
    private var mPadding = 0f
    private val rectF: RectF? = null
    private var mPaint: Paint? = null
    private var mWidth = 0
    private var currentLineIndex = 0
    fun init() {
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.style = Paint.Style.STROKE
        //        mPaint.setColor(Color.BLACK);
        mPaint!!.strokeWidth = 8f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        mWidth =
            if (widthSpecMode != MeasureSpec.AT_MOST && heightSpecMode != MeasureSpec.AT_MOST) {
                if (widthSpecSize >= heightSpecSize) widthSpecSize else heightSpecSize
            } else if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode != MeasureSpec.AT_MOST) {
                heightSpecSize
            } else if (widthSpecMode != MeasureSpec.AT_MOST) {
                widthSpecSize
            } else {
                U.dip2px(mContext, 50f)
            }
        setMeasuredDimension(mWidth, mWidth)
        mPadding = 8f
        //        rectF = new RectF(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding);
    }

    override fun onDraw(canvas: Canvas) { // 圆心坐标 (center,center)
        val center = mWidth shr 1
        val radius = (mWidth shr 1) - 8
        if (currentLineIndex >= 12) currentLineIndex = 0
        //        canvas.rotate(currentLineIndex * 30, center, center);
// 画12根线
        for (i in 0..11) {
            if (i < currentLineIndex + 4 && i >= currentLineIndex) {
                mPaint!!.color = Color.GRAY
            } else if (currentLineIndex > 8 && i < currentLineIndex + 4 - 12) {
                mPaint!!.color = Color.GRAY
            } else {
                mPaint!!.color = Color.WHITE
            }
            //            canvas.drawLine(center, (float) (center + 1.0 / 4 * center),
//                    center, (float) (center + 1.0 / 2 * radius), mPaint);
            canvas.drawLine(
                center.toFloat(), (center + 1.0 / 2 * radius).toFloat(),
                center.toFloat(), 2 * radius.toFloat(), mPaint!!
            )
            canvas.rotate(30f, center.toFloat(), center.toFloat())
        }
        currentLineIndex++
        postInvalidateDelayed(50)
    }

    init {
        init()
    }
}