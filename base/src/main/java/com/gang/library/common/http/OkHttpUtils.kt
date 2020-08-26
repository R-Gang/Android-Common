package com.gang.library.common.http

import com.apkfuns.logutils.LogUtils
import com.gang.library.common.http.callback.HttpCallBack
import com.gang.library.common.utils.U
import com.lzy.okhttputils.OkHttpUtils
import java.util.*

/**
 *
 * @ProjectName:    ChangFenZaiXian
 * @Package:        com.changfenzaixian.app.common.http
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
        callBack: HttpCallBack<*>?
    ) {
        if (map != null) {
            try {
                LogUtils.e("get请求:$url,参数---${U.transMap2String(map)}")
                val params = HashMap<String, String>()
                params.putAll(map) // 不加密的参数
                OkHttpUtils.get(url)
                    .params(params)
                    .execute<Any>(callBack)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            OkHttpUtils.get(url)
                .execute<Any>(callBack)
        }
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
        callBack: HttpCallBack<*>?
    ) {
        if (map != null) {
            try {
                LogUtils.e("post请求:$url,参数---${U.transMap2String(map)}")
                val params = HashMap<String, String>()
                params.putAll(map) // 不加密的参数
                OkHttpUtils.post(url)
                    .params(params)
                    .execute<Any>(callBack)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            OkHttpUtils.post(url)
                .execute<Any>(callBack)
        }
    }

    companion object {
        // OkHttpUtils 自定义请求配置
        fun get(): OkHttpUtils {
            return OkHttpUtils.getInstance()
        }
    }

}