package com.gang.library.ui.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.Toolbar
import com.gang.library.R
import com.gang.library.common.utils.dip2px
import com.gang.library.common.utils.gone
import com.gang.library.common.utils.show
import com.google.android.material.appbar.AppBarLayout
import com.makeramen.roundedimageview.RoundedImageView
import org.jetbrains.annotations.NotNull

class BaseTitleBar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defInt: Int = 0,
) : FrameLayout(context, attributeSet, defInt) {
    private val mView = View.inflate(context, R.layout.base_title_bar, null)
    private val bgView = mView.findViewById<Toolbar>(R.id.my_toolbar)
    private val leftView = mView.findViewById<RelativeLayout>(R.id.rl_back_button)
    private val leftIv = mView.findViewById<ImageView>(R.id.iv_left)
    private val leftRoundIv = mView.findViewById<RoundedImageView>(R.id.iv_title_left)
    private val leftText = mView.findViewById<TextView>(R.id.tv_title_left)
    private val rightView = mView.findViewById<RelativeLayout>(R.id.rl_next_button)
    private val rightIv = mView.findViewById<ImageView>(R.id.iv_right)
    private val rightRoundIv = mView.findViewById<RoundedImageView>(R.id.iv_title_right)
    private val rightText = mView.findViewById<TextView>(R.id.tv_title_right)
    private val tvTitle = mView.findViewById<TextView>(R.id.tv_title)
    private val vLine = mView.findViewById<View>(R.id.title_line)

    fun getBgView() = bgView
    fun getLeftView() = leftView
    fun getLeftIv() = leftIv
    fun getLeftRoundIv() = leftRoundIv
    fun getLeftText() = leftText
    fun getRightView() = rightView
    fun getRightIv() = rightIv
    fun getRightRoundIv() = rightRoundIv
    fun getRightText() = rightText
    fun getTitle() = tvTitle
    fun getVLine() = vLine

    init {
        val param1 = rightView.layoutParams as RelativeLayout.LayoutParams
        param1.width = RelativeLayout.LayoutParams.WRAP_CONTENT
        param1.topMargin = 50
        addView(mView, param1)
        val param = rightView.layoutParams as RelativeLayout.LayoutParams
        param.width = RelativeLayout.LayoutParams.WRAP_CONTENT
        rightView.layoutParams = param
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

    fun setLeftRoundIvRes(@DrawableRes @NotNull resId: Int) {
        leftView.show()
        leftRoundIv.setImageResource(resId)
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
        param.rightMargin = dip2px(24f)
        rightIv.layoutParams = param
        rightView.show()
        rightIv.setImageResource(resId)
        rightIv.scaleType = ImageView.ScaleType.CENTER_INSIDE
    }

    fun setRoundIvRes(@DrawableRes @NotNull resId: Int) {
        rightView.show()
        rightRoundIv.setImageResource(resId)
    }

    fun setRightText(text: String, @ColorRes colorRes: Int = 0, textSize: Float = 18f) {
        val param = rightText.layoutParams as RelativeLayout.LayoutParams
        param.rightMargin = dip2px(22f)
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
        param.rightMargin = dip2px(20f)
        param.height = dip2px(24f)
        rightText.layoutParams = param
        rightText.text = text
        if (colorRes != 0) {
            rightText.setTextColor(context.resources.getColor(colorRes))
        }
        rightText.textSize = textSize
        rightText.gravity = Gravity.CENTER
        rightText.setPadding(dip2px(17f), 0, dip2px(17f), 0)
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

    fun setTooBarHeight(height: Float): BaseTitleBar {
        val param = bgView.layoutParams as AppBarLayout.LayoutParams
        param.height = dip2px(height)
        bgView.layoutParams = param
        return this
    }


}