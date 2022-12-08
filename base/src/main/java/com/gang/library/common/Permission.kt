package com.gang.library.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.NonNull
import com.gang.library.R
import com.gang.library.common.user.Config
import com.gang.library.ui.activity.FileDisplayActivity
import com.gang.tools.kotlin.utils.LogUtils
import com.gang.tools.kotlin.utils.toActivityForResult
import com.github.dfqin.grantor.PermissionListener
import com.github.dfqin.grantor.PermissionsUtil
import com.github.dfqin.grantor.PermissionsUtil.TipInfo


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
        val tip = TipInfo("注意:", mContext.getString(R.string.rationale_call_phone), "不让看", "打开权限")
        PermissionsUtil.requestPermission(
            mContext,
            object : PermissionListener {
                override fun permissionGranted(@NonNull permissions: Array<String>) {
                    LogUtils.e(mContext.toString(), "TODO: CALL_PHONE Granted")
                    mContext.startActivity(
                        Intent(
                            Intent.ACTION_CALL,
                            Uri.parse("tel:$stuPhone")
                        )
                    )
                }

                override fun permissionDenied(@NonNull permissions: Array<String>) {
                    LogUtils.e(mContext.toString(), "TODO: CALL_PHONE Denied")
                }
            },
            arrayOf(Manifest.permission.CALL_PHONE), true, tip
        )
    }

    //获取CAMERA权限 haoruigang on 2021-11-25 13:50:43
    inline fun <reified T : Activity> Activity.getScanCamere(
        mContext: Context,
        tipInfo: TipInfo? = TipInfo(
            "注意:",
            mContext.getString(R.string.rationale_call_camere),
            "不让看",
            "打开权限"
        )
    ) {
        PermissionsUtil.requestPermission(
            mContext,
            object : PermissionListener {
                override fun permissionGranted(@NonNull permissions: Array<String>) {
                    LogUtils.e(mContext.toString(), "TODO: CAMERA Granted")
                    (mContext as Activity).toActivityForResult<T>(Config.REQUEST_CODE_CAMERE) // T to CaptureActivity
                }

                override fun permissionDenied(@NonNull permissions: Array<String>) {
                    LogUtils.e(mContext.toString(), "TODO: CALL_PHONE Denied")
                }
            },
            arrayOf(Manifest.permission.CAMERA), true, tipInfo
        )
    }

    //获取CAMERA权限 haoruigang on 2021-11-25 13:50:43
    inline fun <reified T : Activity> Context.getScanCamere(
        mContext: Context,
        tipInfo: TipInfo? = TipInfo(
            "注意:",
            mContext.getString(R.string.rationale_call_camere),
            "不让看",
            "打开权限"
        )
    ) {
        PermissionsUtil.requestPermission(
            mContext,
            object : PermissionListener {
                override fun permissionGranted(@NonNull permissions: Array<String>) {
                    LogUtils.e(mContext.toString(), "TODO: CAMERA Granted")
                    (mContext as Activity).toActivityForResult<T>(Config.REQUEST_CODE_CAMERE) // T to CaptureActivity
                }

                override fun permissionDenied(@NonNull permissions: Array<String>) {
                    LogUtils.e(mContext.toString(), "TODO: CALL_PHONE Denied")
                }
            },
            arrayOf(Manifest.permission.CAMERA), true, tipInfo
        )

    }

    //获取READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE权限 haoruigang on 2018-4-3 15:29:46
    fun Activity.toGetRead_Write(
        filePath: String,
        fileName: String,
        tipInfo: TipInfo? = TipInfo(
            "注意:",
            this.getString(R.string.rationale_file),
            "不让看",
            "打开权限"
        )
    ) {
        PermissionsUtil.requestPermission(
            this,
            object : PermissionListener {
                override fun permissionGranted(@NonNull permissions: Array<String>) {
                    FileDisplayActivity.actionStart(
                        this@toGetRead_Write,
                        filePath,
                        fileName
                    )
                }

                override fun permissionDenied(@NonNull permissions: Array<String>) {
                    LogUtils.e(this@toGetRead_Write, "TODO: CALL_PHONE Denied")
                }
            },
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), true, tipInfo
        )
    }

    fun Context.toGetRead_Write(
        filePath: String, fileName: String, tipInfo: TipInfo = TipInfo(
            "注意:",
            this.getString(R.string.rationale_file),
            "不让看",
            "打开权限"
        )
    ) {
        //动态权限申请
        PermissionsUtil.requestPermission(
            this,
            object : PermissionListener {
                override fun permissionGranted(@NonNull permissions: Array<String>) {
                    FileDisplayActivity.actionStart(
                        this@toGetRead_Write,
                        filePath,
                        fileName
                    )
                }

                override fun permissionDenied(@NonNull permissions: Array<String>) {
                    LogUtils.e(this@toGetRead_Write, "TODO: CALL_PHONE Denied")
                }
            },
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), true, tipInfo
        )

    }


    //获取权限 haoruigang on 2018-4-3 15:29:46
    fun requestPermission(
        mContext: Context,
        permissions: Array<String>,
        showTip: Boolean = false,
        tipInfo: TipInfo? = null,
        permissAction: PermissionListener? = null,
    ) {
        //动态权限申请
        PermissionsUtil.requestPermission(mContext, object : PermissionListener {
            override fun permissionGranted(permissions: Array<String>) {
                LogUtils.e(mContext.toString(), "用户授权访问")
                permissAction?.permissionGranted(permissions)
            }

            override fun permissionDenied(permissions: Array<String>) {
                LogUtils.e(mContext.toString(), "用户拒绝权限")
                permissAction?.permissionDenied(permissions)
            }
        }, permissions, showTip, tipInfo)
    }

}