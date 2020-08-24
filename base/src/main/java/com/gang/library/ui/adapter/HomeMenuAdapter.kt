package com.gang.library.ui.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.gang.library.R
import com.gang.library.common.view.xrecyclerview.xrecycleradapter.XrecyclerAdapter
import com.gang.library.common.view.xrecyclerview.xrecycleradapter.XrecyclerViewHolder
import com.gang.library.ui.bean.HomeIcon

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
class HomeMenuAdapter(
    datas: MutableList<*>, context: Context, override val layoutResId: Int
) :
    XrecyclerAdapter(datas, context) {

    var homeIcon: HomeIcon? = null

    var ivIcon: ImageView? = null
    var tvName: TextView? = null

    override fun convert(holder: XrecyclerViewHolder, position: Int, context: Context) {
        holder.apply {
            homeIcon = datas[position] as HomeIcon

            ivIcon = holder.view.findViewById<ImageView>(R.id.iv_icon)
            tvName = holder.view.findViewById<TextView>(R.id.tv_name)

            homeIcon?.icon?.toInt()?.let { ivIcon?.setImageResource(it) }
            tvName?.text = homeIcon?.name
        }
    }
}