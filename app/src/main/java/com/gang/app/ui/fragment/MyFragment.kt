package com.gang.app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gang.app.R
import com.gang.library.base.BaseFragment
import com.gang.library.common.Permission.toGetRead_Write
import com.gang.library.ui.activity.WebViewActivity
import com.gang.recycler.kotlin.viewpager.VPFragmentAdapter
import com.gang.tools.kotlin.dimension.statusBarHeight
import com.gang.tools.kotlin.utils.vClick
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.android.synthetic.main.fragment_my.*

/**
 * 我的
 * Created by haoruigang on 2018/4/23 14:22.
 *
 * 1.WebView工具使用方式示例
 */
class MyFragment : BaseFragment() {

    var fragmentList = arrayListOf<Fragment>()
    var mTitleList = arrayListOf("Tab1", "Tab2")

    override val layoutId: Int = R.layout.fragment_my

    override fun initView(savedInstanceState: Bundle?) {
        /*(activity as MainActivity).setSupportActionBar(my_toolbar)
        if (activity is MainActivity) {
            (activity as MainActivity).dark()
        }*/
        val params = my_toolbar.layoutParams as CollapsingToolbarLayout.LayoutParams
        params.height += statusBarHeight
        my_toolbar.layoutParams = params


        val bundle = Bundle()
        fragmentList.add(ItemFragment.newInstance(bundle))
        fragmentList.add(HomeFragment())
        val fragmentAdapter = VPFragmentAdapter(childFragmentManager, fragmentList, mTitleList)
        tabs_viewpager.adapter = fragmentAdapter
        tabs.setupWithViewPager(tabs_viewpager, false)


        val gugongTitle = "故宫小店"
        val gugongShop = "https://shop90820925.m.youzan.com/v2/showcase/homepage?kdt_id=90628757"
        ivAvater.vClick {
            WebViewActivity.actionStart(this.requireActivity(), gugongShop, gugongTitle)
        }
        tvTitle.vClick {
            mActivity.toGetRead_Write("http://dtimemini.1bu2bu.com/doc",
                //            FileUtils.getAssetFile("pullWeiXin.html"),
                gugongTitle
            )
        }
    }

    override fun initData() {

    }
}