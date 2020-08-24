package com.gang.library.common.view.xrecyclerview.xrecycleradapter

import android.view.View
import android.view.View.OnLongClickListener
import androidx.recyclerview.widget.RecyclerView
import com.gang.library.common.view.xrecyclerview.onitemclick.ViewOnItemClick
import com.gang.library.common.view.xrecyclerview.onitemclick.ViewOnItemLongClick

/**
 * Created by 1bu2bu-4 on 2016/9/1.
 */
class XrecyclerViewHolder(
    var view: View,
    onItemClick: ViewOnItemClick?,
    longClick: ViewOnItemLongClick?
) : RecyclerView.ViewHolder(view), View.OnClickListener, OnLongClickListener {
    var onItemClick: ViewOnItemClick? = onItemClick
    var longClick: ViewOnItemLongClick? = longClick
    override fun onClick(v: View) {
        onItemClick?.setOnItemClickListener(v, position)
    }

    override fun onLongClick(v: View): Boolean {
        longClick?.setOnItemLongClickListener(v, position)
        return true
    }

    fun getView(id: Int): View {
        return view.findViewById(id)
    }
    fun <T:View>getView2(id: Int): T {
        return view.findViewById(id) as T
    }

    init {
        view.setOnClickListener(this)
        view.setOnLongClickListener(this)
    }
}