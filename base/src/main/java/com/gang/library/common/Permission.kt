package com.gang.library.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.gang.library.BaseApp.Companion.TAG
import com.gang.library.R
import com.gang.library.common.ext.permissions.BasePermissionActivity
import com.gang.library.common.ext.permissions.BasePermissionFragment
import com.gang.library.common.ext.permissions.PermissionCallBackM
import com.gang.library.common.user.Config
import com.gang.library.ui.activity.FileDisplayActivity
import com.gang.tools.kotlin.utils.LogUtils
import com.gang.tools.kotlin.utils.toActivityForResult

/**
 *
 * @ProjectName:    Android-Common
 * @Package:        com.gang.tools.kotlin.utils
 * @ClassName:      Sys
 * @Description:    获取权限
 * @Author:         haoruigang
 * @CreateDate:     2020/8/21 18:01
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/21 18:01
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
object Permission {

    /**
     * GC
     */
    fun gcAndFinalize() {
        val runtime = Runtime.getRuntime()
        System.gc()
        runtime.runFinalization()
        System.gc()
    }

    //获取CALL_PHONE权限 haoruigang on 2018-4-3 15:29:46
    fun getCallPhone(mContext: Context, stuPhone: String) {
        if (mContext is BasePermissionActivity) {
            mContext.requestPermission(Config.REQUEST_CALL_PHONE,
                arrayOf(Manifest.permission.CALL_PHONE),
                mContext.getString(R.string.rationale_call_phone),
                object : PermissionCallBackM {

                    override fun onPermissionGrantedM(
                        requestCode: Int,
                        vararg perms: String?,
                    ) {
                        LogUtils.e(mContext.toString(), "TODO: CALL_PHONE Granted")
                        mContext.startActivity(Intent(Intent.ACTION_CALL,
                            Uri.parse("tel:$stuPhone")))
                    }

                    override fun onPermissionDeniedM(
                        requestCode: Int,
                        vararg perms: String?,
                    ) {
                        LogUtils.e(mContext.toString(), "TODO: CALL_PHONE Denied")
                    }
                })
        } else if (mContext is BasePermissionFragment) {
            mContext.requestPermission(Config.REQUEST_CALL_PHONE,
                arrayOf(Manifest.permission.CALL_PHONE),
                mContext.getString(R.string.rationale_call_phone),
                object : PermissionCallBackM {

                    override fun onPermissionGrantedM(
                        requestCode: Int,
                        vararg perms: String?,
                    ) {
                        LogUtils.e(mContext.toString(), "TODO: CALL_PHONE Granted")
                        mContext.startActivity(Intent(Intent.ACTION_CALL,
                            Uri.parse("tel:$stuPhone")))
                    }

                    override fun onPermissionDeniedM(
                        requestCode: Int,
                        vararg perms: String?,
                    ) {
                        LogUtils.e(mContext.toString(), "TODO: CALL_PHONE Denied")
                    }
                })
        }
    }

    //获取CAMERA权限 haoruigang on 2021-11-25 13:50:43
    inline fun <reified T : Activity> Activity.getScanCamere(mContext: Context) {
        (mContext as BasePermissionActivity).requestPermission(Config.REQUEST_CAMERA,
            arrayOf(Manifest.permission.CAMERA),
            mContext.getString(R.string.rationale_call_camere),
            object : PermissionCallBackM {
                override fun onPermissionGrantedM(
                    requestCode: Int,
                    vararg perms: String?,
                ) {
                    LogUtils.e(mContext.toString(), "TODO: CAMERA Granted")
                    mContext.toActivityForResult<T>(Config.REQUEST_CODE_CAMERE) // T to CaptureActivity
                }

                override fun onPermissionDeniedM(
                    requestCode: Int,
                    vararg perms: String?,
                ) {
                    LogUtils.e(mContext.toString(), "TODO: CALL_PHONE Denied")
                }
            })
    }

    //获取CAMERA权限 haoruigang on 2021-11-25 13:50:43
    inline fun <reified T : Activity> Context.getScanCamere(mContext: Context) {
        (mContext as BasePermissionActivity).requestPermission(Config.REQUEST_CAMERA,
            arrayOf(Manifest.permission.CAMERA),
            mContext.getString(R.string.rationale_call_camere),
            object : PermissionCallBackM {

                override fun onPermissionGrantedM(
                    requestCode: Int,
                    vararg perms: String?,
                ) {
                    LogUtils.e(mContext.toString(), "TODO: CAMERA Granted")
                    (mContext as Activity).toActivityForResult<T>(Config.REQUEST_CODE_CAMERE)
                }

                override fun onPermissionDeniedM(
                    requestCode: Int,
                    vararg perms: String?,
                ) {
                    LogUtils.e(mContext.toString(), "TODO: CALL_PHONE Denied")
                }
            })

    }

    //获取READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE权限 haoruigang on 2018-4-3 15:29:46
    fun Activity.toGetRead_Write(filePath: String, fileName: String) {
        (this as BasePermissionActivity).requestPermission(Config.REQUEST_CODE_READ_WRITE,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            mContext.getString(R.string.rationale_file),
            object : PermissionCallBackM {
                override fun onPermissionGrantedM(requestCode: Int, vararg perms: String?) {
                    FileDisplayActivity.actionStart(this@toGetRead_Write,
                        filePath,
                        fileName)
                }

                override fun onPermissionDeniedM(requestCode: Int, vararg perms: String?) {
                    LogUtils.e(TAG, "TODO: READ_EXTERNAL_STORAGE Denied")
                }
            })
    }

    fun Context.toGetRead_Write(filePath: String, fileName: String) {
        //动态权限申请
        (this as BasePermissionActivity).requestPermission(Config.REQUEST_CODE_READ_WRITE,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            getString(R.string.rationale_file),
            object : PermissionCallBackM {
                override fun onPermissionGrantedM(requestCode: Int, vararg perms: String?) {
                    FileDisplayActivity.actionStart(this@toGetRead_Write, filePath, fileName)
                }

                override fun onPermissionDeniedM(requestCode: Int, vararg perms: String?) {
                    LogUtils.e(TAG, "TODO: WRITE_EXTERNAL_STORAGE Denied")
                }
            })

    }


    //获取权限 haoruigang on 2018-4-3 15:29:46
    fun requestPermission(
        mContext: Context,
        requestCode: Int, permissions: Array<String>, rationale: String?,
        permissionCallback: PermissionCallBackM?,
    ) {
        //动态权限申请
        (mContext as BasePermissionActivity).requestPermission(
            requestCode,
            permissions,
            rationale,
            permissionCallback
        )
    }

}