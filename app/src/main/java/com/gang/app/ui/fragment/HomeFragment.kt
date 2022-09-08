package com.gang.app.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.gang.app.R
import com.gang.app.ui.activity.HttpApiActivity
import com.gang.app.ui.activity.PickerActivity
import com.gang.app.ui.activity.SpannableActivity
import com.gang.app.ui.adapter.HomeMenuAdapter
import com.gang.app.ui.bean.HomeIcon
import com.gang.library.base.BaseFragment
import com.gang.library.ui.widget.BaseTitleBar
import com.gang.library.ui.widget.ColorWheel
import com.gang.recycler.kotlin.interfaces.ViewOnItemClick
import com.gang.recycler.kotlin.manager.LayoutManager
import com.gang.tools.kotlin.dimension.px2dip
import com.gang.tools.kotlin.dimension.px2sp
import com.gang.tools.kotlin.dimension.statusBarHeight
import com.gang.tools.kotlin.utils.toActivityAnimation
import com.orhanobut.logger.Logger
import com.scwang.smart.refresh.header.ClassicsHeader
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * 首页
 * create by haoruigang on 2018-4-23 10:43:03
 *
 * 1.RecyclerView基本使用方式
 */
class HomeFragment : BaseFragment(), ViewOnItemClick {

    private var homeMenu = arrayListOf<HomeIcon>()

    override val layoutId: Int = R.layout.fragment_home

    override fun initView(savedInstanceState: Bundle?) {

        // 目前kotlin-android-extensions暂时还不支持跨模块
        view?.findViewById<BaseTitleBar>(R.id.myBoolbar)?.apply {
            goneLeftView()
            goneLine()
            setTitle(resources.getString(R.string.app_title),
                R.color.black,
                textSize = px2sp(54f),
                style = Typeface.BOLD)
            setLLEmtity(statusBarHeight)
        }

        refresh_layout.setRefreshHeader(ClassicsHeader(activity))
        refresh_layout.setOnRefreshListener {
            refresh_layout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
        }

    }

    override fun initData() {
        homeMenu.add(HomeIcon(0, R.mipmap.cate1, "企业简介"))
        homeMenu.add(HomeIcon(1, R.mipmap.cate2, "业务板块"))
        homeMenu.add(HomeIcon(2, R.mipmap.cate3, "产品中心"))
        homeMenu.add(HomeIcon(3, R.mipmap.cate4, "营销网络"))
        LayoutManager.instance?.initRecyclerGrid(recyclerView, 4)
        recyclerView.adapter = HomeMenuAdapter(
            homeMenu,
            mActivity, this,
            R.layout.item_home_menu
        )

        //        监听事件
        color_wheel.setOnColorChangedListener(object : ColorWheel.OnColorChangedListener {
            override fun onColorChange(a: Int, r: Int, g: Int, b: Int) {
//                binding.colorBrightView.setProgressColor(Color.argb(a, r, g, b))
                Logger.d(Color.argb(a, r, g, b))
            }

            override fun onColorPick(a: Int, r: Int, g: Int, b: Int) {

                // 将 rgb 转换成 hsv，s 即为饱和度  色调（H），饱和度（S），明度（V）
                val c = Color.argb(a, r, g, b)
                val hsv = FloatArray(3)
                Color.colorToHSV(c, hsv)

                Logger.d(Color.argb(a, r, g, b))
            }
        })
    }

    override fun setOnItemClickListener(view: View?, position: Int) {
        when (position) {
            0 -> {
                startActivity(Intent(mActivity, HttpApiActivity::class.java))
            }
            1 -> {
                startActivity(Intent(mActivity, PickerActivity::class.java))
            }
            2 -> {
                // 带动画跳转
                val ivIcon =
                    android.util.Pair.create(view?.findViewById<ImageView>(R.id.iv_icon) as View,
                        "ivIcon")
                val tvName =
                    android.util.Pair.create(view.findViewById<ImageView>(R.id.tv_name) as View,
                        "tvName")
                activity?.toActivityAnimation(Intent(mActivity, SpannableActivity::class.java),
                    ivIcon,
                    tvName)
            }
            3 -> {
            }
        }
    }


}