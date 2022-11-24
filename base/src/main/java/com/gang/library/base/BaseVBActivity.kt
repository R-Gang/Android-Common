package com.gang.library.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.gang.library.common.fit.permissions.BasePermissionActivity
import com.gang.library.common.ext.viewext.initViewBinding

/**
 * @CreateDate:     2022/7/12 18:19
 * @Author:         haoruigang
 * @ClassName:      BaseVMActivity
 * @Description:    ViewBinding Activity封装
 */
abstract class BaseVBActivity<VB : ViewBinding> : BasePermissionActivity() {

    protected var mBinding: VB? = null

    override val layoutId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        mBinding = initVB()
        super.onCreate(savedInstanceState)
    }

    open fun initVB(): VB? {
        return initViewBinding()
    }

}