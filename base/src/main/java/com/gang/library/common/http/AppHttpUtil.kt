package com.gang.library.common.http

import com.apkfuns.logutils.LogUtils
import com.gang.library.common.utils.isNetConnected
import com.lzy.okhttputils.callback.FileCallback
import com.lzy.okhttputils.callback.StringCallback
import com.lzy.okhttputils.request.BaseRequest
import com.vector.update_app.HttpManager
import okhttp3.Call
import okhttp3.Response
import org.json.JSONException
import java.io.File
import java.net.ConnectException
import java.net.SocketTimeoutException

/**
 * Created by haoruigang
 * on 2017/6/19 0019.
 */
class AppHttpUtil : HttpManager {
    /**
     * 异步get
     *
     * @param url      get请求地址
     * @param params   get参数
     * @param callBack 回调
     */
    override fun asyncGet(
        url: String,
        params: Map<String, String>,
        callBack: HttpManager.Callback,
    ) {
        OkHttpUtils.instance.getOkHttpJsonRequest("app版本更新",
            url,
            params,
            object : StringCallback() {

                override fun onSuccess(t: String?, call: Call?, response: Response?) {
                    LogUtils.json("接口返回数据\n$t")
                    callBack.onResponse(t)
                }

                override fun onError(call: Call?, response: Response?, e: java.lang.Exception?) {
                    super.onError(call, response, e)
                    callBack.onError(validateError(e, response))
                }

            })

    }

    protected fun validateError(error: java.lang.Exception?, response: Response?): String {
        if (error != null) {
            if (!isNetConnected()) {
                return "无网络，请联网重试"
            } else if (error is SocketTimeoutException) {
                return "网络连接超时，请稍候重试"
            } else if (error is JSONException) {
                return "json转化异常"
            } else if (error is ConnectException) {
                return "服务器网络异常或宕机，请稍候重试"
            }
        }
        if (response != null) {
            val code = response.code()
            return if (code >= 500) {
                "服务器异常，请稍候重试"
            } else if (code < 500 && code >= 400) {
                "接口异常，请稍候重试"
            } else {
                String.format("未知异常 code = %d，请稍候重试", code)
            }
        }
        return "未知异常，请稍候重试"
    }

    /**
     * 异步post
     *
     * @param url      post请求地址
     * @param params   post请求参数
     * @param callBack 回调
     */
    override fun asyncPost(
        url: String,
        params: Map<String, String>,
        callBack: HttpManager.Callback,
    ) {
        OkHttpUtils.instance.postOkHttpJsonRequest("app版本更新",
            url,
            params,
            object : StringCallback() {

                override fun onSuccess(t: String?, call: Call?, response: Response?) {
                    LogUtils.json("接口返回数据\n$t")
                    callBack.onResponse(t)
                }

                override fun onError(call: Call?, response: Response?, e: java.lang.Exception?) {
                    super.onError(call, response, e)
                    callBack.onError(validateError(e, response))
                }

            })
    }

    /**
     * 下载
     *
     * @param url      下载地址
     * @param path     文件保存路径
     * @param fileName 文件名称
     * @param callback 回调
     */
    override fun download(
        url: String,
        path: String,
        fileName: String,
        callback: HttpManager.FileCallback,
    ) {
        OkHttpUtils.instance.getOkHttpJsonRequest("app版本下载", url,
            hashMapOf(), object : FileCallback(path, fileName) {
                override fun downloadProgress(
                    currentSize: Long,
                    totalSize: Long,
                    progress: Float,
                    networkSpeed: Long,
                ) {
                    super.downloadProgress(currentSize, totalSize, progress, networkSpeed)
                    callback.onProgress(progress, totalSize)
                }

                override fun onError(call: Call?, response: Response?, e: java.lang.Exception?) {
                    super.onError(call, response, e)
                    callback.onError(validateError(e, response))
                }

                override fun onBefore(request: BaseRequest<out BaseRequest<*>>?) {
                    super.onBefore(request)
                    callback.onBefore()
                }

                override fun onSuccess(file: File?, call: Call?, response: Response?) {
                    callback.onResponse(file)
                }
            })
    }
}