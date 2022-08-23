package com.gang.app.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.gang.app.R
import com.gang.library.common.Permission.getScanCamere
import com.gang.library.common.dimension.statusBarHeight
import com.gang.library.common.ext.permissions.BasePermissionActivity
import com.gang.library.common.user.Config
import com.gang.library.ui.widget.BaseSearchBar
import com.gang.library.ui.widget.numbercode.PhoneCodeFill
import com.gang.tools.kotlin.utils.LogUtils
import com.gang.tools.kotlin.utils.typefaceAll
import com.gang.tools.kotlin.utils.vClick
import com.uuzuche.lib_zxing.activity.CaptureActivity
import com.uuzuche.lib_zxing.activity.CodeUtils
import kotlinx.android.synthetic.main.activity_picker.*

/**
 * 1.地址选择器底部弹框
 * 2.页面选择器
 * 3.获取四位验证码
 * 4.不规则圆角（平滑圆角）
 */
class PickerActivity : BasePermissionActivity() {

    override val layoutId: Int
        get() = R.layout.activity_picker

    override fun initView(savedInstanceState: Bundle?) {
        mNumberCodeView = findViewById<View>(R.id.number_code_view) as PhoneCodeFill
        mNumberCodeView.showSoftInputFlash()
        mNumberCodeView.getetCode1()?.typeface = typefaceAll
        mNumberCodeView.getetCode2()?.typeface = typefaceAll
        mNumberCodeView.getetCode3()?.typeface = typefaceAll
        mNumberCodeView.getetCode4()?.typeface = typefaceAll
        mNumberCodeView.setOnInputListener(object : PhoneCodeFill.OnInputListener {
            override fun onSucess(code: String?) {
                //TODO: 例如底部【下一步】按钮可点击
                mInputCode = code
                LogUtils.d("mInputCode", mInputCode)

                mNumberCodeView.clearCode() // 验证码输入错误清空
            }

            override fun onInput() {
                //TODO:例如底部【下一步】按钮不可点击
            }
        })

        // 目前kotlin-android-extensions暂时还不支持跨模块
        findViewById<BaseSearchBar>(R.id.myBoolbar)?.apply {
            goneLine()
            setLLEmtity(statusBarHeight.toFloat())
        }

    }

    override fun initData() {

    }

    override fun onClick() {
        super.onClick()

        btn_scan.vClick {
            // 简单模式,加载默认二维码扫描界面
            getScanCamere<CaptureActivity>(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == Config.REQUEST_CODE_CAMERE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                val bundle = data.extras ?: return
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    val result = bundle.getString(CodeUtils.RESULT_STRING)
                    Toast.makeText(this, "解析结果:$result", Toast.LENGTH_LONG).show()
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    companion object {

        private var pname: String? = null
        private var cname: String? = null
        private var dname: String? = null

        lateinit var mNumberCodeView: PhoneCodeFill

        var phoneNum = ""
        private var mInputCode: String? = null
    }

}