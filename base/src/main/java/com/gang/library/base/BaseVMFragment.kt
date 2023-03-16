package com.gang.library.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewbinding.ViewBinding
import androidx.viewpager.widget.ViewPager
import com.gang.library.common.ext.viewext.initViewBinding
import com.gang.library.common.ext.viewext.initViewModel
import com.gang.tools.kotlin.utils.LogUtils

/**
 * 基于 [BaseViewModel] 和 [ViewBinding] 管理的 Fragment 基类
 *
 * Created on 2020/4/20.
 *
 * @author o.s
 */
abstract class BaseVMFragment<VM : BaseViewModel, VB : ViewBinding> : BaseFragment() {

    private var _viewModel: VM? = null
    private var _binding: VB? = null

    var isNotifyRefresh = false // 是否通知fragment刷新
    var refreshData: Any? = null // 刷新时数据，可以是任何类型数据（含null）

    var isReady = false // fragment是否就绪

    protected val mViewModel: VM?
        get() = _viewModel

    protected val mBinding: VB?
        get() = _binding

    override val layoutId: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = initVB(container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _viewModel = initVM()
        view.post {
            isReady = true // fragment已就绪
            if (isNotifyRefresh) {
                onRefresh(refreshData)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            LogUtils.d(this::class.java.simpleName, "hide")
            hide()
        } else {
            LogUtils.d(this::class.java.simpleName, "show")
            show()
        }
    }

    /**
     * 外部调用通知fragment在恰当的时机刷新。可以指定任何参数 [any] 将回调给 [onRefresh]。
     * 子类需要重写 [onRefresh] 方法实现刷新逻辑。
     */
    fun notifyRefresh(any: Any? = null) {
        isNotifyRefresh = true
        this.refreshData = any
        if (isReady) {
            onRefresh(any)
        }
    }

    /**
     * 子类重写，刷新Fragment，可以指定任何参数 [any]，默认实现调用 [show] 方法。
     * [onRefresh] 回调受 [notifyRefresh] 控制：
     * 即：外部每次调用 [notifyRefresh] 都会在恰当的时机触发一次 [onRefresh] 的回调
     */
    open fun onRefresh(any: Any? = null) {
        show()
    }

    /**
     * Fragment隐藏，在切换Fragment时才会触发 [hide] [show]
     */
    open fun hide() {}

    /**
     * Fragment显示，在切换Fragment时才会触发 [show] [hide]
     * 注意：Fragment初始化生命周期过程中不会调用 [show]
     */
    open fun show() {}

    open fun initVM(): VM? {
        return initViewModel()
    }

    open fun initVB(
        container: ViewGroup? = null,
        attachToRoot: Boolean = false,
    ): VB? {
        return initViewBinding(container, false)
    }

    /**
     * 获取当前子Fragment
     */
    fun getSubCurrentFragment(
        fragmentAdapter: FragmentPagerAdapter,
        viewPager: ViewPager?,
    ): BaseVMFragment<*, *>? {
        return if (fragmentAdapter.count > 0) {
            fragmentAdapter.getItem(viewPager?.currentItem ?: 0) as? BaseVMFragment<*, *>
        } else {
            null
        }
    }

}