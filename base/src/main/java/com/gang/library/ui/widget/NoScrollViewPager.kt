package com.gang.library.common.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.viewpager.widget.ViewPager

/**
 * 禁止左右滑动,解决ScrollView嵌套ViewPager不显示和出现空白部分
 */
class NoScrollViewPager : ViewPager {

    // ---------------禁止左右滑动---------------
    private var noScroll = true
    private var lastX = -1
    private var lastY = -1

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {
    }

    constructor(context: Context?) : super(context!!) {}

    //是否禁止左右滑动
    fun setNoScroll(noScroll: Boolean) {
        this.noScroll = noScroll
    }

    override fun scrollTo(x: Int, y: Int) {
        super.scrollTo(x, y)
    }

    override fun onTouchEvent(arg0: MotionEvent): Boolean {
        return if (noScroll) false else super.onTouchEvent(arg0)
    }

    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        return if (noScroll) false else super.onInterceptTouchEvent(arg0)
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        super.setCurrentItem(item, smoothScroll)
    }

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item)
    }

    /**
     * @ explain:这个 ViewPager是用来解决ScrollView里面嵌套ViewPager的 内部解决法的
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.rawX.toInt()
        val y = ev.rawY.toInt()
        var dealtX = 0
        var dealtY = 0
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                dealtX = 0
                dealtY = 0
                // 保证子View能够接收到Action_move事件
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                dealtX += Math.abs(x - lastX)
                dealtY += Math.abs(y - lastY)
                Log.i(NoScrollViewPager::class.java.name, "dealtX:=$dealtX")
                Log.i(NoScrollViewPager::class.java.name, "dealtY:=$dealtY")
                // 这里是否拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                if (dealtX >= dealtY) { // 左右滑动请求父 View 不要拦截
                    parent.requestDisallowInterceptTouchEvent(true)
                } else {
                    parent.requestDisallowInterceptTouchEvent(false)
                }
                lastX = x
                lastY = y
            }
            MotionEvent.ACTION_CANCEL -> {
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    //------------解决ScrollView嵌套ViewPager不显示和出现空白部分---------------
    private var current = 0
    private var heights = 0
    /**
     * 保存position与对于的View
     */
    private val mChildrenViews: HashMap<Int, View> =
        LinkedHashMap()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        if (mChildrenViews.size > current) {
            val child = mChildrenViews[current]
            child!!.measure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
            heights = child.measuredHeight
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heights, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun resetHeight(current: Int) {
        this.current = current
        if (mChildrenViews.size > current) {
            var layoutParams =
                layoutParams as ViewGroup.LayoutParams
            if (layoutParams == null) {
                layoutParams =
                    LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heights)
            } else {
                layoutParams.height = heights
            }
            setLayoutParams(layoutParams)
        }
    }

    /**
     * 保存position与对于的View
     */
    fun setObjectForPosition(view: View, position: Int) {
        mChildrenViews[position] = view
    }

}