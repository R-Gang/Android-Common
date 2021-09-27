package com.gang.library.common.view.manager

import android.content.Context
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gang.library.R
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView

/**
 * Date: 2019/4/10
 * Author: haoruigang
 * Description: xrecyclerview 管理
 */
class LayoutManager {

    private var mContext: Context? = null

    fun init(context: Context?): LayoutManager {
        mContext = context
        return this
    }

    fun initRecyclerView(recyclerView: RecyclerView) {
        initRecyclerView(recyclerView, true)
    }

    fun initRecyclerView(recyclerView: RecyclerView,isVertical: Boolean): LinearLayoutManager {
        val manager = LinearLayoutManager(mContext)
        manager.orientation =
            if (isVertical) RecyclerView.VERTICAL else LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = manager
        return manager
    }

    fun initRecyclerGrid(recyclerView: RecyclerView,span: Int) {
        initRecyclerGrid(recyclerView, span, true)
    }

    fun initRecyclerGrid(recyclerView: RecyclerView,span: Int,isVertical: Boolean): GridLayoutManager {
        val manager = GridLayoutManager(mContext, span)
        manager.orientation =
            if (isVertical) RecyclerView.VERTICAL else LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = manager
        return manager
    }

    fun initXrecyclerView(xRecyclerView: XRecyclerView) {
        initXrecyclerView(xRecyclerView, true)
    }

    //    初始化 RecyclerView的配置
    fun initXrecyclerView(xRecyclerView: XRecyclerView, isVertical: Boolean): LinearLayoutManager {
        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation =
            if (isVertical) RecyclerView.VERTICAL else LinearLayoutManager.HORIZONTAL
        xRecyclerView.layoutManager = layoutManager
        // xRecyclerView.setRefreshHeader(new CustomArrowHeader(this));
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader)
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman)
        xRecyclerView.setArrowImageView(R.mipmap.ic_recycler_view_arrow)
        xRecyclerView.setPullRefreshEnabled(false)
        xRecyclerView.setLoadingMoreEnabled(false)
        xRecyclerView.defaultRefreshHeaderView.setRefreshTimeVisible(true)
        xRecyclerView.itemAnimator = DefaultItemAnimator() //设置Item增加、移除动画
        return layoutManager
    }


    fun initXrecyclerGrid(xRecyclerView: XRecyclerView, span: Int) {
        initXrecyclerGrid(xRecyclerView, span, true)
    }

    fun initXrecyclerGrid(xRecyclerView: XRecyclerView, span: Int, isVertical: Boolean) {
        val manager = GridLayoutManager(mContext, span)
        manager.orientation =
            if (isVertical) RecyclerView.VERTICAL else LinearLayoutManager.HORIZONTAL
        xRecyclerView.layoutManager = manager
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader)
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman)
        xRecyclerView.setArrowImageView(R.mipmap.ic_recycler_view_arrow)
        xRecyclerView.setPullRefreshEnabled(false)
        xRecyclerView.setLoadingMoreEnabled(false)
        xRecyclerView.defaultRefreshHeaderView.setRefreshTimeVisible(true)
        xRecyclerView.itemAnimator = DefaultItemAnimator() //设置Item增加、移除动画
    }

    /**
     * 流式布局recyclerView
     */
    fun initFlexbox(recyclerView: RecyclerView) {
        val layoutManager = FlexboxLayoutManager(mContext)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        layoutManager.flexWrap = FlexWrap.WRAP
        recyclerView.layoutManager = layoutManager
    }

    companion object {
        private var mInstance: LayoutManager? = null

        /**
         * 获取LayoutManager实例
         */
        @get:Synchronized
        val instance: LayoutManager?
            get() {
                if (null == mInstance) {
                    mInstance =
                        LayoutManager()
                }
                return mInstance
            }
    }
}