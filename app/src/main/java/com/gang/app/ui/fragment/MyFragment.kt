package com.gang.app.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gang.app.R
import com.gang.library.common.http.progress.MyProgressDialog
import com.gang.library.common.utils.SysUtils
import com.gang.library.ui.adapter.VPFragmentAdapter
import com.gang.library.ui.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_my.*

/**
 * 我的
 * Created by haoruigang on 2018/4/23 14:22.
 *
 * 1.WebView工具使用方式示例
 */
class MyFragment : BaseFragment() {

    var fragmentList = arrayListOf<Fragment>()
    var mTitleList = arrayListOf<String>("Tab1", "Tab2")

    override val layoutId: Int = R.layout.fragment_my

    override fun initView(
        view: View?,
        savedInstanceState: Bundle?,
    ) {
//        (activity as MainActivity).setSupportActionBar(my_toolbar)
//        if (activity is MainActivity) {
//            (activity as MainActivity).dark()
//        }


        val itemFragment = ItemFragment()
        val bundle = Bundle()
        itemFragment.newInstance().arguments = bundle
        fragmentList.add(itemFragment)
        fragmentList.add(HomeFragment())
        val fragmentAdapter = VPFragmentAdapter(childFragmentManager, fragmentList, mTitleList)
        tabs_viewpager.adapter = fragmentAdapter
        tabs.setupWithViewPager(tabs_viewpager, false)


        MyProgressDialog(mActivity).show()
    }

    override fun initData() {
        SysUtils.actionStartWeb(
            mActivity,
            "https://shop90820925.m.youzan.com/v2/showcase/homepage?kdt_id=90628757",
//            FileUtils.getAssetFile("pullWeiXin.html"),
//            "https://applinks.dpm.org.cn/pullWeiXin.html",
            "故宫小店"
        )
    }
}