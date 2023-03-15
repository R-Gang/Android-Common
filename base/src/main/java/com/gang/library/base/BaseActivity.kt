package com.gang.library.base

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.gang.library.common.AppManager
import com.gang.library.common.CrashHandler
import com.gang.library.common.EventBus
import com.gang.library.common.fit.notch.CutoutUtil
import com.gang.library.common.fit.notch.callback.CutoutAdapt
import com.gang.library.common.fit.notch.callback.NotchCallback
import com.gang.library.common.user.Config
import com.gang.library.common.user.ToUIEvent
import com.jaeger.library.StatusBarUtil
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * Created by haoruigang on 2018-4-3 09:51:22 继承权限父类
 */
abstract class BaseActivity : AppCompatActivity() {

    var pageIndex = 1
    var pageSize = "15"
    lateinit var mContext: Context

    protected var mNotchScreen: Boolean = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutId > 0) {
            setContentView(layoutId)
        }
        mContext = this

        if (Config.eventBusEnabled) {
            if (!EventBus.isRegistered(this)) {
                EventBus.register(this) //注册EventBus
            }
        }

        if (Config.statusBarEnabled) {
            StatusBarUtil.setTranslucentForImageView(this, 0, null)
        }

        if (Config.activityEnabled) {
            AppManager.appManager?.addActivity(this)
        }
        CrashHandler.instance?.init(this) //初始化全局异常管理
        initView(savedInstanceState)
        initData()
        onClick()
        Notch()
    }

    open fun Notch() {
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
            }
        }
    }

    // 状态栏颜色自适应
    open fun dark() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    // 软键盘弹出内容整体上移
    open fun soft() {
        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN or
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
    }

    //应用运行时，保持屏幕高亮，不锁屏
    open fun screenHighlight() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    //应用运行时，解除保持屏幕高亮，不锁屏
    open fun clearScreenHighlight() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 隐藏导航栏和状态栏通过系统上滑或者下滑拉出导航栏后会自动隐藏，
     * =====
     * decorView 传入自动隐藏的view
     * ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
     * ViewGroup decorChild = (ViewGroup) decorView.getChildAt(0);
     */
    open fun hideSystemUI(decorView: ViewGroup) {
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // 状态栏颜色自适应
                or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                //使用下面三个参数，可以使内容显示在system bar的下面，防止system bar显示或
                //隐藏时，Activity的大小被resize。
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // 隐藏导航栏和状态栏
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    // 隐藏导航栏和状态栏通过系统上滑或者下滑拉出导航栏后会自动隐藏，
    open fun hideSystemUI() {
        val decorView = window.decorView
        decorView.systemUiVisibility =
                //(修改这个选项，可以设置不同模式 。
                // 模式一：SYSTEM_UI_FLAG_IMMERSIVE_STICKY 通过系统上滑或者下滑拉出导航栏后会自动隐藏，
                // 模式二：SYSTEM_UI_FLAG_IMMERSIVE不会自动隐藏)
            (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    // 状态栏颜色自适应
                    or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    //使用下面三个参数，可以使内容显示在system bar的下面，防止system bar显示或
                    //隐藏时，Activity的大小被resize。
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // 隐藏导航栏和状态栏
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    // 隐藏导航栏通过系统上滑或者下滑拉出导航栏后会自动隐藏，
    open fun hideNavigationSystemUI() {
        val decorView = window.decorView
        decorView.systemUiVisibility =
                //(修改这个选项，可以设置不同模式 。
            (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    // 状态栏颜色自适应
                    or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    // 隐藏导航栏
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    // 隐藏状态栏通过系统上滑或者下滑拉出导航栏后会自动隐藏
    open fun hideStableSystemUI() {
        val decorView = window.decorView
        decorView.systemUiVisibility =
                //(修改这个选项，可以设置不同模式 。
            (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    // 状态栏颜色自适应
                    or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // 隐藏状态栏
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }


    //显示system bar， 同时还是希望内容显示在system bar的下方。
    open fun showSystemUI() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    /***************** 子类实现 start ****************/
    /**
     * 该抽象方法就是 onCreate中需要的layoutId
     *
     * @return
     */
    abstract val layoutId: Int  // 获取布局文件

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
     * findViewById
     */
    fun <T : View?> findViewId(v: Int): T {
        return findViewById<T>(v)
    }

    /**
     * 刘海屏适配
     */
    open fun onNotchCreate(activity: Activity) {
        this.mNotchScreen = true // 是全面屏
    }

    /**
     * 初始化点击事件
     */
    open fun onClick() {

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

    override fun onDestroy() {
        super.onDestroy()
        if (Config.eventBusEnabled) {
            if (EventBus.isRegistered(this)) {
                EventBus.unregister(this) //反注册EventBus
            }
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
        if (any is ToUIEvent) any else return
    }

}