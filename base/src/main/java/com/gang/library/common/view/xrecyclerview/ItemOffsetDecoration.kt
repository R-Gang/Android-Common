package com.gang.library.common.view.xrecyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
// 设置间距
class ItemOffsetDecoration : ItemDecoration {
    private var left: Int
    private var top: Int
    private var right: Int
    private var bottme: Int

    constructor(itemOffset: Int) {
        left = itemOffset
        top = itemOffset
        right = itemOffset
        bottme = itemOffset
    }

    constructor(leftRight: Int, topBottme: Int) {
        left = leftRight
        right = leftRight
        top = topBottme
        bottme = topBottme
    }

    constructor(left: Int, top: Int, right: Int, bottme: Int) {
        this.left = left
        this.right = right
        this.top = top
        this.bottme = bottme
    }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect[left, top, right] = bottme
    }
}