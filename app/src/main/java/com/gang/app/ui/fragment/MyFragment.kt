package com.gang.app.ui.fragment

import android.os.Bundle
import android.view.View
import com.gang.app.R
import com.gang.library.common.utils.FileUtils
import com.gang.library.common.utils.SysUtils
import com.gang.library.ui.fragment.BaseFragment

/**
 * 我的
 * Created by haoruigang on 2018/4/23 14:22.
 *
 * 1.WebView工具使用方式示例
 */
class MyFragment : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_my

    override fun initView(
        view: View?,
        savedInstanceState: Bundle?
    ) {

    }

    override fun initData() {
//        SysUtils.actionStartWeb(
//            mActivity,
//            "https://shop90820925.m.youzan.com/v2/showcase/homepage?kdt_id=90628757",
////            FileUtils.getAssetFile("pullWeiXin.html"),
////            "https://applinks.dpm.org.cn/pullWeiXin.html",
//            "故宫小店"
//        )
    }
}