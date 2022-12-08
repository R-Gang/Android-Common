package com.gang.app.ui.fragment

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.gang.app.R
import com.gang.library.base.BaseFragment
import com.gang.library.common.Permission.requestPermission
import com.gang.library.common.Permission.toGetRead_Write
import com.gang.library.ui.activity.WebViewActivity
import com.gang.recycler.kotlin.viewpager.VPFragmentAdapter
import com.gang.tools.kotlin.dimension.statusBarHeight
import com.gang.tools.kotlin.utils.vClick
import com.google.android.material.tabs.TabLayout

/**
 * 我的
 * Created by haoruigang on 2018/4/23 14:22.
 *
 * 1.WebView工具使用方式示例
 */
class MyFragment : BaseFragment() {

    private var my_toolbar: Toolbar? = null
    private var tabs_viewpager: ViewPager? = null

    var fragmentList = arrayListOf<Fragment>()
    var mTitleList = arrayListOf("Tab1", "Tab2")

    override val layoutId: Int = R.layout.fragment_my

    override fun initView(savedInstanceState: Bundle?) {
        /*(activity as MainActivity).setSupportActionBar(my_toolbar)
        if (activity is MainActivity) {
            (activity as MainActivity).dark()
        }*/
        my_toolbar = findViewId<Toolbar>(R.id.my_toolbar)
        val params = my_toolbar?.layoutParams as FrameLayout.LayoutParams
        params.height += statusBarHeight
        my_toolbar?.layoutParams = params


        val bundle = Bundle()
        fragmentList.add(ItemFragment.newInstance(bundle))
        fragmentList.add(HomeFragment())
        val fragmentAdapter = VPFragmentAdapter(childFragmentManager, fragmentList, mTitleList)
        tabs_viewpager = findViewId<ViewPager>(R.id.tabs_viewpager)
        tabs_viewpager?.adapter = fragmentAdapter
        findViewId<TabLayout>(R.id.tabs).setupWithViewPager(tabs_viewpager, false)


        val gugongTitle = "故宫小店"
        val gugongShop = "https://shop90820925.m.youzan.com/v2/showcase/homepage?kdt_id=90628757"
        findViewId<ImageView>(R.id.ivAvater).vClick {
            WebViewActivity.actionStart(this.requireActivity(), gugongShop, gugongTitle)
        }
        findViewId<ImageView>(R.id.tvTitle).vClick {
            mActivity.toGetRead_Write(
                "http://dtimemini.1bu2bu.com/doc",
                //            FileUtils.getAssetFile("pullWeiXin.html"),
                gugongTitle
            )

            requestPermission(
                mContext = mActivity,
                permissions = arrayOf())
        }
    }

    override fun initData() {

    }
}