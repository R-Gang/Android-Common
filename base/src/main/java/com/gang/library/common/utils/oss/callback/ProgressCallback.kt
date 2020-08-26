package com.gang.library.common.utils.oss.callback

/**
 * Created by haoruigang on 2017/8/31.
 */
interface ProgressCallback<T1, T2> :
    Callback<T1, T2> {
    fun onProgress(request: T1, currentSize: Long, totalSize: Long)
}