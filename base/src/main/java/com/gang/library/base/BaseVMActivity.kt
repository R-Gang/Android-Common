package com.gang.library.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.gang.library.common.ext.viewext.initViewBinding
import com.gang.library.common.ext.viewext.initViewModel

/**
 * @CreateDate:     2022/7/12 18:19
 * @Author:         haoruigang
 * @ClassName:      BaseVMActivity
 * @Description:    DataBinding Activity封装
 */
abstract class BaseVMActivity<VM : BaseViewModel, VB : ViewBinding> : BaseActivity() {

    private var _viewModel: VM? = null
    private var _binding: VB? = null

    protected val mViewModel: VM?
        get() = _viewModel

    protected val mBinding: VB?
        get() = _binding

    override val layoutId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = initVB()
        _viewModel = initVM()
        super.onCreate(savedInstanceState)
    }

    open fun initVM(): VM? {
        return initViewModel()
    }

    open fun initVB(): VB? {
        return initViewBinding()
    }

}