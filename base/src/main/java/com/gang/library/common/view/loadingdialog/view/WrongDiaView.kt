package com.gang.library.common.view.loadingdialog.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.gang.library.common.utils.U.dip2px

/**
 * Created by Luo_xiasuhuei321@163.com on 2016/11/6.
 * desc:
 */
class WrongDiaView @JvmOverloads constructor(
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
    private var count = 0
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
                dip2px(mContext!!, 80f)
            }
        setMeasuredDimension(mWidth, mWidth)
        mPadding = 8f
        rectF = RectF(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding)
    }

    //    private void initAttr(Context context, AttributeSet attrs, int defStyleAttr) {
//        TypedArray a = context.getTheme()
//                .obtainStyledAttributes(attrs, R.styleable.WrongDiaView, defStyleAttr, 0);
//        for (int i = 0; i < a.getIndexCount(); i++) {
//            int attr = a.getIndex(i);
//            if (attr == R.styleable.RightDiaView_speed) {
//                speed = a.getInt(attr, 1);
//            }
//            if (attr == R.styleable.RightDiaView_strokeColor) {
//                color = a.getColor(attr, Color.WHITE);
//            }
//        }
//        a.recycle();
//    }
    private fun initPaint(context: Context) {
        mContext = context
        mPaint = Paint()
        //抗锯齿
        mPaint!!.isAntiAlias = true
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.color = Color.WHITE
        mPaint!!.strokeWidth = 8f
    }

    var progress = 0
    override fun onDraw(canvas: Canvas) {
        if (drawEveryTime) drawDynamic(canvas) else {
            drawStatic(canvas)
            if (listener != null) listener!!.dispatchFinishEvent(this)
        }
    }

    private fun drawDynamic(canvas: Canvas) {
        if (progress < 100) progress += speed
        //根据进度画圆弧
        canvas.drawArc(rectF!!, 235f, 360 * progress / 100.toFloat(), false, mPaint!!)
        val line1_start = 3 * mWidth / 10
        val line2_startX = 7 * mWidth / 10
        //绘制×
        if (progress == 100) {
            if (line1_x + line1_start <= line2_startX) {
                line1_x += speed
                line1_y += speed
            }
            //画第一根线
            canvas.drawLine(
                line1_start.toFloat(), line1_start.toFloat(),
                line1_start + line1_x.toFloat(), line1_start + line1_y.toFloat(), mPaint!!
            )
            if (line1_x == 2 * mWidth / 5) {
                line1_x++
                line1_y++
            }
            if (line1_x >= 2 * mWidth / 5 && line2_startX - line2_y >= line1_start) {
                line2_x -= speed
                line2_y += speed
            }
            //画第二根线
            canvas.drawLine(
                line2_startX.toFloat(), line1_start.toFloat(),
                line2_startX + line2_x.toFloat(), line1_start + line2_y.toFloat(), mPaint!!
            )
            if (line2_startX - line2_y < line1_start) { //1.只分发一次绘制完成的事件
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
        }
        invalidate()
    }

    private fun drawStatic(canvas: Canvas) {
        canvas.drawArc(rectF!!, 0f, 360f, false, mPaint!!)
        val line1_start = 3 * mWidth / 10
        val line2_startX = 7 * mWidth / 10
        canvas.drawLine(
            line1_start.toFloat(), line1_start.toFloat(),
            line1_start + 2 * mWidth / 5.toFloat(), line1_start + 2 * mWidth / 5.toFloat(), mPaint!!
        )
        canvas.drawLine(
            line1_start + 2 * mWidth / 5.toFloat(), line1_start.toFloat(),
            line1_start.toFloat(), line1_start + 2 * mWidth / 5.toFloat(), mPaint!!
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
    protected fun setRepeatTime(times: Int) {
        if (drawEveryTime) this.times = times
    }

    /**
     * 动态画出还是直接画出
     */
    protected fun setDrawDynamic(drawEveryTime: Boolean) {
        this.drawEveryTime = drawEveryTime
    }

    /**
     * 设置速度
     */
    protected fun setSpeed(speed: Int) {
        require(!(speed <= 0 && speed >= 3)) {
            "how can u set this speed??  " + speed + "  do not " +
                    "use reflect to use this method!u can see the LoadingDialog class for how to" +
                    "set the speed"
        }
        this.speed = speed
    }

    protected fun setDrawColor(color: Int) {
        mPaint!!.color = color
    }

    fun setOnDrawFinishListener(f: FinishDrawListener?) {
        listener = f
    }

    //    private int color;
    init {
        //        initAttr(context, attrs, defStyleAttr);
        initPaint(context)
    }
}