package com.gang.app.ui.fragment

import android.os.Bundle
import android.view.View
import com.gang.app.R
import com.gang.app.ui.adapter.HomeMenuAdapter
import com.gang.app.ui.bean.HomeIcon
import com.gang.library.common.view.manager.LayoutManager
import com.gang.library.ui.fragment.BaseFragment
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * 首页
 * create by haoruigang on 2018-4-23 10:43:03
 */
class HomeFragment : BaseFragment() {

    private var homeMenu = arrayListOf<HomeIcon>()

    override val layoutId: Int = R.layout.fragment_home

    override fun initView(view: View?, savedInstanceState: Bundle?) {

        refresh_layout.refreshHeader = ClassicsHeader(mActivity)
        refresh_layout.setOnRefreshListener {}
    }

    override fun initData() {
        homeMenu.add(HomeIcon(0, R.mipmap.cate1, "企业简介"))
        homeMenu.add(HomeIcon(1, R.mipmap.cate2, "业务板块"))
        homeMenu.add(HomeIcon(2, R.mipmap.cate3, "产品中心"))
        homeMenu.add(HomeIcon(3, R.mipmap.cate4, "营销网络"))
        LayoutManager.instance?.init(activity)?.initRecyclerGrid(recyclerView, 4)
        recyclerView.adapter = HomeMenuAdapter(
            homeMenu,
            mActivity,
            R.layout.item_home_menu
        )
    }

}