package com.gang.library.common.view.xrecyclerview

import android.content.Context
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gang.library.R
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView

/**
 * Date: 2019/4/10
 * Author: haoruigang
 * Description: com.zhuangxiuweibao.app.common.view.xrecyclerview
 */
class LayoutManager {

    private var mContext: Context? = null

    fun init(context: Context?): LayoutManager {
        mContext = context
        return this
    }

    fun initRecyclerView(
        recyclerView: RecyclerView,
        isVertical: Boolean
    ): LinearLayoutManager {
        val manager = LinearLayoutManager(mContext)
        manager.orientation =
            if (isVertical) RecyclerView.VERTICAL else LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = manager
        return manager
    }

    fun initRecyclerGrid(recyclerView: RecyclerView, span: Int) {
        val manager = GridLayoutManager(mContext, span)
        manager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = manager
    }

    //    初始化 RecyclerView的配置
    fun iniXrecyclerView(xRecyclerView: XRecyclerView): LinearLayoutManager {
        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = RecyclerView.VERTICAL
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

    fun iniXrecyclerGrid(xRecyclerView: XRecyclerView, span: Int) {
        val manager = GridLayoutManager(mContext, span)
        manager.orientation = RecyclerView.VERTICAL
        xRecyclerView.layoutManager = manager
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader)
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman)
        xRecyclerView.setArrowImageView(R.mipmap.ic_recycler_view_arrow)
        xRecyclerView.setPullRefreshEnabled(false)
        xRecyclerView.setLoadingMoreEnabled(false)
        xRecyclerView.defaultRefreshHeaderView.setRefreshTimeVisible(true)
        xRecyclerView.itemAnimator = DefaultItemAnimator() //设置Item增加、移除动画
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
                    mInstance = LayoutManager()
                }
                return mInstance
            }
    }
}