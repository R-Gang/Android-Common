package com.gang.library.common

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 *
 * @ProjectName:    Android-Common
 * @Package:        com.gang.library.common
 * @ClassName:      EventBus
 * @Description:    EventBus
 * @Author:         haoruigang
 * @CreateDate:     2020/8/25 19:00
 */
object EventBus {

    // EventBus 被注册
    fun isRegistered(subscriber: Any): Boolean {
        return EventBus.getDefault().isRegistered(subscriber)
    }

    // 注册EventBus
    fun register(subscriber: Any) {
        EventBus.getDefault().register(subscriber)
    }

    // 反注册EventBus
    fun unregister(subscriber: Any) {
        EventBus.getDefault().unregister(subscriber)
    }

    /** Posts the given event to the event bus. */
    fun post(objects: Any) {
        EventBus.getDefault().post(objects)
    }

    fun postSticky(objects: Any) {
        EventBus.getDefault().postSticky(objects)
    }

    //Eventbus
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    open fun onEvent(objects: Objects) {

    }

}