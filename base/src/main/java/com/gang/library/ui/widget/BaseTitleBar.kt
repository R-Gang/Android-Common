package com.gang.library.ui.widget

import android.app.Activity
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
import com.gang.tools.kotlin.dimension.dp
import com.gang.tools.kotlin.utils.gone
import com.gang.tools.kotlin.utils.show
import com.gang.tools.kotlin.utils.vClick
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

    fun getBgView(): Toolbar = bgView
    fun getllEmtity(): ImageView = llEmtity
    fun getLeftView(): RelativeLayout = leftView
    fun getLeftIv(): ImageView = leftIv
    fun getLeftText(): TextView = leftText
    fun getRightView(): RelativeLayout = rightView
    fun getRightIv(): ImageView = rightIv
    fun getRightText(): TextView = rightText
    fun getTitle(): TextView = tvTitle
    fun getVLine(): View = vLine

    init {
        addView(mView)
    }

    fun setBgColor(@ColorRes @NotNull resId: Int) {
        bgView.setBackgroundColor(context.resources.getColor(resId))
    }

    fun setTooBarHeight(height: Int): BaseTitleBar {
        val param = bgView.layoutParams as ViewGroup.LayoutParams
        param.height = height
        bgView.layoutParams = param
        return this
    }

    fun setLLEmtity(height: Int): BaseTitleBar {
        llEmtity.show()
        val llParams = llEmtity.layoutParams
        llParams.height = height
        llEmtity.layoutParams = llParams
        return this
    }

    fun backLFinish(activity: Activity) {
        leftView?.vClick { activity.finish() }
    }

    fun setTitle(
        text: String,
        @ColorRes colorRes: Int = 0,
        textSize: Float = 18f,
        tf: Typeface = Typeface.DEFAULT,
        style: Int = Typeface.NORMAL,
    ) {
        tvTitle.text = text
        if (colorRes != 0) {
            tvTitle.setTextColor(context.resources.getColor(colorRes))
        }
        tvTitle.textSize = textSize
        tvTitle.setTypeface(tf, style)
    }

    fun goneLine() {
        vLine.gone()
    }

    fun goneLeftView() {
        leftView.gone()
    }

    fun setLeftIvRes(
        @DrawableRes resId: Int,
        type: ImageView.ScaleType = ImageView.ScaleType.CENTER_INSIDE,
    ) {
        leftView.show()
        leftIv.setImageResource(resId)
        leftIv.scaleType = type
    }


    fun setLeftText(
        text: String,
        @ColorRes colorRes: Int = 0,
        textSize: Float = 18f,
        tf: Typeface = Typeface.DEFAULT,
        style: Int = Typeface.NORMAL,
    ) {
        leftView.show()
        leftText.text = text
        leftText.textSize = textSize
        if (colorRes != 0) {
            leftText.setTextColor(context.resources.getColor(colorRes))
        }
        leftText.setTypeface(tf, style)
    }

    fun setRightIvRes(
        @DrawableRes resId: Int,
        type: ImageView.ScaleType = ImageView.ScaleType.CENTER_INSIDE,
    ) {
        rightView.show()
        rightIv.setImageResource(resId)
        rightIv.scaleType = type
    }

    fun setRightIvParams(params: RelativeLayout.LayoutParams?) {
        var param = rightIv.layoutParams as RelativeLayout.LayoutParams
        if (params == null) {
            param.rightMargin = 24.dp
        } else {
            param = params
        }
        rightIv.layoutParams = param
    }

    fun setRightText(
        text: String,
        @ColorRes colorRes: Int = 0,
        textSize: Float = 18f,
        tf: Typeface = Typeface.DEFAULT,
        style: Int = Typeface.NORMAL,
    ) {
        rightView.show()
        rightText.text = text
        rightText.textSize = textSize
        if (colorRes != 0) {
            rightText.setTextColor(context.resources.getColor(colorRes))
        }
        rightText.setTypeface(tf, style)
    }

    fun setRightViewParams(params: RelativeLayout.LayoutParams?) {
        var param = rightView.layoutParams as RelativeLayout.LayoutParams
        if (params == null) {
            param.rightMargin = 22.dp
        } else {
            param = params
        }
        rightView.layoutParams = param
    }

    fun setRightTextParams(params: RelativeLayout.LayoutParams?) {
        var param = rightText.layoutParams as RelativeLayout.LayoutParams
        if (params == null) {
            param.rightMargin = 22.dp
        } else {
            param = params
        }
        rightText.layoutParams = param
    }

    fun setRightButton(
        text: String,
        @ColorRes colorRes: Int = 0,
        @DrawableRes bgResId: Int,
        textSize: Float = 18f,
        gravity: Int = Gravity.CENTER,
        tf: Typeface = Typeface.DEFAULT,
        style: Int = Typeface.NORMAL,
        leftPadding: Int = 0,
        topPadding: Int = 0,
        rightPadding: Int = 0,
        bottomPadding: Int = 0,
    ): BaseTitleBar {
        rightView.show()
        rightText.text = text
        if (colorRes != 0) {
            rightText.setTextColor(context.resources.getColor(colorRes))
        }
        rightText.textSize = textSize
        rightText.gravity = gravity
        rightText.setPadding(leftPadding, topPadding, rightPadding, bottomPadding)
        rightText.setBackgroundResource(bgResId)
        rightText.setTypeface(tf, style)
        return this
    }


}