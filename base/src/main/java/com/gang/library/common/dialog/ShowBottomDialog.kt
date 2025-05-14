package com.gang.library.common.dialog

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager

/**
 *  底部弹窗滑动BaseDialog
 */
fun <VB : ViewDataBinding> ShowBottomDialog(
    fragmentManager: FragmentManager,
    layoutRes: Int = 0,
    isCancelOutside: Boolean = true,
    actionListener: ((v: VB?) -> Unit)? = null,
): BaseBottomDialog<VB> {
    val mBottomCommentSheetDialog = BaseBottomDialog<VB>()
    mBottomCommentSheetDialog.setFragmentManager(fragmentManager)
        .setLayoutRes(layoutRes)
        .setCancelOutside(isCancelOutside)
        .setViewListener(object : BaseBottomDialog.ViewListener<VB> {
            override fun bindView(view: VB) {
                actionListener?.invoke(view)
            }
        })
    return mBottomCommentSheetDialog
}