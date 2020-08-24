package com.gang.library.ui.fragment

import android.os.Bundle
import android.view.View
import com.gang.library.R
import com.gang.library.common.view.xrecyclerview.LayoutManager
import com.gang.library.ui.adapter.HomeMenuAdapter
import com.gang.library.ui.bean.HomeIcon
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * 首页
 * create by haoruigang on 2018-4-23 10:43:03
 */
class HomeFragment : BaseFragment() {

    var homeMenu = arrayListOf<HomeIcon>()

    override val layoutId: Int = R.layout.fragment_home

    override fun initView(view: View?, savedInstanceState: Bundle?) {

    }

    override fun initData() {

        homeMenu.add(HomeIcon(0, R.mipmap.cate1, "企业简介"))
        homeMenu.add(HomeIcon(1, R.mipmap.cate2, "业务板块"))
        homeMenu.add(HomeIcon(2, R.mipmap.cate3, "产品中心"))
        homeMenu.add(HomeIcon(3, R.mipmap.cate4, "营销网络"))
        LayoutManager.instance?.init(activity)?.initRecyclerGrid(recyclerView, 4)
        recyclerView.adapter =
            activity?.let { HomeMenuAdapter(homeMenu, it, R.layout.item_home_menu) }
    }

}