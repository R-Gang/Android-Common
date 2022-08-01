package com.gang.app.common.view.smartrefresh.custom

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.Nullable
import com.gang.app.R
import com.library.kotlin.dimension.dip2px
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle

class MRefreshHeader(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr), RefreshHeader {

    /**
     * 1，构造方法
     */
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, @Nullable attrs: AttributeSet?) : this(context, attrs, 0) {
        val view = inflate(context, R.layout.loading_refresh, null)
        val iv1 = view.findViewById<ImageView>(R.id.iv1)
        val iv2 = view.findViewById<ImageView>(R.id.iv2)
        t1(iv1, iv2)
        this.addView(view)
    }

    private fun t1(v: View, v2: View) {
        val holder = PropertyValuesHolder.ofFloat(TRANSLATION_X, 0f, dip2px(20f), 0f)
        val holder2 = PropertyValuesHolder.ofFloat(TRANSLATION_X, 0f, -dip2px(20f), 0f)
        val set = AnimatorSet()
        val obj = ObjectAnimator.ofPropertyValuesHolder(v, holder)
        val obj2 = ObjectAnimator.ofPropertyValuesHolder(v2, holder2)
        obj.repeatCount = -1
        obj.addUpdateListener { it ->
            if (it.animatedValue.toString().toFloat() >= dip2px(19f)) {
                v.bringToFront()
            }
            if (it.animatedValue.toString().toFloat() <= dip2px(1f)) {
                v2.bringToFront()
            }
        }
        obj2.repeatCount = -1
        set.play(obj).with(obj2)
        set.duration = 1200
        set.interpolator = LinearInterpolator()
        set.start()
    }

    /**
     * 2，获取真实视图（必须返回，不能为null）一般就是返回当前自定义的view
     */
    override fun getView(): View {
        return this
    }


    /**
     * 3，获取变换方式（必须指定一个：平移、拉伸、固定、全屏）,Translate指平移，大多数都是平移
     */
    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate
    }

    /**
     * 5，一般可以理解为一下case中的三种状态，在达到相应状态时候开始改变
     * 注意：这三种状态都是初始化的状态
     */
    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState,
    ) {

    }

    override fun setPrimaryColors(vararg colors: Int) {}
    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {

    }

    override fun onMoving(
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        height: Int,
        maxDragHeight: Int,
    ) {

    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {

    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        return 0
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {}
    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }

}