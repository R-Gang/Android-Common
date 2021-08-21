package com.gang.library.ui.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.gang.library.R
import kotlin.math.acos
import kotlin.math.sqrt
class ColorWheel : View {

    private var bigCircle //外圆半径
            = 0
    private var rudeRadius //可移动小球的半径
            = 0
    private var centerColor //可移动小球的颜色
            = 0
    private var bitmapBack //背景图片
            : Bitmap? = null
    private var mPaint //背景画笔
            : Paint? = null
    private var mCenterPaint //可移动小球背景
            : Paint? = null
    private var centerPoint //中心位置
            : Point? = null
    private var mRockPosition //小球当前位置
            : Point? = null
    private var listener //小球移动监听
            : OnColorChangedListener? = null
    private var length //小球到中心位置的距离
            = 0
    var colorStr = ""

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    fun setOnColorChangedListener(listener: OnColorChangedListener?) {
        this.listener = listener
    }

    /**
     * @describe 初始化操作
     * @param attrs
     */
    private fun init(attrs: AttributeSet?) {
        val types = context.obtainStyledAttributes(attrs, R.styleable.ColorWheel)
        try {
            //外圆半径
            bigCircle = types.getDimensionPixelOffset(R.styleable.ColorWheel_circle_radius, 100)
            //可移动小球半径
            rudeRadius = types.getDimensionPixelOffset(R.styleable.ColorWheel_center_radius, 10)
            //可移动小球的颜色
            centerColor = types.getColor(R.styleable.ColorWheel_center_color, Color.WHITE)
        } finally {
            types.recycle() //TypeArray用完需要recycle
        }

        //中心位置坐标
        centerPoint = Point(bigCircle, bigCircle)
        mRockPosition = Point(centerPoint!!)

        //初始化背景画笔和可移动小球的画笔
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.isDither = true
        mCenterPaint = Paint()
        mCenterPaint!!.color = centerColor
        bitmapBack = createColorBitmap(bigCircle * 2, bigCircle * 2)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //画背景图
        canvas.drawBitmap(bitmapBack!!, 0f, 0f, null)
        //画中心小球
        canvas.drawCircle(mRockPosition!!.x.toFloat(), mRockPosition!!.y.toFloat(), rudeRadius.toFloat(), mCenterPaint!!)
    }

    private fun createColorBitmap(width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val colorCount = 12
        val colorAngleStep = 360 / 12
        val colors = IntArray(colorCount + 1)
        val hsv = floatArrayOf(0f, 1f, 1f)
        for (i in colors.indices) {
            hsv[0] = (360 - i * colorAngleStep % 360).toFloat()
            colors[i] = Color.HSVToColor(hsv)
        }
        colors[colorCount] = colors[0]
        val sweepGradient = SweepGradient((width / 2).toFloat(), (height / 2).toFloat(), colors, null)
        val radialGradient =  RadialGradient((width / 2).toFloat(), (height / 2).toFloat(), bigCircle.toFloat(),
                -0x1, 0x00FFFFFF, Shader.TileMode.CLAMP)
        val composeShader = ComposeShader(sweepGradient, radialGradient, PorterDuff.Mode.SRC_OVER)
        mPaint!!.shader = composeShader
        val canvas = Canvas(bitmap)
//        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), bigCircle.toFloat(), mPaint!!)

        var r1 = RectF()
        r1.left = 0f
        r1.top =0f
        r1.right = width.toFloat()
        r1.bottom = height.toFloat()
        canvas.drawRoundRect(r1,10f,10f, mPaint!!)
        return bitmap
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_BUTTON_PRESS -> {
                length = getLength(event.x, event.y, centerPoint!!.x, centerPoint!!.y)
                if (length <= bigCircle - rudeRadius) {
                    mRockPosition!![event.x.toInt()] = event.y.toInt()
                } else {
                    mRockPosition = getBorderPoint(centerPoint, Point(event.x.toInt(), event.y.toInt()), bigCircle - rudeRadius)
                }

                val pixel = bitmapBack!!.getPixel(mRockPosition!!.x, mRockPosition!!.y)
                val r = Color.red(pixel)
                val g = Color.green(pixel)
                val b = Color.blue(pixel)
                val a = Color.alpha(pixel)

                //十六进制的颜色字符串
                colorStr = "#" + toBrowserHexValue(r) + toBrowserHexValue(g) + toBrowserHexValue(b)
                if (listener != null) {
                    listener!!.onColorPick(a, r, g, b)
                }
            }
            MotionEvent.ACTION_DOWN -> {
                length = getLength(event.x, event.y, centerPoint!!.x, centerPoint!!.y)
                if (length > bigCircle - rudeRadius) {
                    return true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                length = getLength(event.x, event.y, centerPoint!!.x, centerPoint!!.y)
                if (length <= bigCircle - rudeRadius) {
                    mRockPosition!![event.x.toInt()] = event.y.toInt()
                } else {
                    mRockPosition = getBorderPoint(centerPoint, Point(event.x.toInt(), event.y.toInt()), bigCircle - rudeRadius)
                }
            }
            MotionEvent.ACTION_UP -> {
                val pixel = bitmapBack!!.getPixel(mRockPosition!!.x, mRockPosition!!.y)
                val r = Color.red(pixel)
                val g = Color.green(pixel)
                val b = Color.blue(pixel)
                val a = Color.alpha(pixel)

                //十六进制的颜色字符串
                colorStr = "#" + toBrowserHexValue(r) + toBrowserHexValue(g) + toBrowserHexValue(b)
                if (listener != null) {
                    listener!!.onColorPick(a, r, g, b)
                }
            }
            else -> {
            }
        }
        rGB
        invalidate() //更新画布
        return true
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(event)
    }

    /*
     *转16进制数
     */
    private fun toBrowserHexValue(number: Int): String {
        val builder = StringBuilder(Integer.toHexString(number and 0xff))
        while (builder.length < 2) {
            builder.append("0")
        }
        return builder.toString().toUpperCase()
    }//十六进制的颜色字符串

    /*
       *像素转RGB
       */
    private val rGB: Unit
        get() {
            val pixel = bitmapBack!!.getPixel(mRockPosition!!.x, mRockPosition!!.y)
            val r = Color.red(pixel)
            val g = Color.green(pixel)
            val b = Color.blue(pixel)
            val a = Color.alpha(pixel)

            //十六进制的颜色字符串
            colorStr = "#" + toBrowserHexValue(r) + toBrowserHexValue(g) + toBrowserHexValue(b)
            if (listener != null) {
                listener!!.onColorChange(a, r, g, b)
            }
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //视图大小设置为直径
        setMeasuredDimension(bigCircle * 2, bigCircle * 2)
    }

    //颜色发生变化的回调接口
    interface OnColorChangedListener {
        fun onColorChange(a: Int, r: Int, g: Int, b: Int)
        fun onColorPick(a: Int, r: Int, g: Int, b: Int)
    }

    companion object {
        //计算两点之间的位置
        private fun getLength(x1: Float, y1: Float, x2: Int, y2: Int): Int {
            return Math.sqrt(Math.pow((x1 - x2).toDouble(), 2.0) + Math.pow((y1 - y2).toDouble(), 2.0)).toInt()
        }

        //当触摸点超出圆的范围的时候，设置小球边缘位置
        private fun getBorderPoint(a: Point?, b: Point, cutRadius: Int): Point {
            val radian = getRadian(a, b)
            return Point(a!!.x + (cutRadius * Math.cos(radian.toDouble())).toInt(), a.x + (cutRadius * Math.sin(radian.toDouble())).toInt())
        }

        //触摸点与中心点之间直线与水平方向的夹角角度
        fun getRadian(a: Point?, b: Point): Float {
            val lenA = (b.x - a!!.x).toFloat()
            val lenB = (b.y - a.y).toFloat()
            val lenC = sqrt((lenA * lenA + lenB * lenB).toDouble()).toFloat()
            var ang = acos((lenA / lenC).toDouble()).toFloat()
            ang *= if (b.y < a.y) -1 else 1
            return ang
        }
    }
}