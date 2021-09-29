package com.gang.app.ui.activity

import android.os.Bundle
import android.os.CountDownTimer
import com.apkfuns.logutils.LogUtils
import com.gang.app.R
import com.gang.app.bean.BaseModel
import com.gang.app.common.http.ApiCallBack
import com.gang.app.common.http.HttpManager
import com.gang.app.common.user.ToUIEvent
import com.gang.library.common.user.Config
import com.gang.library.common.user.UserManager
import com.gang.library.common.utils.DateUtils
import com.gang.library.common.utils.MD5
import com.gang.library.common.utils.showToast
import com.gang.library.ui.activity.BaseActivity

/**
 * 接口使用方式示例
 */
class HttpApiActivity : BaseActivity() {


    private lateinit var headImage: String
    private var countDownTimer: CountDownTimer? = null
    val phoneNum = "18510507183"


    override val layoutId: Int
        get() = R.layout.activity_http_api

    override fun initView(savedInstanceState: Bundle?) {
        //return the compressed file path Config.photo +

        dark()

        var mImageName =
            DateUtils.getCurTimeLong("yyyyMMddHHmmss") + UserManager.INSTANCE.userData.user_id
                .toString() + ".jpg"
        //Url
        if (mImageName != null && mImageName != "") {
            headImage = Config.OSS_URL.toString() + mImageName
        }
        // 阿里云使用方式
//        AliYunOss.getInstance(this)?.upload(mImageName, "", null)

    }

    //重写Activity该方法，当窗口焦点变化时自动隐藏system bar，这样可以排除在弹出dialog和menu时，system bar会重新显示的问题。
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    override fun initData() {

        /**
         * haoruigang 2018-3-30 10:31:11   获取验证码接口
         */
//        HttpManager.instance.clientConfig("HttpApiActivity",
//            object : ApiCallBack<BaseModel<String>>() {
//                override fun onSuccess(date: BaseModel<String>?) {
////                    countDown()
//                    //                        String code = date.getData().get(0).getCode();
////                        etCode.setText(code);
//                }
//
//                override fun onFail(statusCode: Int, errorMsg: String?) {
//                    showToast(errorMsg)
//                }
//
//                override fun onError(throwable: Throwable?) {
//                    showToast(throwable.toString(),3000)
//                }
//            })

    }

    // 倒计时
    private fun countDown() {
//        phoneNum = etPhone.getText().toString()
        //        if (!phoneNum.matches("0?(13|14|15|17|18)[0-9]{9}")) {
        if (phoneNum.length != 11) {
            showToast("请输入正确的手机号码")
            return
        }
//        tvGetCode.setClickable(false)
        if (countDownTimer == null) countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(l: Long) {
//                tvGetCode.setText((l / 1000).toString() + "S")
            }

            override fun onFinish() {
//                tvGetCode.setClickable(true)
//                tvGetCode.setText("获取验证码")
//                tvGetCode.setOnClickListener(View.OnClickListener { view: View? -> getRandomCode() })
            }
        }
        countDownTimer!!.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }

    // eventbus 演示
    override fun onEvent(any: Any) {
        (any as ToUIEvent).apply {
            if (tag == ToUIEvent.MESSAGE_EVENT) {
                LogUtils.e("MainActivity===eventbus 演示")
                showToast(obj.toString())
            }
        }
    }

}