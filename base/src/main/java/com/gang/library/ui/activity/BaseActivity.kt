package com.gang.library.ui.activity

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util
import com.gang.library.common.AppManager
import com.gang.library.common.CrashHandler
import com.gang.library.common.user.ToUIEvent
import com.gang.library.common.utils.permissions.BasePermissionActivity
import com.jaeger.library.StatusBarUtil
import kotlinx.android.synthetic.main.base_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this) //注册EventBus
        }
        AppManager.appManager?.addActivity(this)
        CrashHandler.instance?.init(this) //初始化全局异常管理
        StatusBarUtil.setTranslucent(this, 30)// 状态栏半透明 statusBarAlpha值需要在 0 ~ 255,默认值是112
        initView(savedInstanceState)
        initData()
        onClick()
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
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this) //反注册EventBus
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
    open fun onEvent(event: ToUIEvent?) {
    }
}