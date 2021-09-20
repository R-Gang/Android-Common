package com.gang.library.common.view.smartrefresh

import com.scwang.smart.refresh.layout.SmartRefreshLayout


/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.app.ui.interfaces
 * @ClassName:      RefreshLayout
 * @Description:     java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     2020/8/11 10:28
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/11 10:28
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class RefreshLayout(private val refreshLayout: SmartRefreshLayout) : SmartRefresh() {

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