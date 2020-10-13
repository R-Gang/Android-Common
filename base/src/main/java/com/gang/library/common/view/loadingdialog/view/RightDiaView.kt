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
 * Created by Luo_xiasuhuei321@163.com on 2016/11/5.
 * desc:
 */
class RightDiaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val TAG = this.javaClass.simpleName
    private var listener: FinishDrawListener? = null
    private lateinit var mContext: Context
    private var mWidth = 0
    private var mPadding = 0f
    private var mPaint: Paint? = null
    private var rectF: RectF? = null
    private var line1_x = 0
    private var line1_y = 0
    private var line2_x = 0
    private var line2_y = 0
    private var times = 0
    private var drawEveryTime = true
    private var speed = 1
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
                U.dip2px(mContext, 80f)
            }
        setMeasuredDimension(mWidth, mWidth)
        mPadding = 8f
        rectF = RectF(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding)
    }

    private fun init(context: Context) {
        mPaint = Paint()
        //抗锯齿
        mPaint!!.isAntiAlias = true
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.color = Color.WHITE
        mPaint!!.strokeWidth = 8f
        mContext = context
    }

    var progress = 0
    override fun onDraw(canvas: Canvas) {
        if (drawEveryTime) drawDynamic(canvas) else {
            drawStatic(canvas)
            if (listener != null) listener!!.dispatchFinishEvent(this)
        }
    }

    private var count = 0
    private fun drawDynamic(canvas: Canvas) {
        if (progress < 100) progress += speed
        //根据进度画圆弧
        canvas.drawArc(rectF!!, 235f, 360 * progress / 100.toFloat(), false, mPaint!!)
        val center = mWidth / 2
        val center1 = center - mWidth / 5
        val radius = mWidth / 2 - 8
        //绘制对勾
        if (progress == 100) {
            if (line1_x < radius / 3) {
                line1_x += speed
                line1_y += speed
            }
            //画第一根线
            canvas.drawLine(
                center1.toFloat(),
                center.toFloat(),
                center1 + line1_x.toFloat(),
                center + line1_y.toFloat(),
                mPaint!!
            )
            if (line1_x >= radius / 3 && line2_x == 0 && line2_y == 0) {
                line2_x = line1_x
                line2_y = line1_y
                line1_x += speed
                line1_y += speed
            }
            if (line1_x >= radius / 3 && line2_x <= radius && line2_y <= center - radius / 3) {
                line2_x += speed
                line2_y -= speed
            }
            //画第二根线
            canvas.drawLine(
                center1 + line1_x - 1.toFloat(), center + line1_y - 4.toFloat(),
                center1 + line2_x.toFloat(), center + line2_y.toFloat(), mPaint!!
            )
        }
        if (line2_x > radius && progress >= 100 && line1_x != radius / 3) { //1.只分发一次绘制完成的事件
//2.只在最后一次绘制时分发
            if (count == 0 && times == 0 && listener != null) {
                listener!!.dispatchFinishEvent(this)
                count++
            }
            times--
            if (times >= 0) {
                reDraw()
                invalidate()
            } else {
                return
            }
        }
        invalidate()
    }

    private fun drawStatic(canvas: Canvas) {
        canvas.drawArc(rectF!!, 0f, 360f, false, mPaint!!)
        val center = mWidth / 2
        val center1 = center - mWidth / 5
        val radius = mWidth / 2 - 8
        canvas.drawLine(
            center1.toFloat(), center.toFloat(),
            center1 + radius / 3.toFloat(), center + radius / 3.toFloat(), mPaint!!
        )
        canvas.drawLine(
            center1 + radius / 3 - 1.toFloat(), center + radius / 3 - 4.toFloat(),
            center1 + radius.toFloat(), center - radius / 3.toFloat(), mPaint!!
        )
    }

    private fun reDraw() {
        line1_x = 0
        line2_x = 0
        line1_y = 0
        line2_y = 0
        progress = 0
    }
    //---------------------------对外提供的api-------------------------//
    /**
     * 设置重复绘制的次数，只在drawEveryTime = true时有效
     *
     * @param times 重复次数，例如设置1，除了第一次绘制还会额外重绘一次
     */
    fun setRepeatTime(times: Int) {
        if (drawEveryTime) this.times = times
    }

    /**
     * 动态画出还是直接画出
     */
    fun setDrawDynamic(drawEveryTime: Boolean) {
        this.drawEveryTime = drawEveryTime
    }

    /**
     * 调整绘制的速度，最小值默认为1
     *
     * @param speed 速度
     */
    fun setSpeed(speed: Int) {
        require(speed !in 3..0) { "support speed >0 & < 3, the speed you set is: $speed" }
        this.speed = speed
    }

    fun setDrawColor(color: Int) {
        mPaint!!.color = color
    }

    fun setOnDrawFinishListener(f: FinishDrawListener?) {
        listener = f
    }

    init {
        init(context)
    }
}