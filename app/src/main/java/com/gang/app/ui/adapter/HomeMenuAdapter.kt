package com.gang.app.ui.adapter

import android.content.Context
import android.media.Image
import android.widget.ImageView
import android.widget.TextView
import com.gang.app.R
import com.gang.app.ui.bean.HomeIcon
import com.gang.recycler.kotlin.interfaces.ViewOnItemClick
import com.gang.recycler.kotlin.recycleradapter.RecyclerAdapter
import com.gang.recycler.kotlin.recycleradapter.RecyclerViewHolder

/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.app.ui.adapter
 * @ClassName:      HomeMenuAdapter
 * @Description:     java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     2020/8/5 16:00
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/5 16:00
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */

/**
 * 适配器基础使用方式
 */
class HomeMenuAdapter(
    datas: MutableList<*>, `object`: Any, onItemClick1: ViewOnItemClick,
    override val layoutResId: Int,
) :
    RecyclerAdapter(datas, `object`, onItemClick1) {

    var homeIcon: HomeIcon? = null

    override fun convert(holder: RecyclerViewHolder, position: Int, context: Context) {
        holder.view.apply {
            homeIcon = datas[position] as HomeIcon

            homeIcon?.icon?.toInt()?.let { findViewById<ImageView>(R.id.iv_icon)?.setImageResource(it) }
            findViewById<TextView>(R.id.tv_name)?.text = homeIcon?.name
        }
    }
}