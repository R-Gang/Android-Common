package com.gang.library.common.ext.oss.callback

import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.ServiceException

/**
 * Created by haoruigang on 2017/8/31.
 */
interface Callback<T1, T2> {
    fun onSuccess(request: T1, result: T2)
    fun onFailure(
        request: T1,
        clientException: ClientException?,
        serviceException: ServiceException?
    )
}