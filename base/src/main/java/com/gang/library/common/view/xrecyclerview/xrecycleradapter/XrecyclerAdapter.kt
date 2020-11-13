package com.gang.library.common.view.xrecyclerview.xrecycleradapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.gang.library.common.view.xrecyclerview.onitemclick.ViewOnItemClick
import com.gang.library.common.view.xrecyclerview.onitemclick.ViewOnItemLongClick

abstract class XrecyclerAdapter : RecyclerView.Adapter<XrecyclerViewHolder> {

    var datas = mutableListOf<Any>()
    var onItemClick: ViewOnItemClick? = null
    var longClick: ViewOnItemLongClick? = null
    private var mObject: Any? = null
    lateinit var mContext: Context

    constructor(`object`: Any) {
        instanceofObj(`object`)
    }

    //为butter准备的构造
    constructor(datas: MutableList<*>, `object`: Any) {
        this.datas.clear()
        this.datas.addAll(datas as ArrayList<*>)
        instanceofObj(`object`)
    }

    constructor(
        datas: MutableList<*>,
        `object`: Any,
        onItemClick1: ViewOnItemClick,
        onItemLongClick: ViewOnItemLongClick
    ) {
        this.datas.clear()
        this.datas.addAll(datas as ArrayList<*>)
        instanceofObj(`object`)
        onItemClick = onItemClick1
        longClick = onItemLongClick
    }

    constructor(
        datas: MutableList<*>,
        `object`: Any,
        onItemClick1: ViewOnItemClick
    ) {
        this.datas.clear()
        this.datas.addAll(datas as ArrayList<*>)
        instanceofObj(`object`)
        onItemClick = onItemClick1
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun instanceofObj(`object`: Any) {
        mObject = `object`
        when (`object`) {
            is Fragment -> {
                mContext = `object`.context!!
            }
            is Activity -> {
                mContext = `object`
            }
            is View -> {
                mContext = `object`.context
            }
        }
    }

    //创建新View，被LayoutManager所调用
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): XrecyclerViewHolder {
        val res: Int? = layoutResId
        val view = res?.let { LayoutInflater.from(viewGroup.context).inflate(it, viewGroup, false) }
        return view?.let { XrecyclerViewHolder(it, onItemClick, longClick) }!!
    }

    //将数据与界面进行绑定的操作
    override fun onBindViewHolder(
        viewHolder: XrecyclerViewHolder,
        position: Int
    ) {
        convert(viewHolder, position, mContext)
    }

    abstract fun convert(
        holder: XrecyclerViewHolder,
        position: Int,
        context: Context
    )

    abstract val layoutResId: Int

    //获取数据的数量
    override fun getItemCount(): Int {
        return if (datas.isNullOrEmpty()) 0 else datas.size
    }

    open fun update(pdata: List<*>?) {
        this.datas.clear()
        this.datas.addAll(pdata as ArrayList<*>)
        notifyDataSetChanged()
    }

}