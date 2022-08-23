package com.gang.library.ui.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.Toolbar
import com.gang.library.R
import com.gang.tools.kotlin.dimension.dip2px
import com.gang.tools.kotlin.utils.gone
import com.gang.tools.kotlin.utils.show
import org.jetbrains.annotations.NotNull

class BaseTitleBar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defInt: Int = 0,
) : FrameLayout(context, attributeSet, defInt) {
    private val mView = View.inflate(context, R.layout.base_title_bar, null)
    private val bgView = mView.findViewById<Toolbar>(R.id.my_toolbar)
    private val llEmtity = mView.findViewById<ImageView>(R.id.ll_emtity)
    private val leftView = mView.findViewById<RelativeLayout>(R.id.rl_back_button)
    private val leftIv = mView.findViewById<ImageView>(R.id.iv_left)
    private val leftText = mView.findViewById<TextView>(R.id.tv_title_left)
    private val rightView = mView.findViewById<RelativeLayout>(R.id.rl_next_button)
    private val rightIv = mView.findViewById<ImageView>(R.id.iv_right)
    private val rightText = mView.findViewById<TextView>(R.id.tv_title_right)
    private val tvTitle = mView.findViewById<TextView>(R.id.tv_title)
    private val vLine = mView.findViewById<View>(R.id.title_line)

    fun getBgView() = bgView
    fun getllEmtity() = llEmtity
    fun getLeftView() = leftView
    fun getLeftIv() = leftIv
    fun getLeftText() = leftText
    fun getRightView() = rightView
    fun getRightIv() = rightIv
    fun getRightText() = rightText
    fun getTitle() = tvTitle
    fun getVLine() = vLine

    init {
        addView(mView)
    }

    fun setBgColor(@ColorRes @NotNull resId: Int) {
        bgView.setBackgroundColor(context.resources.getColor(resId))
    }

    fun goneLeftView() {
        leftView.gone()
    }

    fun setLeftIvRes(@DrawableRes @NotNull resId: Int) {
        leftView.show()
        leftIv.setImageResource(resId)
        leftIv.background = null
        leftIv.scaleType = ImageView.ScaleType.CENTER_INSIDE
    }


    fun setLeftText(text: String, @ColorRes colorRes: Int = 0) {
        leftView.show()
        leftText.text = text
        if (colorRes != 0) {
            leftText.setTextColor(context.resources.getColor(colorRes))
        }
    }

    fun setRightIvRes(@DrawableRes @NotNull resId: Int) {
        val param = rightIv.layoutParams as RelativeLayout.LayoutParams
        param.rightMargin = dip2px(24f).toInt()
        rightIv.layoutParams = param
        rightView.show()
        rightIv.setImageResource(resId)
        rightIv.scaleType = ImageView.ScaleType.CENTER_INSIDE
    }

    fun setRightText(text: String, @ColorRes colorRes: Int = 0, textSize: Float = 18f) {
        val param = rightText.layoutParams as RelativeLayout.LayoutParams
        param.rightMargin = dip2px(22f).toInt()
        rightText.layoutParams = param
        rightView.show()
        rightText.text = text
        rightText.textSize = textSize
        if (colorRes != 0) {
            rightText.setTextColor(context.resources.getColor(colorRes))
        }
    }

    fun setTitle(text: String, @ColorRes colorRes: Int = 0) {
        tvTitle.text = text
        if (colorRes != 0) {
            tvTitle.setTextColor(context.resources.getColor(colorRes))
        }
    }

    fun goneLine() {
        vLine.gone()
    }

    fun setLeftIvScaleType(type: ImageView.ScaleType = ImageView.ScaleType.CENTER_INSIDE): BaseTitleBar {
        leftIv.scaleType = type
        return this
    }

    fun setRightFinishBtn(
        text: String,
        @ColorRes colorRes: Int = 0,
        @DrawableRes bgResId: Int,
        textSize: Float,
    ): BaseTitleBar {
        val param = rightText.layoutParams as RelativeLayout.LayoutParams
        param.rightMargin = dip2px(20f).toInt()
        param.height = dip2px(24f).toInt()
        rightText.layoutParams = param
        rightText.text = text
        if (colorRes != 0) {
            rightText.setTextColor(context.resources.getColor(colorRes))
        }
        rightText.textSize = textSize
        rightText.gravity = Gravity.CENTER
        rightText.setPadding(dip2px(17f).toInt(), 0, dip2px(17f).toInt(), 0)
        rightText.setBackgroundResource(bgResId)
        return this
    }

    fun setBoldTitle(): BaseTitleBar {
        tvTitle.setTypeface(null, Typeface.BOLD)
        return this
    }

    fun setBoldRightText(): BaseTitleBar {
        rightText.setTypeface(null, Typeface.BOLD)
        return this
    }

    fun setLLEmtity(height: Float): BaseTitleBar {
        llEmtity.show()
        val llParams = llEmtity.layoutParams
        llParams.height = height.toInt()
        getllEmtity().layoutParams = llParams
        return this
    }

    fun setTooBarHeight(height: Float): BaseTitleBar {
        val param = bgView.layoutParams as ViewGroup.LayoutParams
        param.height = dip2px(height).toInt()
        bgView.layoutParams = param
        return this
    }


}