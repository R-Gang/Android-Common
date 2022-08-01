package com.gang.app.common.view.smartrefresh.callback

import com.gang.app.common.view.smartrefresh.callback.SmartRefreshCallBack
import com.scwang.smart.refresh.layout.SmartRefreshLayout


/**
 * @ClassName:      RefreshLayout
 * @Description:     java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     2020/8/11 10:28
 */
class RefreshLayout(private val refreshLayout: SmartRefreshLayout) : SmartRefreshCallBack {

    override fun finishSuccessRefresh() {
        refreshLayout.finishRefresh()
    }

    override fun finishSuccessLoadmore() {
        refreshLayout.finishLoadMore()
    }

    override fun finishFailRefresh() {
        refreshLayout.finishRefresh(false)
    }

    override fun finishFailLoadmore() {
        refreshLayout.finishLoadMore(false)
    }

}