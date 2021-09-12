package com.gang.app.common.http

import com.gang.app.common.user.Constants
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
    fun doRandomCode(
        tag: String?,
        phoneNum: String,
        phoneMd5Sum: String,
        callBack: ApiCallBack<*>
    ) {
        val map = HashMap<String, String>()
        map["phone"] = phoneNum
        map["phoneMd5Sum"] = phoneMd5Sum
        Api.instance.postOkHttpJsonRequest(tag, Constants.SENDVCODE, map, callBack)
    }

    /**
     * 客户端基本配置
     */
    fun clientConfig(tag: String?, callBack: ApiCallBack<*>) {
        val map = HashMap<String, String>()
        Api.instance.getOkHttpJsonRequest(tag, Constants.CLIENT_CONFIG, map, callBack)
    }


    private object SingletonHolder {
        var INSTANCE: HttpManager = HttpManager()
    }

    companion object {
        val instance: HttpManager
            get() = SingletonHolder.INSTANCE
    }
}