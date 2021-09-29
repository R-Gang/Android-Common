package com.gang.app.common.user

import android.app.Activity
import com.alibaba.sdk.android.push.CommonCallback
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory
import com.apkfuns.logutils.LogUtils
import com.gang.library.bean.UserEntity
import com.gang.library.common.http.progress.MyProgressDialog
import com.gang.library.common.utils.MD5

/**
 *
 * @ProjectName:    Android-Common
 * @Package:        com.gang.app.common.user
 * @ClassName:      UserManager
 * @Description:     java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     2021/9/22 14:32
 * @UpdateUser:     更新者：
 * @UpdateDate:     2021/9/22 14:32
 * @UpdateRemark:   更新说明：
 * @Version:
 */
enum class UserManager {

    INSTANCE;

    var userData = UserEntity()

    private val mPushService = PushServiceFactory.getCloudPushService()
    open fun xgPush() {
        val account: String? = MD5(userData.user_id)
        mPushService.bindAccount(account, object : CommonCallback {
            override fun onSuccess(s: String) {
                LogUtils.d("AlibabaPush1", s + "推送注册成功，设备token为：" + account)
            }

            override fun onFailed(errorCode: String, errorMsg: String) {
                LogUtils.d("AlibabaPush1", "推送注册失败，错误码：$errorMsg,错误信息：$errorMsg")
            }
        })
    }

    open fun xgUnPush(activity: Activity) {
        // 1.获取设备Token
        val account: String? = MD5(userData.user_id)
        val dialog = MyProgressDialog(activity, true)
        dialog.show()
        mPushService.unbindAccount(object : CommonCallback {
            override fun onSuccess(s: String) {
//                logout(activity, dialog)
                LogUtils.d("AlibabaPush2", "推送注销成功$s")
            }

            override fun onFailed(errorCode: String, errorMsg: String) {
                LogUtils.d("AlibabaPush2", "推送注销失败 errCode = $errorCode , msg = $errorMsg")
            }
        })
    }
}