package com.gang.library.common.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.gang.library.BaseApplication.Companion.TAG
import com.gang.library.R
import com.gang.library.common.user.Config
import com.gang.library.common.utils.permissions.BasePermissionActivity
import com.gang.library.common.utils.permissions.BasePermissionFragment
import com.gang.library.common.utils.permissions.PermissionCallBackM
import com.gang.library.ui.activity.FileDisplayActivity
import com.uuzuche.lib_zxing.activity.CaptureActivity

/**
 *
 * @ProjectName:    Android-Common
 * @Package:        com.gang.library.common.utils
 * @ClassName:      Sys
 * @Description:    获取权限
 * @Author:         haoruigang
 * @CreateDate:     2020/8/21 18:01
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/21 18:01
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
object SysUtils {

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
                    @SuppressLint("MissingPermission")
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
                    @SuppressLint("MissingPermission")
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
    fun getScanCamere(mContext: Context) {
        if (mContext is BasePermissionActivity) {
            mContext.requestPermission(Config.REQUEST_CAMERA,
                arrayOf(Manifest.permission.CAMERA),
                mContext.getString(R.string.rationale_call_camere),
                object : PermissionCallBackM {
                    @SuppressLint("MissingPermission")
                    override fun onPermissionGrantedM(
                        requestCode: Int,
                        vararg perms: String?,
                    ) {
                        LogUtils.e(mContext.toString(), "TODO: CAMERA Granted")
                        mContext.toActivityForResult<CaptureActivity>(Config.REQUEST_CODE_CAMERE)
                    }

                    override fun onPermissionDeniedM(
                        requestCode: Int,
                        vararg perms: String?,
                    ) {
                        LogUtils.e(mContext.toString(), "TODO: CALL_PHONE Denied")
                    }
                })
        } else if (mContext is BasePermissionFragment) {
            mContext.requestPermission(Config.REQUEST_CAMERA,
                arrayOf(Manifest.permission.CAMERA),
                mContext.getString(R.string.rationale_call_camere),
                object : PermissionCallBackM {
                    @SuppressLint("MissingPermission")
                    override fun onPermissionGrantedM(
                        requestCode: Int,
                        vararg perms: String?,
                    ) {
                        LogUtils.e(mContext.toString(), "TODO: CAMERA Granted")
                        (mContext as Activity).toActivityForResult<CaptureActivity>(Config.REQUEST_CODE_CAMERE)
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

    //获取READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE权限 haoruigang on 2018-4-3 15:29:46
    fun getRead_Write(mContext: Context, filePath: String, fileName: String) {
        //动态权限申请
        if (mContext is BasePermissionActivity) {
            mContext.requestPermission(Config.REQUEST_CODE_READ_WRITE,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                mContext.getString(R.string.rationale_file),
                object : PermissionCallBackM {
                    override fun onPermissionGrantedM(requestCode: Int, vararg perms: String?) {
                        FileDisplayActivity.actionStart(mContext, filePath, fileName)
                    }

                    override fun onPermissionDeniedM(requestCode: Int, vararg perms: String?) {
                        LogUtils.e(TAG, "TODO: WRITE_EXTERNAL_STORAGE Denied")
                    }
                })
        } else if (mContext is BasePermissionFragment) {
            mContext.requestPermission(Config.REQUEST_CODE_READ_WRITE,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                mContext.getString(R.string.rationale_file),
                object : PermissionCallBackM {
                    override fun onPermissionGrantedM(requestCode: Int, vararg perms: String?) {
                        FileDisplayActivity.actionStart(mContext, filePath, fileName)
                    }

                    override fun onPermissionDeniedM(requestCode: Int, vararg perms: String?) {
                        LogUtils.e(TAG, "TODO: WRITE_EXTERNAL_STORAGE Denied")
                    }
                })
        }

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