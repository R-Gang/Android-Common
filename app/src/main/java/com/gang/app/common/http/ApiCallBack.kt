package com.gang.app.common.http

import android.app.Activity
import android.text.TextUtils
import com.apkfuns.logutils.LogUtils
import com.gang.library.common.http.callback.HttpCallBack
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject

/**
 *
 * @ProjectName:
 * @Package:        com.jietu.software.app.common.http
 * @ClassName:      haoruigang
 * @Description:     java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     2021/9/6 15:44
 * @UpdateUser:     更新者：
 * @UpdateDate:     2021/9/6 15:44
 * @UpdateRemark:   更新说明：
 * @Version:
 */
abstract class ApiCallBack<T> : HttpCallBack<T> {

    constructor() : super()

    constructor(activity: Activity) : super(activity)

    constructor(activity: Activity, isDismiss: Boolean) : super(activity, isDismiss)

//    constructor() : this()

//    constructor(activity: Activity) : this(activity)

    // 是否可取消请求，默认可取消  haoruigang  2017-11-28 11:12:09
//    constructor(activity: Activity) : super(activity) {
//        dialog = LoadingDialog(activity)
//        dialog?.setLoadingText("加载中")
//            ?.setSuccessText("加载成功") //显示加载成功时的文字
//            ?.show()
//    }


    //  成功回调
    override fun onSuccess(o: Any?, call: Call?, response: Response?) {
        LogUtils.e("接口返回数据 $o")
        try {
            val jsonObject = JSONObject(o.toString())
            statusCode = jsonObject.optInt("code")
            data = jsonObject.optString("value")
            errorMsg = jsonObject.optString("message")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        try {
            dismiss()
            val gsonType = getTType(this.javaClass)
            if (statusCode == 0 || statusCode == 2000 && !TextUtils.isEmpty(o.toString())) {
                if ("class java.lang.String" == gsonType.toString()) {
                    onSuccess(o.toString() as T)
                } else {
                    val o1: T = Gson().fromJson(o.toString(), gsonType)
                    onSuccess(o1)
                }
            } else {
                onFail(statusCode, errorMsg)
            }
        } catch (e: Exception) {
            LogUtils.e("Exception $e")
            onError(e)
        }
    }

    companion object {
        var statusCode = 0
        var data: String? = null
        var errorMsg: String? = null

//        var dialog: LoadingDialog? = null

//        fun dismiss() {
//            dialog?.close()
//        }
    }

}