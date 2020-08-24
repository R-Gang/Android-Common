package com.gang.library.common.http.callback

/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.app.common.http.callback
 * @ClassName:      IHttpCallBack
 * @Description:    网络回调接口类
 * @Author:         haoruigang
 * @CreateDate:     2020/8/3 15:56
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/3 15:56
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
open interface IHttpCallBack<T> {
    fun onSuccess(data: T)
    fun onFail(statusCode: Int, errorMsg: String?)
    fun onError(throwable: Throwable?)
}