package com.gang.library.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.gang.library.common.fit.permissions.BasePermissionActivity
import com.gang.library.common.ext.viewext.initViewBinding
import com.gang.library.common.ext.viewext.initViewModel

/**
 * @CreateDate:     2022/7/12 18:19
 * @Author:         haoruigang
 * @ClassName:      BaseVMActivity
 * @Description:    DataBinding Activity封装
 */
abstract class BaseVMActivity<VM : BaseViewModel, VB : ViewBinding> : BasePermissionActivity() {

    protected var mBinding: VB? = null
    protected var mViewModel: VM? = null

    override val layoutId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        mBinding = initVB()
        mViewModel = initVM()
        super.onCreate(savedInstanceState)
    }

    open fun initVM(): VM? {
        return initViewModel()
    }

    open fun initVB(): VB? {
        return initViewBinding()
    }

}