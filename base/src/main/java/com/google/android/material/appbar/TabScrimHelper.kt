package com.google.android.material.appbar

import android.R
import android.animation.ValueAnimator
import com.google.android.material.appbar.TabScrimHelper
import androidx.core.view.ViewCompat
import android.animation.ValueAnimator.AnimatorUpdateListener
import androidx.core.graphics.ColorUtils
import android.content.res.ColorStateList
import android.graphics.Color
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.tabs.TabLayout

class TabScrimHelper(tabLayout: TabLayout, toolbarLayout: CollapsingToolbarLayout) :
    AppBarLayout.OnOffsetChangedListener {
    private val mTabLayout: TabLayout
    private val mToolbarLayout: CollapsingToolbarLayout
    private var mScrimAnimationDuration: Long = 0
    private var mScrimAlpha = 0
    private var mScrimsAreShown = false
    private var mScrimAnimator: ValueAnimator? = null
    private val mNormalColor: Int
    private val mSelectedColor: Int
    private var mCollapseTabSelectTextColor = 0
    private var mCollapseTabNormalTextColor = 0
    private var mCollapseTabBackgroundColor = 0
    fun setCollapseTabBackgroundColor(collapseTabBackgroundColor: Int) {
        mCollapseTabBackgroundColor = collapseTabBackgroundColor
    }

    fun setCollapseTabSelectTextColor(collapseTabSelectTextColor: Int) {
        mCollapseTabSelectTextColor = collapseTabSelectTextColor
    }

    private fun initDefault() {
        mScrimAnimationDuration = DEFAULT_SCRIM_ANIMATION_DURATION.toLong()
        mCollapseTabBackgroundColor = Color.parseColor("#3F51B5")
        mCollapseTabNormalTextColor = Color.parseColor("#FFFFFF")
        mCollapseTabSelectTextColor = Color.parseColor("#FF4081")
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        setScrimsShown(mToolbarLayout.height + verticalOffset < mToolbarLayout.scrimVisibleHeightTrigger)
    }

    fun setScrimsShown(shown: Boolean) {
        setScrimsShown(shown, ViewCompat.isLaidOut(mToolbarLayout) && !mToolbarLayout.isInEditMode)
    }

    fun setScrimsShown(shown: Boolean, animate: Boolean) {
        if (mScrimsAreShown != shown) {
            if (animate) {
                animateScrim(if (shown) 0xFF else 0x0)
            } else {
                setScrimAlpha(if (shown) 0xFF else 0x0)
            }
            mScrimsAreShown = shown
        }
    }

    private fun animateScrim(targetAlpha: Int) {
        if (mScrimAnimator == null) {
            mScrimAnimator = ValueAnimator()
            mScrimAnimator!!.duration = mScrimAnimationDuration
            mScrimAnimator!!.interpolator =
                if (targetAlpha > mScrimAlpha) AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR else AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR
            mScrimAnimator!!.addUpdateListener { animator -> setScrimAlpha(animator.animatedValue as Int) }
        } else if (mScrimAnimator!!.isRunning) {
            mScrimAnimator!!.cancel()
        }
        mScrimAnimator!!.setIntValues(mScrimAlpha, targetAlpha)
        mScrimAnimator!!.start()
    }

    fun setScrimAlpha(alpha: Int) {
        if (alpha != mScrimAlpha) {
            mScrimAlpha = alpha
            updateLayout()
        }
    }

    private fun updateLayout() {
        val color = ColorUtils.setAlphaComponent(mCollapseTabBackgroundColor, mScrimAlpha)
        mTabLayout.setBackgroundColor(color)
        val i = 1f * mScrimAlpha / 255
        mTabLayout.setTabTextColors(
            ColorUtils.blendARGB(mNormalColor, mCollapseTabNormalTextColor, i),
            ColorUtils.blendARGB(mSelectedColor, mCollapseTabSelectTextColor, i))
    }

    companion object {
        private const val DEFAULT_SCRIM_ANIMATION_DURATION = 600
    }

    init {
        initDefault()
        mTabLayout = tabLayout
        mToolbarLayout = toolbarLayout
        val colorStateList = mTabLayout.tabTextColors
        mSelectedColor = colorStateList!!.getColorForState(intArrayOf(R.attr.state_selected),
            Color.parseColor("#3F51B5"))
        mNormalColor = colorStateList.defaultColor
    }
}