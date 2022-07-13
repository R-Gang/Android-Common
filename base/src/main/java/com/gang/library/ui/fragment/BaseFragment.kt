package com.gang.library.ui.fragment

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.gang.library.common.EventBus
import com.gang.library.common.user.Config
import com.gang.library.common.utils.notch.CutoutUtil
import com.gang.library.common.utils.notch.callback.CutoutAdapt
import com.gang.library.common.utils.notch.callback.NotchCallback
import com.gang.library.ui.activity.BaseActivity
import com.jaeger.library.StatusBarUtil
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 基类Fragment
 *
 * @author haoruigang 继承权限父类
 */
abstract class BaseFragment : Fragment() {

    lateinit var mActivity: BaseActivity
    var mContext: Context? = null
    var pageIndex = 1
    var pageSize = "15"

    /**
     * 获得全局的，防止使用getActivity()为空
     *
     * @param context
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as BaseActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        if (Config.eventBusEnabled) {
            if (!EventBus.isRegistered(this)) {
                EventBus.register(this) //注册EventBus
            }
        }
        if (Config.setContentView) {
            val view = inflater.inflate(layoutId, container, false)
            return view
        } else {
            return super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (Config.statusBarEnabled) {
            StatusBarUtil.setTranslucentForImageView(mActivity, 0, null)
        }

        initData()
        initView(view, savedInstanceState)
        onClick()
        Notch()
    }

    open fun Notch() {
        //刘海屏适配
        // 方案一
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            CutoutUtil.isAllScreenDevice(mActivity,
                object : NotchCallback {
                    override fun Notch(isNotch: Boolean) {
                        if (isNotch) {
                            val lp = mActivity.window.attributes
                            lp.layoutInDisplayCutoutMode =
                                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                            mActivity.window.attributes = lp
                            onNotchCreate(mActivity)
                        }
                    }
                })
        }

        // 方案二 需在manifest中配置
        // 如果是允许全屏显示到刘海屏区域的刘海屏机型
        if (CutoutUtil.allowDisplayToCutout()) { // 如果允许通过显示状态栏方式适配刘海屏
            if (this !is CutoutAdapt) {
                // 需自行将该界面视图元素下移，否则可能会被刘海遮挡
                onNotchCreate(mActivity)
            }
        }
    }

    /**
     * 该抽象方法就是 onCreateView中需要的layoutId
     *
     * @return
     */
    protected abstract val layoutId: Int

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * findViewById
     */
    fun <T : View?> findViewId(v: Int): T {
        return mActivity.findViewById<T>(v)
    }

    /**
     * 初始化View
     *
     * @param view
     * @param savedInstanceState
     */
    abstract fun initView(view: View?, savedInstanceState: Bundle?)

    /**
     * 刘海屏适配
     */
    open fun onNotchCreate(activity: Activity) {

    }

    /**
     * 初始化点击事件
     */
    open fun onClick() {}

    override fun onDestroy() {
        super.onDestroy()
        if (Config.eventBusEnabled) {
            if (EventBus.isRegistered(this)) {
                EventBus.unregister(this) //反注册EventBus
            }
        }
    }

    //Eventbus
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    open fun onEvent(any: Any) {
    }
}