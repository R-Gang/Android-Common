package com.gang.library.common.http.progress

import android.app.Activity
import android.content.Context
import com.gang.library.common.view.loadingdialog.view.LoadingDialog
/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.app.common.http.callback
 * @ClassName:      IHttpCallBack
 * @Description:    全局dialog
 * @Author:         haoruigang
 * @CreateDate:     2020/8/3 15:56
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/3 15:56
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class MyProgressDialog : LoadingDialog {
    private var activity: Activity?
    private var isDismiss = false
    private var isShow = false

    /**
     * 不可取消(true不可，false可取消)
     *
     * @param activity
     * @param isDismiss
     */
    constructor(activity: Activity?, isDismiss: Boolean) : super(activity) {
        this.isDismiss = isDismiss
        this.activity = activity
        init()
    }

    /**
     * 默认可取消
     *
     * @param activity
     */
    constructor(activity: Activity?) : super(activity) {
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
        if (activity != null && !activity!!.isFinishing) {
            super.show()
            isShow = true
        }
    }

    fun isShowing(): Boolean {
        return isShow
    }

    fun dismiss() {
        if (activity != null && !activity!!.isFinishing) {
            super.close()
            isShow = false
        }
    }

    companion object {
        // LoadingDialog 自定义参数
        fun get(context: Context): LoadingDialog {
            return LoadingDialog(context)
        }
    }
}