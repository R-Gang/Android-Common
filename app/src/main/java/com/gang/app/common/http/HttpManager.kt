package com.gang.app.common.http

import com.gang.app.common.user.Constants
import com.gang.library.common.http.OkHttpUtils
import com.gang.library.common.http.callback.HttpCallBack
import java.util.*

/**
 * 作者： haoruigang on 2017-11-28 11:14:10.
 * 类描述：网络帮助类(主要用来管理参数)
 */
class HttpManager {
    /**
     * haoruigang on 2018-3-30 10:30:01
     * 描述:获取验证码
     *
     * @param tag
     * @param phoneNum
     * @param callBack
     */
    fun doRandomCode(tag: String?, phoneNum: String, callBack: HttpCallBack<*>) {
        val map = HashMap<String, String>()
        map["mobile"] = phoneNum
        OkHttpUtils.instance.getOkHttpJsonRequest(tag, Constants.GET_RANDOM_CODE, map, callBack)
    }

    private object SingletonHolder {
        var INSTANCE: HttpManager = HttpManager()
    }

    companion object {
        val instance: HttpManager
            get() = SingletonHolder.INSTANCE
    }
}