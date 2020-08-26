package com.gang.library.common.view.smartrefresh

/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.app.ui.interfaces
 * @ClassName:      CallBack
 * @Description:    java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     2020/8/10 18:38
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/10 18:38
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
interface RefreshCallBack {

    fun finishSuccessRefresh()

    fun finishSuccessLoadmore()

    fun finishFailRefresh()

    fun finishFailLoadmore()

}

open class SmartRefresh :
    RefreshCallBack {
    override fun finishSuccessRefresh() {

    }

    override fun finishSuccessLoadmore() {
    }

    override fun finishFailRefresh() {
    }

    override fun finishFailLoadmore() {
    }

}