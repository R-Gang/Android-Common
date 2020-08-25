package com.gang.app.ui.adapter

import android.content.Context
import com.gang.library.common.view.xrecyclerview.xrecycleradapter.XrecyclerAdapter
import com.gang.library.common.view.xrecyclerview.xrecycleradapter.XrecyclerViewHolder
import com.gang.app.ui.bean.HomeIcon
import kotlinx.android.synthetic.main.item_home_menu.view.*

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

    override fun convert(holder: XrecyclerViewHolder, position: Int, context: Context) {
        holder.view.apply {
            homeIcon = datas[position] as HomeIcon

            homeIcon?.icon?.toInt()?.let { iv_icon?.setImageResource(it) }
            tv_name?.text = homeIcon?.name
        }
    }
}