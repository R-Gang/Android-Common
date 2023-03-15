package com.gang.app.common.user

/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.app.common.user
 * @ClassName:      ToUIEvent
 * @Description:    通知管理类
 * @Author:         haoruigang
 * @CreateDate:     2020/8/3 16:17
 */
class ToUIEvent : com.gang.library.common.user.ToUIEvent {

    companion object {
        val MESSAGE_EVENT = 0 //消息刷新
        val TAbBAR_EVENT = 1 //按钮切换
    }


    var tag = 0
    var obj: Any? = null

    constructor()

    constructor(tag: Int) {
        this.tag = tag
    }

    constructor(tag: Int, obj: Any?) {
        this.tag = tag
        this.obj = obj
    }

}