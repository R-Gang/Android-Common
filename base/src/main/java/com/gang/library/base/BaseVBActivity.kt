package com.gang.library.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.gang.library.common.ext.viewext.initViewBinding

/**
 * @CreateDate:     2022/7/12 18:19
 * @Author:         haoruigang
 * @ClassName:      BaseVMActivity
 * @Description:    ViewBinding Activity封装
 */
abstract class BaseVBActivity<VB : ViewBinding> : BaseActivity() {

    private var _binding: VB? = null

    protected val mBinding: VB?
        get() = _binding

    override val layoutId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = initVB()
        super.onCreate(savedInstanceState)
    }

    open fun initVB(): VB? {
        return initViewBinding()
    }

}