package com.gang.library.common.view.progress

import android.app.Activity
import com.xiasuhuei321.loadingdialog.view.LoadingDialog

/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.kotlin.okhttp.progress
 * @ClassName:      IHttpCallBack
 * @Description:    全局dialog
 * @Author:         haoruigang
 * @CreateDate:     2020/8/3 15:56
 */
class MyProgressDialog : LoadingDialog {

    private var activity: Activity

    /**
     * 不可取消(true不可，false可取消)
     *
     * @param activity
     * @param isDismiss
     */
    constructor(activity: Activity, isDismiss: Boolean) : super(activity) {
        Companion.isDismiss = isDismiss
        this.activity = activity
        init()
    }

    /**
     * 默认可取消
     *
     * @param activity
     */
    constructor(activity: Activity) : super(activity) {
        this.activity = activity
        init()
    }

    private fun init() {
        setLoadingText("加载中")
            .setSuccessText("加载成功") //显示加载成功时的文字
            .setInterceptBack(isDismiss)
            .show()

    }

    override fun show() {
        if (!activity.isFinishing) {
            super.show()
            isShow = true
        }
    }

    fun isShowing(): Boolean {
        return isShow
    }

    fun dismiss() {
        if (!activity.isFinishing) {
            close()
            isShow = false
        }
    }

    companion object {

        var isDismiss = false
        var isShow = false
    }
}