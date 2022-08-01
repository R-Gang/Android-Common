package com.gang.app.common.user

import android.app.Activity
import com.alibaba.sdk.android.push.CommonCallback
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory
import com.gang.library.bean.UserEntity
import com.gang.library.common.store.getSpValue
import com.orhanobut.logger.Logger

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

    fun isLogin(): Boolean {
        return userData.user_id.isNotEmpty() && userData.user_token.isNotEmpty()
    }

    fun isLogin1(): Boolean {
        return (getSpValue("user_id", "").toString().isNotEmpty()
                && getSpValue("user_token", "").toString().isNotEmpty())
    }

    private val mPushService = PushServiceFactory.getCloudPushService()

    open fun alibabaPush(account: String?, pushAccount: PushAccountCallBack) {
        // val account: String? = MD5(userData.user_id)
        mPushService.bindAccount(account, object : CommonCallback {
            override fun onSuccess(s: String) {
                Logger.d("AlibabaPush1\t$s\t推送注册成功，设备token为：$account")
                pushAccount.onSuccess(s)
            }

            override fun onFailed(errorCode: String, errorMsg: String) {
                Logger.d("AlibabaPush1\t\t推送注册失败，错误码：$errorMsg,错误信息：$errorMsg")
                pushAccount.onFailed(errorCode, errorMsg)
            }
        })
    }

    open fun alibabaUnPush(activity: Activity, pushAccount: PushAccountCallBack) {
        // 1.获取设备Token
//        val dialog = MyProgressDialog(activity, true)
//        dialog.show()
        mPushService.unbindAccount(object : CommonCallback {
            override fun onSuccess(s: String) {
//                logout(activity, dialog)
                Logger.d("AlibabaPush2\t$s\t推送注销成功")
                pushAccount.onSuccess(s)
            }

            override fun onFailed(errorCode: String, errorMsg: String) {
                Logger.d("AlibabaPush2\t\t推送注销失败 errCode = $errorCode , msg = $errorMsg")
                pushAccount.onFailed(errorCode, errorMsg)
            }
        })
    }

    interface PushAccountCallBack {
        fun onSuccess(s: String)

        fun onFailed(errorCode: String, errorMsg: String)
    }
}