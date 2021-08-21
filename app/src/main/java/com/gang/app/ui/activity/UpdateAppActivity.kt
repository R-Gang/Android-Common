package com.gang.app.ui.activity

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.gang.app.R
import com.gang.app.common.user.Constants
import com.gang.library.common.utils.LogUtils
import com.gang.library.common.utils.permissions.PermissionCallBackM
import com.gang.library.ui.activity.BaseActivity
import com.vector.update_app.UpdateAppManager

/**
 * 版本更新使用方式示例
 */

@Suppress("UNREACHABLE_CODE")
@RequiresApi(api = Build.VERSION_CODES.O)
class UpdateAppActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_update_app

    override fun initData() {
        TODO("Not yet implemented")
    }

    override fun initView(savedInstanceState: Bundle?) {
        TODO("Not yet implemented")

        //动态权限申请
        requestPermission(
            Constants.REQUEST_CODE_WRITE, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            getString(R.string.rationale_file),
            object : PermissionCallBackM {
                override fun onPermissionGrantedM(requestCode: Int, vararg perms: String?) {
                    //版本更新
//                    UpdateAppManager.Builder() //当前Activity
//                        .setActivity(this@UpdateAppActivity) //更新地址
//                        .setUpdateUrl(Constants.VERSION_PATH)
//                        .handleException { obj: java.lang.Exception -> obj.printStackTrace() } //实现httpManager接口的对象
//                        .setHttpManager(AppHttpUtil())
//                        .build()
//                        .checkNewApp(UpdateCallback(this@UpdateAppActivity))
                }

                override fun onPermissionDeniedM(requestCode: Int, vararg perms: String?) {
                    TODO("Not yet implemented")
                    LogUtils.e("MainActivity", "TODO: WRITE_EXTERNAL_STORAGE Denied")
                }

            })
    }
}