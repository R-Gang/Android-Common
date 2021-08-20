package com.gang.library.common.view.manager

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import com.gang.library.common.view.srecyclerview.OnPageChangeListener

/**
 * LayoutManager 一次只能滑一页
 */
class PagerLayoutManager internal constructor(context: Context?) : LinearLayoutManager(context),
    OnChildAttachStateChangeListener {
    val mPagerSnapHelper: PagerSnapHelper = PagerSnapHelper()
    var mOnPageChangeListener: OnPageChangeListener? = null
    var currentPostion = 0
    var haveSelect = false

    override fun onAttachedToWindow(view: RecyclerView) {
        view.addOnChildAttachStateChangeListener(this)
        mPagerSnapHelper.attachToRecyclerView(view)
        super.onAttachedToWindow(view)
    }

    override fun onChildViewAttachedToWindow(view: View) {
        if (!haveSelect) {
            haveSelect = true
            currentPostion = getPosition(view)
            mOnPageChangeListener!!.onPageSelected(currentPostion, view)
        }
    }

    override fun onChildViewDetachedFromWindow(view: View) {}
    override fun onScrollStateChanged(state: Int) {
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            val view = mPagerSnapHelper.findSnapView(this)
            if (view != null && mOnPageChangeListener != null) {
                val position = getPosition(view)
                if (currentPostion != position) {
                    currentPostion = position
                    mOnPageChangeListener!!.onPageSelected(currentPostion, view)
                }
            }
        }
        super.onScrollStateChanged(state)
    }

    override fun canScrollVertically(): Boolean {
        return true
    }

    fun setOnPageChangeListener(mOnPageChangeListener: OnPageChangeListener?) {
        this.mOnPageChangeListener = mOnPageChangeListener
    }

}