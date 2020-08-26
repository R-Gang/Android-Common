package com.gang.library.common.utils.oss

import android.content.Context
import android.util.Log
import com.alibaba.sdk.android.oss.*
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask
import com.alibaba.sdk.android.oss.model.*
import com.gang.library.BaseApplication.Companion.appContext
import com.gang.library.common.user.Config
import com.gang.library.common.utils.oss.callback.ProgressCallback
import java.io.File

/**
 * Created by haoruigang on 2016/9/2.
 */
class AliYunOss(context: Context?) {

    var oss: OSS

    // Upload from local files. Use asynchronous API asyncPutObjectFromLocalFile
    fun upload(
        fileName: String?,
        path: String?,
        progressCallback: ProgressCallback<PutObjectRequest?, PutObjectResult?>?
    ) { // Creates the upload request
        val put = PutObjectRequest(Config.ossBucket, fileName, path)
        put.crC64 = OSSRequest.CRC64Config.YES
        // Sets the progress callback and upload file asynchronously
        put.progressCallback =
            OSSProgressCallback { request: PutObjectRequest?, currentSize: Long, totalSize: Long ->
                progressCallback?.onProgress(request, currentSize, totalSize)
            }
        val task: OSSAsyncTask<*> = oss.asyncPutObject(
            put,
            object : OSSCompletedCallback<PutObjectRequest?, PutObjectResult> {
                override fun onSuccess(
                    request: PutObjectRequest?,
                    result: PutObjectResult
                ) {
                    progressCallback!!.onSuccess(request, result)
                    Log.d("PutObject", "UploadSuccess")
                    Log.d("ETag", result.eTag)
                    Log.d("RequestId", result.requestId)
                    // 只有设置了servercallback，这个值才有数据
                    val serverCallbackReturnJson =
                        result.serverCallbackReturnBody
                    Log.d("servercallback", serverCallbackReturnJson)
                }

                override fun onFailure(
                    request: PutObjectRequest?,
                    clientExcepion: ClientException,
                    serviceException: ServiceException
                ) {
                    progressCallback!!.onFailure(request, clientExcepion, serviceException)
                    // request exception
                    clientExcepion?.printStackTrace()
                    if (serviceException != null) { // service side exception
                        Log.e("ErrorCode", serviceException.errorCode)
                        Log.e("RequestId", serviceException.requestId)
                        Log.e("HostId", serviceException.hostId)
                        Log.e("RawMessage", serviceException.rawMessage)
                    }
                }
            })
        task.waitUntilFinished()
    }

    fun download(
        fileName: String?,
        progressCallback: ProgressCallback<GetObjectRequest?, GetObjectResult?>?
    ) {
        val get = GetObjectRequest(Config.ossBucket, fileName)
        get.crC64 = OSSRequest.CRC64Config.YES
        //设置下载进度回调
        get.setProgressListener { request: GetObjectRequest?, currentSize: Long, totalSize: Long ->
            progressCallback?.onProgress(request, currentSize, totalSize)
        }
        val task: OSSAsyncTask<*> = oss.asyncGetObject(
            get,
            object : OSSCompletedCallback<GetObjectRequest?, GetObjectResult?> {
                override fun onSuccess(
                    request: GetObjectRequest?,
                    result: GetObjectResult?
                ) { // 请求成功
                    progressCallback!!.onSuccess(request, result)
                }

                override fun onFailure(
                    request: GetObjectRequest?,
                    clientExcepion: ClientException,
                    serviceException: ServiceException
                ) {
                    progressCallback!!.onFailure(request, clientExcepion, serviceException)
                    // 请求异常
                    clientExcepion?.printStackTrace()
                    if (serviceException != null) { // 服务异常
                        Log.e("ErrorCode", serviceException.errorCode)
                        Log.e("RequestId", serviceException.requestId)
                        Log.e("HostId", serviceException.hostId)
                        Log.e("RawMessage", serviceException.rawMessage)
                    }
                }
            })
        task.waitUntilFinished()
    }

    companion object {
        @Volatile
        private var instance: AliYunOss? = null

        fun getInstance(context: Context?): AliYunOss? {
            synchronized(AliYunOss::class.java) {
                if (instance == null) {
                    instance = AliYunOss(context)
                }
            }
            return instance
        }
    }

    init {
        val endpoint = Config.ENDPOINT
        // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
        val credentialProvider: OSSCredentialProvider =
            OSSPlainTextAKSKCredentialProvider(Config.accessKeyId, Config.accessKeySecret)
        val conf = ClientConfiguration()
        conf.connectionTimeout = 15 * 1000 // 连接超时，默认15秒
        conf.socketTimeout = 15 * 1000 // socket超时，默认15秒
        conf.maxConcurrentRequest = 5 // 最大并发请求书，默认5个
        conf.maxErrorRetry = 2 // 失败后最大重试次数，默认2次
        //        OSSLog.enableLog();
        oss = OSSClient(appContext, endpoint, credentialProvider, conf)
    }
}