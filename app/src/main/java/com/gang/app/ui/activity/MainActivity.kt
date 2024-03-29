package com.gang.app.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.gang.app.R
import com.gang.app.common.user.ToUIEvent
import com.gang.app.ui.fragment.HomeFragment
import com.gang.app.ui.fragment.MyFragment
import com.gang.library.base.BaseActivity
import com.gang.library.common.AppManager
import com.gang.library.common.EventBus
import com.gang.library.common.store.SpExt
import com.gang.tools.kotlin.interfaces.Setter
import com.gang.tools.kotlin.utils.*
import com.gang.tools.kotlin.utils.NotifiUtil.Companion.OpenNotificationSetting
import com.orhanobut.logger.Logger
import kotlin.system.exitProcess

/**
 * 1.刘海屏适配示例
 * 2.EventBus使用方式示例
 * 3.判断是否需要开启通知栏功能
 * 4.沉浸式状态栏
 */
class MainActivity : BaseActivity(), View.OnClickListener {

    private var radioButtons: MutableList<TextView> = ArrayList()

    private var position = 0
    private var homeFragment: HomeFragment? = null
    private var myFragment: MyFragment? = null

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        window.setFormat(PixelFormat.TRANSLUCENT)
        dark()

        // 添加底部按钮
        radioButtons.add(findViewId(R.id.ll_home_btn))
        radioButtons.add(findViewId(R.id.ll_my_btn))

        radioButtons.forEach { it.setOnClickListener(this) }
        setChioceItem(position)
        @SuppressLint("ObsoleteSdkInt")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //判断是否需要开启通知栏功能
            OpenNotificationSetting(this, null, "")
        }

    }

    override fun initData() {

    }

    override fun onNotchCreate(activity: Activity) {
        //去掉标题
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //全屏显示
//        window.decorView.systemUiVisibility =
//            View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
//                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        // 此处写界面视图元素下移代码，否则可能会被刘海遮挡
//        StatusBarUtil.setTransparent(activity);
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        position = savedInstanceState.getInt("position")
        setChioceItem(position)
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) { //记录当前的position
        super.onSaveInstanceState(outState)
        outState.putInt("position", position)
    }

    fun setChioceItem(index: Int) {
        position = index
        val fragmentManager: FragmentManager = supportFragmentManager
        val ft = fragmentManager.beginTransaction()
        hideFragments(ft)
        applyV(radioButtons, TABSPEC, radioButtons[index])
        when (index) {
            0 ->  //首页
                if (null == homeFragment) {
                    homeFragment = HomeFragment()
                    ft.add(R.id.ll_content, homeFragment!!)
                } else {
                    ft.show(homeFragment!!)
                }
            1 ->  //我的
                if (null == myFragment) {
                    myFragment = MyFragment()
                    ft.add(R.id.ll_content, myFragment!!)
                } else {
                    ft.show(myFragment!!)
                }

        }
        ft.commitAllowingStateLoss()
        // eventbus 演示
        EventBus.postSticky(ToUIEvent(ToUIEvent.MESSAGE_EVENT, index))
    }

    private fun hideFragments(ft: FragmentTransaction) {
        if (null != homeFragment) ft.hide(homeFragment!!)
        if (null != myFragment) ft.hide(myFragment!!)
    }

    //控制normal 状态的当前View 隐藏，其它空间仍然为显示
    val TABSPEC =
        object : Setter<TextView, TextView> {
            override fun set(view: TextView, value: TextView?, index: Int) {
                assert(value != null)
                view.isSelected = view.id == value?.id
            }
        }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ll_home_btn -> setChioceItem(0)
            R.id.ll_my_btn -> setChioceItem(1)
        }
    }

    override fun dispatchKeyEvent(paramKeyEvent: KeyEvent): Boolean {
        if (paramKeyEvent.action == 0
            && paramKeyEvent.keyCode == 4
        ) {
            AppManager.appManager?.finishAllActivity()
        }
        return super.dispatchKeyEvent(paramKeyEvent)
    }

    // eventbus 演示
    override fun onEvent(any: Any) {
        (any as ToUIEvent).apply {
            if (tag == ToUIEvent.MESSAGE_EVENT) {
                Logger.e("MainActivity===eventbus 演示")
                showToast(obj.toString())
            }
        }
    }

    //重写Activity该方法，当窗口焦点变化时自动隐藏system bar，这样可以排除在弹出dialog和menu时，system bar会重新显示的问题。
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideNavigationSystemUI()
        }
    }

    //系统方法
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun exit() {
        if ((currentMilliSecond - vClickTime) > 2000) {
            toastCustom("再按一次返回键退出" + getString(R.string.app_name))
            vClickTime = currentMilliSecond
        } else {
            SpExt.putSpValue("ExitTwice", true) // 两次点击
            Handler().postDelayed({
                finish()
                exitProcess(0)
            }, 100)
        }
    }

}