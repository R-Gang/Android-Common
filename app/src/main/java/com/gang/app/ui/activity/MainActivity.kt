package com.gang.app.ui.activity

import android.app.Activity
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.apkfuns.logutils.LogUtils
import com.gang.app.R
import com.gang.app.common.user.ToUIEvent
import com.gang.app.ui.fragment.HomeFragment
import com.gang.app.ui.fragment.MyFragment
import com.gang.library.common.AppManager
import com.gang.library.common.EventBus
import com.gang.library.common.http.progress.MyProgressDialog
import com.gang.library.common.user.UserManager
import com.gang.library.common.utils.NotifiUtil
import com.gang.library.common.utils.showToast
import com.gang.library.ui.activity.BaseActivity
import com.gang.library.ui.interfaces.Setter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * 1.刘海屏适配示例
 * 2.EventBus使用方式示例
 * 3.判断是否需要开启通知栏功能
 * 4.沉浸式状态栏
 */
@RequiresApi(api = Build.VERSION_CODES.O)
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

        radioButtons.forEach { it.setOnClickListener(this) }
        setChioceItem(position)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //判断是否需要开启通知栏功能
            NotifiUtil.OpenNotificationSetting(this, null, "")
        }

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

    override fun initData() {
        // 添加底部按钮
        radioButtons.add(ll_home_btn)
        radioButtons.add(ll_my_btn)
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
        radioButtons.let { UserManager.INSTANCE.apply(it, TABSPEC, radioButtons[index]) }
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
                LogUtils.e("MainActivity===eventbus 演示")
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

}