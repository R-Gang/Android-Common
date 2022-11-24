package com.gang.library.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.gang.library.common.ext.viewext.initViewBinding

/**
 * 基于 [BaseViewModel] 和 [ViewBinding] 管理的 Fragment 基类
 *
 * Created on 2020/4/20.
 *
 * @author o.s
 */
abstract class BaseVBFragment<VB : ViewBinding> : BaseFragment() {
    private var _binding: VB? = null
    private var isInitData = false // 是否初始化数据

    override val layoutId: Int = 0

    protected val mBinding: VB?
        get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = initVB(container, false)
        return _binding?.root
    }

    override fun onResume() {
        super.onResume()
        // 只有在当前Fragment显示可交互时，做数据初始化操作。
        if (!isInitData) {
            isInitData = true
            initData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    open fun initVB(
        container: ViewGroup? = null,
        attachToRoot: Boolean = false,
    ): VB? {
        return initViewBinding(container, false)
    }

}