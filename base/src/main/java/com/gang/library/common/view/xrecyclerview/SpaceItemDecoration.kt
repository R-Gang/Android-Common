package com.gang.library.common.view.xrecyclerview

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.annotation.IntDef
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * good programmer.
 *
 * @data : 2018-01-11 下午 03:28
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : RecyclerView 设置间距
 */
class SpaceItemDecoration : ItemDecoration {
    //限定为LINEARLAYOUT,GRIDLAYOUT,STAGGEREDGRIDLAYOUT
    @IntDef(
        LINEARLAYOUT,
        GRIDLAYOUT,
        STAGGEREDGRIDLAYOUT
    ) //表示注解所存活的时间,在运行时,而不会存在. class 文件.
    @Retention(RetentionPolicy.SOURCE)
    annotation class LayoutManager(val type: Int = LINEARLAYOUT)

    private var leftRight = 0
    private var topBottom = 0
    /**
     * 头布局个数
     */
    private var headItemCount: Int
    /**
     * 边距
     */
    private var space = 0
    /**
     * 时候包含边距
     */
    private var includeEdge = false
    /**
     * 烈数
     */
    private var spanCount = 0
    @LayoutManager
    private var layoutManager: Int

    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     *
     * @param leftRight
     * @param topBottom
     * @param headItemCount
     * @param layoutManager
     */
    constructor(
        leftRight: Int,
        topBottom: Int,
        headItemCount: Int, @LayoutManager layoutManager: Int
    ) {
        this.leftRight = leftRight
        this.topBottom = topBottom
        this.headItemCount = headItemCount
        this.layoutManager = layoutManager
    }

    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     *
     * @param space
     * @param includeEdge
     * @param layoutManager
     */
    constructor(
        space: Int,
        includeEdge: Boolean, @LayoutManager layoutManager: Int
    ) : this(space, 0, includeEdge, layoutManager) {
    }

    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     *
     * @param space
     * @param headItemCount
     * @param includeEdge
     * @param layoutManager
     */
    constructor(
        space: Int,
        headItemCount: Int,
        includeEdge: Boolean, @LayoutManager layoutManager: Int
    ) {
        this.space = space
        this.headItemCount = headItemCount
        this.includeEdge = includeEdge
        this.layoutManager = layoutManager
    }

    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     *
     * @param space
     * @param headItemCount
     * @param layoutManager
     */
    constructor(
        space: Int,
        headItemCount: Int, @LayoutManager layoutManager: Int
    ) : this(space, headItemCount, true, layoutManager) {
    }

    /**
     * LinearLayoutManager or GridLayoutManager or StaggeredGridLayoutManager spacing
     *
     * @param space
     * @param layoutManager
     */
    constructor(space: Int, @LayoutManager layoutManager: Int) : this(
        space,
        0,
        true,
        layoutManager
    ) {
    }

    override fun onDraw(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.onDraw(c, parent, state)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        when (layoutManager) {
            LINEARLAYOUT -> setLinearLayoutSpaceItemDecoration(
                outRect,
                view,
                parent,
                state
            )
            GRIDLAYOUT -> {
                val gridLayoutManager =
                    parent.layoutManager as GridLayoutManager?
                //列数
                spanCount = gridLayoutManager!!.spanCount
                setNGridLayoutSpaceItemDecoration(outRect, view, parent, state)
            }
            STAGGEREDGRIDLAYOUT -> {
                val staggeredGridLayoutManager =
                    parent.layoutManager as StaggeredGridLayoutManager?
                //列数
                spanCount = staggeredGridLayoutManager!!.spanCount
                setNGridLayoutSpaceItemDecoration(outRect, view, parent, state)
            }
            else -> {
            }
        }
    }

    /**
     * LinearLayoutManager spacing
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    private fun setLinearLayoutSpaceItemDecoration(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = space
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space
        } else {
            outRect.top = 0
        }
    }

    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    private fun setNGridLayoutSpaceItemDecoration(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) - headItemCount
        if (headItemCount != 0 && position == -headItemCount) {
            return
        }
        val column = position % spanCount
        if (includeEdge) {
            outRect.left = space - column * space / spanCount
            outRect.right = (column + 1) * space / spanCount
            if (position < spanCount) {
                outRect.top = space
            }
            outRect.bottom = space
        } else {
            outRect.left = column * space / spanCount
            outRect.right = space - (column + 1) * space / spanCount
            if (position >= spanCount) {
                outRect.top = space
            }
        }
    }

    /**
     * GridLayoutManager设置间距（此方法最左边和最右边间距为设置的一半）
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    private fun setGridLayoutSpaceItemDecoration(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager as GridLayoutManager?
        //判断总的数量是否可以整除
        val totalCount = layoutManager!!.itemCount
        val surplusCount = totalCount % layoutManager.spanCount
        val childPosition = parent.getChildAdapterPosition(view)
        //竖直方向的
        if (layoutManager.orientation == GridLayoutManager.VERTICAL) {
            if (surplusCount == 0 && childPosition > totalCount - layoutManager.spanCount - 1) { //后面几项需要bottom
                outRect.bottom = topBottom
            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
                outRect.bottom = topBottom
            }
            //被整除的需要右边
            if ((childPosition + 1 - headItemCount) % layoutManager.spanCount == 0) { //加了右边后最后一列的图就非宽度少一个右边距
//outRect.right = leftRight;
            }
            outRect.top = topBottom
            outRect.left = leftRight / 2
            outRect.right = leftRight / 2
        } else {
            if (surplusCount == 0 && childPosition > totalCount - layoutManager.spanCount - 1) { //后面几项需要右边
                outRect.right = leftRight
            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
                outRect.right = leftRight
            }
            //被整除的需要下边
            if ((childPosition + 1) % layoutManager.spanCount == 0) {
                outRect.bottom = topBottom
            }
            outRect.top = topBottom
            outRect.left = leftRight
        }
    }

    companion object {
        const val LINEARLAYOUT = 0
        const val GRIDLAYOUT = 1
        const val STAGGEREDGRIDLAYOUT = 2
    }
}