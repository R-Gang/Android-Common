package com.gang.library.common.utils

import android.app.AppOpsManager
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationManagerCompat

/**
 * Created by haoruigang
 * 2018/12/20 10:17
 */
class NotifiUtil {
    /*=====================添加Listener回调================================*/
    interface OnNextLitener {
        /**
         * 不需要设置通知的下一步
         */
        fun onNext()
    }

    private var mOnNextLitener: OnNextLitener? = null
    fun setOnNextLitener(mOnNextLitener: OnNextLitener?) {
        this.mOnNextLitener = mOnNextLitener
    }

    companion object {
        //判断是否需要打开设置界面
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        fun OpenNotificationSetting(
            context: Context,
            mOnNextLitener: OnNextLitener?
        ) {
            if (!isNotificationEnabled(context)) {
                showSettingDialog(context)
            } else {
                mOnNextLitener?.onNext()
            }
        }
        //判断该app是否打开了通知
        /**
         * 可以通过NotificationManagerCompat 中的 areNotificationsEnabled()来判断是否开启通知权限。NotificationManagerCompat 在 android.support.v4.app包中，是API 22.1.0 中加入的。而 areNotificationsEnabled()则是在 API 24.1.0之后加入的。
         * areNotificationsEnabled 只对 API 19 及以上版本有效，低于API 19 会一直返回true
         */
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        fun isNotificationEnabled(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val notificationManagerCompat =
                    NotificationManagerCompat.from(context)
                return notificationManagerCompat.areNotificationsEnabled()
            }
            val CHECK_OP_NO_THROW = "checkOpNoThrow"
            val OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION"
            val mAppOps =
                context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val appInfo = context.applicationInfo
            val pkg = context.applicationContext.packageName
            val uid = appInfo.uid
            var appOpsClass: Class<*>? = null
            /* Context.APP_OPS_MANAGER */try {
                appOpsClass = Class.forName(AppOpsManager::class.java.name)
                val checkOpNoThrowMethod = appOpsClass.getMethod(
                    CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String::class.java
                )
                val opPostNotificationValue =
                    appOpsClass.getDeclaredField(OP_POST_NOTIFICATION)
                val value = opPostNotificationValue[Int::class.java] as Int
                return checkOpNoThrowMethod.invoke(
                    mAppOps,
                    value,
                    uid,
                    pkg
                ) as Int == AppOpsManager.MODE_ALLOWED
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }
        //打开手机设置页面
        /**
         * 假设没有开启通知权限，点击之后就需要跳转到 APP的通知设置界面，对应的Action是：Settings.ACTION_APP_NOTIFICATION_SETTINGS, 这个Action是 API 26 后增加的
         * 如果在部分手机中无法精确的跳转到 APP对应的通知设置界面，那么我们就考虑直接跳转到 APP信息界面，对应的Action是：Settings.ACTION_APPLICATION_DETAILS_SETTINGS
         */
        private fun gotoSet(context: Context) {
            val intent = Intent()
            if (Build.VERSION.SDK_INT >= 26) { // android 8.0引导
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
            } else if (Build.VERSION.SDK_INT >= 21) { // android 5.0-7.0
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("app_package", context.packageName)
                intent.putExtra("app_uid", context.applicationInfo.uid)
            } else { // 其他
                intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                intent.data = Uri.fromParts("package", context.packageName, null)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        private fun showSettingDialog(mContext: Context) { //提示弹窗
            val dialog: Dialog = AlertDialog.Builder(mContext)
                .setTitle("开启推送实时接收维保信息")
                .setPositiveButton(
                    "通知我"
                ) { dialog12: DialogInterface, which: Int ->
                    gotoSet(mContext)
                    dialog12.dismiss()
                }
                .setNegativeButton(
                    "暂不需要"
                ) { dialog1: DialogInterface, which: Int -> dialog1.dismiss() }
                .create()
            dialog.show()
        }
    }
}