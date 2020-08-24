package com.gang.library.common.view.xrecyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.GridLayoutAnimationController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView with support for grid animations.
 *
 * 弹出带动画
 * Based on: 2018-7-30 16:33:20 haoruigang
 */
class GridRecyclerView : RecyclerView {
    /**
     * @see View.View
     */
    constructor(context: Context?) : super(context!!) {}

    /**
     * @see View.View
     */
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {
    }

    /**
     * @see View.View
     */
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context!!, attrs, defStyle) {
    }

    override fun attachLayoutAnimationParameters(
        child: View, params: ViewGroup.LayoutParams,
        index: Int, count: Int
    ) {
        val layoutManager = layoutManager
        if (adapter != null && layoutManager is GridLayoutManager) {
            var animationParams =
                params.layoutAnimationParameters as GridLayoutAnimationController.AnimationParameters
            if (animationParams == null) {
                animationParams = GridLayoutAnimationController.AnimationParameters()
                params.layoutAnimationParameters = animationParams
            }
            val columns = layoutManager.spanCount
            animationParams.count = count
            animationParams.index = index
            animationParams.columnsCount = columns
            animationParams.rowsCount = count / columns
            val invertedIndex = count - 1 - index
            animationParams.column = columns - 1 - invertedIndex % columns
            animationParams.row = animationParams.rowsCount - 1 - invertedIndex / columns
        } else {
            super.attachLayoutAnimationParameters(child, params, index, count)
        }
    }
}