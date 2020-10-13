package com.gang.library.common.view.loadingdialog.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt

/**
 * Created by Luo on 2016/9/23.
 * desc:
 */
class LVCircularRing @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    val TAG = javaClass.simpleName
    private var mWidth = 0f
    private var mPadding = 0f
    private var startAngle = 0f
    private var mPaint: Paint? = null
    private var color = Color.argb(100, 255, 255, 255)
    private var mPaint2: Paint? = null
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = if (measuredWidth > height) measuredHeight.toFloat() else measuredWidth.toFloat()
        mPadding = 5f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //        mPaint.setColor(Color.argb(100, 255, 255, 255));
        mPaint2!!.color = color
        canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2 - mPadding, mPaint2!!)
        mPaint!!.color = Color.WHITE
        val rectF = RectF(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding)
        canvas.drawArc(rectF, startAngle, 100f, false, mPaint!!) //第四个参数是否显示半径
    }

    private fun initPaint() {
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.color = color
        mPaint!!.strokeWidth = 8f
        mPaint2 = Paint()
        mPaint2!!.isAntiAlias = true
        mPaint2!!.style = Paint.Style.STROKE
        mPaint2!!.strokeWidth = 8f
        mPaint2!!.color = color
    }

    fun startAnim() {
        stopAnim()
        startViewAnim(0f, 1f, 1000)
    }

    fun stopAnim() {
        if (valueAnimator != null) {
            clearAnimation()
            valueAnimator!!.repeatCount = 1
            valueAnimator!!.cancel()
            valueAnimator!!.end()
        }
    }

    lateinit var valueAnimator: ValueAnimator
    private fun startViewAnim(
        startF: Float,
        endF: Float,
        time: Long
    ): ValueAnimator? {
        valueAnimator = ValueAnimator.ofFloat(startF, endF)
        valueAnimator.duration = time
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.repeatCount = ValueAnimator.INFINITE //无限循环
        valueAnimator.repeatMode = ValueAnimator.RESTART //
        valueAnimator.addUpdateListener(AnimatorUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            startAngle = 360 * value
            invalidate()
        })
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
            }
        })
        if (!valueAnimator.isRunning) {
            valueAnimator.start()
        }
        return valueAnimator
    }

    fun setColor(@ColorInt color: Int) {
        this.color = color
        mPaint!!.color = color
        mPaint2!!.color = color
    }

    init {
        initPaint()
    }
}