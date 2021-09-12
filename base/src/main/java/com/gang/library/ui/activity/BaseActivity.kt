package com.gang.library.ui.activity

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util
import com.gang.library.common.AppManager
import com.gang.library.common.CrashHandler
import com.gang.library.common.EventBus
import com.gang.library.common.utils.notch.CutoutUtil
import com.gang.library.common.utils.notch.callback.CutoutAdapt
import com.gang.library.common.utils.notch.callback.NotchCallback
import com.gang.library.common.utils.permissions.BasePermissionActivity
import com.jaeger.library.StatusBarUtil
import kotlinx.android.synthetic.main.base_title_bar.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * Created by haoruigang on 2018-4-3 09:51:22 继承权限父类
 */
abstract class BaseActivity : BasePermissionActivity() {

    var pageIndex = 1
    var pageSize = "15"
    lateinit var mContext: Context

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        mContext = this

        if (!EventBus.isRegistered(this)) {
            EventBus.register(this) //注册EventBus
        }

        AppManager.appManager?.addActivity(this)
        CrashHandler.instance?.init(this) //初始化全局异常管理
        initData()
        initView(savedInstanceState)
        onClick()
        Notch()
    }

    fun Notch() {
        //刘海屏适配
        // 方案一
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            CutoutUtil.isAllScreenDevice(this,
                object : NotchCallback {
                    override fun Notch(isNotch: Boolean) {
                        if (isNotch) {
                            val lp = window.attributes
                            lp.layoutInDisplayCutoutMode =
                                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                            window.attributes = lp
                            onNotchCreate(this@BaseActivity)
                        }
                    }
                })
        }

        // 方案二 需在manifest中配置
        // 如果是允许全屏显示到刘海屏区域的刘海屏机型
        if (CutoutUtil.allowDisplayToCutout()) { // 如果允许通过显示状态栏方式适配刘海屏
            if (this !is CutoutAdapt) {
                // 需自行将该界面视图元素下移，否则可能会被刘海遮挡
                onNotchCreate(this)
            } else {
                StatusBarUtil.setTranslucent(this, 30)// 状态栏半透明 statusBarAlpha值需要在 0 ~ 255,默认值是112
            }
        }
    }

    /********************* 子类实现  */ // 获取布局文件
    abstract val layoutId: Int

    /**
     * 初始化View
     *
     * @param view
     * @param savedInstanceState
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 刘海屏适配
     */
    open fun onNotchCreate(activity: Activity) {

    }

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化点击事件
     */
    open fun onClick() {
        rl_back_button?.setOnClickListener { finish() }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (EventBus.isRegistered(this)) {
            EventBus.unregister(this) //反注册EventBus
        }
        if (Util.isOnMainThread() && !this.isFinishing) {
            Glide.with(applicationContext).pauseRequests()
        }
    }

    //设置字体为默认大小，不随系统字体大小改而改变
    override fun onConfigurationChanged(newConfig: Configuration) {
        if (newConfig.fontScale != 1f) //非默认值
            resources
        super.onConfigurationChanged(newConfig)
    }

    override fun getResources(): Resources {
        val res = super.getResources()
        if (res.configuration.fontScale != 1f) { //非默认值
            val newConfig = Configuration()
            newConfig.setToDefaults() //设置默认
            res.updateConfiguration(newConfig, res.displayMetrics)
        }
        return res
    }

    //Eventbus
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    open fun onEvent(any: Any) {

    }

}