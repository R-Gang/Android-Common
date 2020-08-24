package com.gang.library.ui.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.core.widget.NestedScrollView

/**
 * 有弹性的ScrollView
 * 实现下拉弹回和上拉弹回
 *
 * @author zhangjg
 * @date Feb 13, 2014 6:11:33 PM
 */
class ReboundScrollView : NestedScrollView {
    //ScrollView的子View， 也是ScrollView的唯一一个子View
    private var contentView: View? = null
    //手指按下时的Y值, 用于在移动时计算移动距离
//如果按下时不能上拉和下拉， 会在手指移动时更新为当前手指的Y值
    private var startY = 0f
    //用于记录正常的布局位置
    private val originalRect = Rect()
    //手指按下时记录是否可以继续下拉
    private var canPullDown = false
    //手指按下时记录是否可以继续上拉
    private var canPullUp = false
    //在手指滑动的过程中记录是否移动了布局
    private var isMoved = false

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 0) {
            contentView = getChildAt(0)
        }
    }

    override fun onLayout(
        changed: Boolean,
        l: Int,
        t: Int,
        r: Int,
        b: Int
    ) {
        super.onLayout(changed, l, t, r, b)
        if (contentView == null) return
        //ScrollView中的唯一子控件的位置信息, 这个位置信息在整个控件的生命周期中保持不变
        originalRect[contentView!!.left, contentView!!.top, contentView!!
            .right] = contentView!!.bottom
    }

    /**
     * 在触摸事件中, 处理上拉和下拉的逻辑
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (contentView == null) {
            return super.dispatchTouchEvent(ev)
        }
        val action = ev.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                //判断是否可以上拉和下拉
                canPullDown = isCanPullDown()
                canPullUp = isCanPullUp()
                //记录按下时的Y值
                startY = ev.y
            }
            MotionEvent.ACTION_UP -> {
                //如果有移动布局，则执行
                if (isMoved) {
                    // 开启动画
                    val anim = TranslateAnimation(
                        0F, 0F, contentView!!.top.toFloat(),
                        originalRect.top.toFloat()
                    )
                    anim.duration = ANIM_TIME.toLong()
                    contentView!!.startAnimation(anim)
                    // 设置回到正常的布局位置
                    contentView!!.layout(
                        originalRect.left, originalRect.top,
                        originalRect.right, originalRect.bottom
                    )
                    //将标志位设回false
                    canPullDown = false
                    canPullUp = false
                    isMoved = false
                }
            }
            MotionEvent.ACTION_MOVE -> {
                //在移动的过程中， 既没有滚动到可以上拉的程度， 也没有滚动到可以下拉的程度
                if (!canPullDown && !canPullUp) {
                    startY = ev.y
                    canPullDown = isCanPullDown()
                    canPullUp = isCanPullUp()
                } else {
                    //计算手指移动的距离
                    val nowY = ev.y
                    val deltaY = (nowY - startY).toInt()
                    //是否应该移动布局
                    val shouldMove = (canPullDown && deltaY > 0 //可以下拉， 并且手指向下移动
                            || canPullUp && deltaY < 0 //可以上拉， 并且手指向上移动
                            || canPullUp && canPullDown) //既可以上拉也可以下拉（这种情况出现在ScrollView包裹的控件比ScrollView还小）
                    if (shouldMove) { //计算偏移量
                        val offset = (deltaY * MOVE_FACTOR).toInt()
                        //随着手指的移动而移动布局
                        contentView!!.layout(
                            originalRect.left, originalRect.top + offset,
                            originalRect.right, originalRect.bottom + offset
                        )
                        isMoved = true //记录移动了布局
                    }
                }
            }
            else -> {
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 判断是否滚动到顶部
     */
    private fun isCanPullDown(): Boolean {
        return scrollY == 0 ||
                contentView!!.height < height + scrollY
    }

    /**
     * 判断是否滚动到底部
     */
    private fun isCanPullUp(): Boolean {
        return contentView!!.height <= height + scrollY
    }

    companion object {
        private const val TAG = "ElasticScrollView"
        //移动因子, 是一个百分比, 比如手指移动了100px, 那么View就只移动50px
//目的是达到一个延迟的效果
        private const val MOVE_FACTOR = 0.5f
        //松开手指后, 界面回到正常位置需要的动画时间
        private const val ANIM_TIME = 300
    }
}