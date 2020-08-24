package com.gang.library.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gang.library.common.user.ToUIEvent
import com.gang.library.common.utils.permissions.BasePermissionFragment
import com.gang.library.ui.activity.BaseActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 基类Fragment
 *
 * @author haoruigang 继承权限父类
 */
abstract class BaseFragment : BasePermissionFragment() {

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
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layoutId, container, false)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this) //注册EventBus
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView(view, savedInstanceState)
        initData()
        onClick()
    }

    /**
     * 该抽象方法就是 onCreateView中需要的layoutId
     *
     * @return
     */
    protected abstract val layoutId: Int

    /**
     * 初始化View
     *
     * @param view
     * @param savedInstanceState
     */
    abstract fun initView(view: View?, savedInstanceState: Bundle?)

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化点击事件
     */
    open fun onClick() {}

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this) //反注册EventBus
        }
    }

    //Eventbus
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    open fun onEvent(event: ToUIEvent?) {
    }
}