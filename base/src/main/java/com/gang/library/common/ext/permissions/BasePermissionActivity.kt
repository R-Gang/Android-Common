package com.gang.library.common.ext.permissions

import android.R
import android.content.Intent
import com.gang.library.common.ext.permissions.easyPermission.EasyPermission
import com.gang.library.ui.activity.BaseActivity

/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.app.common.utils.permissions.easyPermission
 * @ClassName:      BasePermissionActivity
 * @Description:    动态请求权限
 * @Author:         haoruigang
 * @CreateDate:     2020/8/3 16:34
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/3 16:34
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
abstract class BasePermissionActivity : BaseActivity(), EasyPermission.PermissionCallback {
    private var mRequestCode = 0
    private lateinit var mPermissions: Array<String>
    private var mPermissionCallBack: PermissionCallBackM? = null

    //rationale: 申请授权理由
    fun requestPermission(
        requestCode: Int, permissions: Array<String>, rationale: String?,
        permissionCallback: PermissionCallBackM?,
    ) {
        mRequestCode = requestCode
        mPermissionCallBack = permissionCallback
        mPermissions = permissions
        EasyPermission.with(this)
            .addRequestCode(requestCode)
            .permissions(*permissions)
            //.nagativeButtonText(android.R.string.ok)
            //.positveButtonText(android.R.string.cancel)
            .rationale(rationale)
            .request()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    public override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        /*
            从Settings界面跳转回来，标准代码，就这么写
        */if (requestCode == EasyPermission.SETTINGS_REQ_CODE) {
            if (EasyPermission.hasPermissions(this, *mPermissions)) { //已授权，处理业务逻辑
                onEasyPermissionGranted(mRequestCode, *mPermissions)
            } else {
                onEasyPermissionDenied(mRequestCode, *mPermissions)
            }
        }
    }

    override fun onEasyPermissionGranted(requestCode: Int, vararg perms: String?) {
        mPermissionCallBack?.onPermissionGrantedM(requestCode, *perms)
    }

    override fun onEasyPermissionDenied(
        requestCode: Int,
        vararg perms: String?,
    ) { //rationale: Never Ask Again后的提示信息
        if (EasyPermission.checkDeniedPermissionsNeverAskAgain(
                this,
                "授权啊,不授权没法用啊," + "去设置里授权大哥",
                R.string.ok,
                R.string.cancel,
                { dialog, which ->
                    mPermissionCallBack?.onPermissionDeniedM(
                        requestCode, *perms
                    )
                },
                perms
            )
        ) {
            return
        }
        mPermissionCallBack?.onPermissionDeniedM(requestCode, *perms)
    }
}