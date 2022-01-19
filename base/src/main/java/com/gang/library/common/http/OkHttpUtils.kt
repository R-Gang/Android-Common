package com.gang.library.common.http

import com.apkfuns.logutils.LogUtils
import com.gang.library.common.utils.getPreferences
import com.gang.library.common.utils.transMap2String
import com.lzy.okhttputils.OkHttpUtils
import com.lzy.okhttputils.callback.AbsCallback
import com.lzy.okhttputils.model.HttpHeaders
import com.lzy.okhttputils.model.HttpParams

/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.library.common.http
 * @ClassName:      OkHttpUtils
 * @Description:     java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     2020/8/3 16:05
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/3 16:05
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class OkHttpUtils {


    /**
     * 参数调用
     * get
     * @param url
     * @param map
     * @param callBack
     */
    fun getOkHttpJsonRequest(
        tag: String?,
        url: String,
        map: Map<String, String>?,
        callBack: AbsCallback<*>?,
    ) {
        getHeaderJsonRequest(tag, url, map as HashMap<String, String>?, null, null, callBack)
    }

    /**
     * 参数调用
     * post
     * @param url
     * @param map
     * @param callBack
     */
    fun postOkHttpJsonRequest(
        tag: String?,
        url: String,
        map: Map<String, String>?,
        callBack: AbsCallback<*>?,
    ) {
        postHeaderJsonRequest(tag, url, map as HashMap<String, String>?, null, null, callBack)
    }

    /**
     * 参数调用
     * get
     * @param url
     * @param map
     * @param callBack
     */
    fun getHeaderJsonRequest(
        tag: String?,
        url: String,
        map: HashMap<String, String>?,
        headers: HttpHeaders?,
        httpParams: HttpParams?,
        callBack: AbsCallback<*>?,
    ) {
        if (map != null) {
            LogUtils.e(tag + "get请求:$url,参数---${transMap2String(map)}")

            val params = HashMap<String, String>()
            params.putAll(map) // 不加密的参数

            OkHttpHandlerData(headers, httpParams, object : HandlerParams {
                override fun handlerData(header: HttpHeaders, httpParam: HttpParams) {
                    try {
                        OkHttpUtils.get(url)
                            .headers(header)
                            .params(params)
                            .params(httpParam)
                            .execute(callBack)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
        } else {
            OkHttpUtils.get(url)
                .execute(callBack)
        }
    }

    /**
     * 参数调用
     * post
     * @param url
     * @param map
     * @param callBack
     */
    fun postHeaderJsonRequest(
        tag: String?,
        url: String,
        map: HashMap<String, String>?,
        headers: HttpHeaders?,
        httpParams: HttpParams?,
        callBack: AbsCallback<*>?,
    ) {
        if (map != null) {
            try {
                LogUtils.e(tag + "post请求:$url,参数---${transMap2String(map)}")

                val params = HashMap<String, String>()
                params.putAll(map) // 不加密的参数

                OkHttpHandlerData(headers, httpParams, object : HandlerParams {
                    override fun handlerData(header: HttpHeaders, httpParam: HttpParams) {
                        try {
                            OkHttpUtils.post(url)
                                .headers(header)
                                .params(params)
                                .params(httpParam)
                                .execute(callBack)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            OkHttpUtils.post(url)
                .execute(callBack)
        }
    }

    /**
     * 参数处理
     */
    fun OkHttpHandlerData(
        headers: HttpHeaders?,
        httpParams: HttpParams?,
        callBack: HandlerParams?,
    ) {
        val access_token = getPreferences("access_token", "")

        var header = HttpHeaders()
        if (headers == null) {
            header.put("Content-Type", "application/x-www-form-urlencoded")
            header.put("Authorization", "Bearer $access_token")
            header.put("Accept", "application/json")
        } else {
            header = headers
        }

        var httpParam = HttpParams()
        if (httpParams != null) {
            httpParam = httpParams
        }

        callBack?.handlerData(header, httpParam)
    }

    interface HandlerParams {
        fun handlerData(header: HttpHeaders, httpParam: HttpParams)
    }

    companion object {

        val instance: com.gang.library.common.http.OkHttpUtils
            get() = SingletonHolder.INSTANCE
    }

    private object SingletonHolder {
        var INSTANCE: com.gang.library.common.http.OkHttpUtils = OkHttpUtils()
    }

}
