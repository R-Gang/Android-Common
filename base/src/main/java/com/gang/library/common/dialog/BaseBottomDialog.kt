package com.gang.library.common.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.gang.library.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * @CreateDate:     2023/3/29 19:44
 * @Author:         haoruigang
 * @ClassName:      BaseBottomDialog
 * @Description:    底部滑动BaseDialog
 */
open class BaseBottomDialog<VB : ViewDataBinding> : BottomSheetDialogFragment() {

    private val TAG = "BaseBottomDialog"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setCanceledOnTouchOutside(cancelOutside)
        val view = DataBindingUtil.inflate<VB>(
            LayoutInflater.from(context), layoutResId, container, false
        )
        bindView(view)
        return view.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.apply {
            window?.apply {
                //设置 dialog 的宽高
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, attributes.height)
                //设置 dialog 的背景为 null
                /*setBackgroundDrawableResource(R.color.transparent)
                setDimAmount(0f)  // 背景透明*/

                // 设置软键盘不自动弹出
                setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                            or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                )
                setOnDismissListener {
                    if (behaviorChanged != null) {
                        behaviorChanged?.changedState(null, BottomSheetBehavior.STATE_COLLAPSED)
                    }
                }
                val bottomSheet: FrameLayout? = findViewById(R.id.design_bottom_sheet)
                if (bottomSheet != null) {
                    // 默认背景颜色去掉，不然圆角显示不见
                    bottomSheet.background = ColorDrawable(Color.TRANSPARENT)
                    val behavior = BottomSheetBehavior.from(bottomSheet)
                    // 初始为展开状态
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    behavior.peekHeight = 0
                    if (behaviorChanged != null) {
                        behaviorChanged?.changedState(null, BottomSheetBehavior.STATE_EXPANDED)
                    }
                    behavior.setBottomSheetCallback(object :
                        BottomSheetBehavior.BottomSheetCallback() {
                        override fun onStateChanged(bottomSheet: View, newState: Int) {
                            if (newState == BottomSheetBehavior.STATE_HIDDEN
                                || newState == BottomSheetBehavior.STATE_COLLAPSED
                            ) {
                                this@BaseBottomDialog.dismiss()
                                /*//关闭弹窗
                                behavior.state = BottomSheetBehavior.STATE_HIDDEN;*/
                            }
                            if (behaviorChanged != null) behaviorChanged?.changedState(
                                bottomSheet,
                                newState
                            )
                        }

                        override fun onSlide(bottomSheet: View, slideOffset: Float) {
                            if (behaviorChanged != null) behaviorChanged?.changedOffset(
                                bottomSheet,
                                slideOffset
                            )
                        }
                    })
                }
            }
        }
    }

    @LayoutRes
    var layoutResId: Int = 0
        private set

    var behaviorChanged: IBehaviorChanged? = null

    interface IBehaviorChanged {
        fun changedState(bottomSheet: View?, state: Int)
        fun changedOffset(bottomSheet: View?, slideOffset: Float)
    }


    var mFragmentManager: FragmentManager? = null
    fun setFragmentManager(manager: FragmentManager): BaseBottomDialog<VB> {
        mFragmentManager = manager
        return this
    }

    private var mViewListener: ViewListener<VB>? = null
    fun bindView(view: VB) {
        if (mViewListener != null) {
            mViewListener?.bindView(view)
        }
    }

    interface ViewListener<VB> {
        fun bindView(view: VB)
    }

    fun setViewListener(listener: ViewListener<VB>): BaseBottomDialog<VB> {
        mViewListener = listener
        return this
    }

    fun setLayoutRes(@LayoutRes layoutRes: Int): BaseBottomDialog<VB> {
        this.layoutResId = layoutRes
        return this
    }

    var cancelOutside: Boolean = true  // 默认可取消
    fun setCancelOutside(cancel: Boolean): BaseBottomDialog<VB> {
        cancelOutside = cancel
        return this
    }

    var fragmentTag: String = TAG
    fun setTag(tag: String): BaseBottomDialog<VB> {
        fragmentTag = tag
        return this
    }


    var isShow = false
    fun isShowing(): Boolean {
        return isShow
    }

    fun show() {
        if (mFragmentManager != null && !isShowing()) {
            mFragmentManager?.let { show(it, fragmentTag) }
            isShow = true
        }
    }

    override fun dismiss() {
        super.dismiss()
        isShow = false
    }

}