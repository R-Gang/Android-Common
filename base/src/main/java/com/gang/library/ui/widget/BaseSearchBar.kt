package com.gang.library.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.Toolbar
import com.gang.library.R
import com.gang.library.common.dimension.dip2px
import com.gang.tools.kotlin.utils.gone
import com.gang.tools.kotlin.utils.show
import org.jetbrains.annotations.NotNull

class BaseSearchBar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defInt: Int = 0,
) : FrameLayout(context, attributeSet, defInt) {
    private val mView = View.inflate(context, R.layout.base_search_bar, null)
    private val bgView = mView.findViewById<Toolbar>(R.id.my_toolbar)
    private val llEmtity = mView.findViewById<ImageView>(R.id.ll_emtity)
    private val leftView = mView.findViewById<RelativeLayout>(R.id.rl_back_button)
    private val leftText = mView.findViewById<TextView>(R.id.tv_title_left)
    private val leftIv = mView.findViewById<ImageView>(R.id.iv_left)
    private val ivSearch = mView.findViewById<ImageView>(R.id.iv_search)
    private val etSearch = mView.findViewById<EditText>(R.id.et_search)
    private val vLine = mView.findViewById<View>(R.id.title_line)

    init {
        addView(mView)
    }


    fun getllEmtity() = llEmtity
    fun getLeftView() = leftView
    fun getLeftText() = leftText
    fun getLeftIv() = leftIv
    fun getSearchView() = etSearch
    fun getSearchIcon() = ivSearch
    fun getVLine() = vLine

    fun setLeftText(text: String) {
        leftView.show()
        leftText.text = text
    }

    fun setLeftIvRes(@DrawableRes @NotNull resId: Int) {
        leftView.show()
        leftIv.setImageResource(resId)
        leftIv.scaleType = ImageView.ScaleType.CENTER_INSIDE
    }

    fun setSearchIv(@DrawableRes @NotNull resId: Int) {
        ivSearch.setImageResource(resId)
        ivSearch.scaleType = ImageView.ScaleType.CENTER_INSIDE
    }

    fun setSearchIcon(@DrawableRes @NotNull resId: Int) {
        etSearch.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0)
    }

    fun goneLine() {
        vLine.gone()
    }

    fun goneLeftView() {
        leftView.gone()
    }

    fun setLLEmtity(height: Float): BaseSearchBar {
        llEmtity.show()
        val llParams = llEmtity.layoutParams as RelativeLayout.LayoutParams
        llParams.height = height.toInt()
        getllEmtity().layoutParams = llParams
        return this
    }

    fun setTooBarHeight(height: Float): BaseSearchBar {
        val param = bgView.layoutParams as ViewGroup.LayoutParams
        param.height = dip2px(height).toInt()
        bgView.layoutParams = param
        return this
    }
}