package com.gang.library.common.utils.version

import com.vector.update_app.service.DownloadService
import java.io.File

open class DownloadCallback : DownloadService.DownloadCallback {
    /**
     * 开始
     */
    override fun onStart() {
        // "下载进度"
    }

    /**
     * 进度k
     *
     * @param progress  进度 0.00 -1.00 ，总大小
     * @param totalSize 总大小 单位B
     */
    override fun onProgress(progress: Float, totalSize: Long) {
        // Math.round(progress * 100)
    }

    /**
     * 总大小
     *
     * @param totalSize 单位B
     */
    override fun setMax(totalSize: Long) {}

    /**
     * 下载完了
     *
     * @param file 下载的app
     * @return true ：下载完自动跳到安装界面，false：则不进行安装
     */
    override fun onFinish(file: File?): Boolean {
        return true
    }

    /**
     * 下载异常
     *
     * @param msg 异常信息
     */
    override fun onError(msg: String?) {}

    /**
     * 当应用处于前台，准备执行安装程序时候的回调，
     *
     * @param file 当前安装包
     * @return false 默认 false ,当返回时 true 时，需要自己处理 ，前提条件是 onFinish 返回 false 。
     */
    override fun onInstallAppAndAppOnForeground(file: File?): Boolean {
        return false
    }
}