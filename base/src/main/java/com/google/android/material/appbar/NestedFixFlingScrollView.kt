package com.google.android.material.appbar

import android.R
import android.content.Context
import kotlin.jvm.JvmOverloads
import android.widget.FrameLayout
import androidx.core.view.NestedScrollingParent
import androidx.core.view.NestedScrollingChild
import androidx.core.view.ScrollingView
import com.google.android.material.appbar.NestedFixFlingScrollView
import androidx.core.widget.ScrollerCompat
import androidx.core.widget.EdgeEffectCompat
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import android.view.View.MeasureSpec
import androidx.core.view.MotionEventCompat
import androidx.core.view.VelocityTrackerCompat
import androidx.core.view.InputDeviceCompat
import android.util.TypedValue
import android.view.ViewGroup.MarginLayoutParams
import android.os.Parcelable
import android.os.Parcel
import androidx.core.view.AccessibilityDelegateCompat
import android.os.Bundle
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import android.widget.ScrollView
import android.view.accessibility.AccessibilityEvent
import androidx.core.view.accessibility.AccessibilityRecordCompat
import androidx.core.view.accessibility.AccessibilityEventCompat
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils

/**
 * Add this class to fix the scroll view can not fling by the action up has checked by mIsBeingDragged
 *
 * case MotionEvent.ACTION_UP:
 * if (mIsBeingDragged) {
 * final VelocityTracker velocityTracker = mVelocityTracker;
 * velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
 * int initialVelocity = (int) VelocityTrackerCompat.getYVelocity(velocityTracker,
 * mActivePointerId);
 *
 * if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
 * flingWithNestedDispatch(-initialVelocity);
 * } else if (mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0,
 * getScrollRange())) {
 * ViewCompat.postInvalidateOnAnimation(this);
 * }
 * }
 */
class NestedFixFlingScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), NestedScrollingParent, NestedScrollingChild,
    ScrollingView {
    /**
     * Interface definition for a callback to be invoked when the scroll
     * X or Y positions of a view change.
     *
     *
     * This version of the interface works on all versions of Android, back to API v4.
     *
     * @see .setOnScrollChangeListener
     */
    interface OnScrollChangeListener {
        /**
         * Called when the scroll position of a view changes.
         *
         * @param v The view whose scroll position has changed.
         * @param scrollX Current horizontal scroll origin.
         * @param scrollY Current vertical scroll origin.
         * @param oldScrollX Previous horizontal scroll origin.
         * @param oldScrollY Previous vertical scroll origin.
         */
        fun onScrollChange(
            v: NestedFixFlingScrollView?, scrollX: Int, scrollY: Int,
            oldScrollX: Int, oldScrollY: Int
        )
    }

    private var mLastScroll: Long = 0
    private val mTempRect = Rect()
    private var mScroller: ScrollerCompat? = null
    private var mEdgeGlowTop: EdgeEffectCompat? = null
    private var mEdgeGlowBottom: EdgeEffectCompat? = null

    /**
     * Position of the last motion event.
     */
    private var mLastMotionY = 0

    /**
     * True when the layout has changed but the traversal has not come through yet.
     * Ideally the view hierarchy would keep track of this for us.
     */
    private var mIsLayoutDirty = true
    private var mIsLaidOut = false

    /**
     * The child to give focus to in the event that a child has requested focus while the
     * layout is dirty. This prevents the scroll from being wrong if the child has not been
     * laid out before requesting focus.
     */
    private var mChildToScrollTo: View? = null

    /**
     * True if the user is currently dragging this ScrollView around. This is
     * not the same as 'is being flinged', which can be checked by
     * mScroller.isFinished() (flinging begins when the user lifts his finger).
     */
    private var mIsBeingDragged = false

    /**
     * Determines speed during touch scrolling
     */
    private var mVelocityTracker: VelocityTracker? = null

    /**
     * When set to true, the scroll view measure its child to make it fill the currently
     * visible area.
     */
    private var mFillViewport = false
    /**
     * @return Whether arrow scrolling will animate its transition.
     */
    /**
     * Set whether arrow scrolling will animate its transition.
     * @param smoothScrollingEnabled whether arrow scrolling will animate its transition
     */
    /**
     * Whether arrow scrolling is animated.
     */
    var isSmoothScrollingEnabled = true
    private var mTouchSlop = 0
    private var mMinimumVelocity = 0
    private var mMaximumVelocity = 0

    /**
     * ID of the active pointer. This is used to retain consistency during
     * drags/flings if multiple pointers are used.
     */
    private var mActivePointerId = INVALID_POINTER

    /**
     * Used during scrolling to retrieve the new offset within the window.
     */
    private val mScrollOffset = IntArray(2)
    private val mScrollConsumed = IntArray(2)
    private var mNestedYOffset = 0
    private var mSavedState: SavedState? = null
    private val mParentHelper: NestedScrollingParentHelper
    private val mChildHelper: NestedScrollingChildHelper
    private var mVerticalScrollFactor = 0f
    private var mOnScrollChangeListener: OnScrollChangeListener? = null

    // NestedScrollingChild
    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mChildHelper.isNestedScrollingEnabled = enabled
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return mChildHelper.isNestedScrollingEnabled
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return mChildHelper.startNestedScroll(axes)
    }

    override fun stopNestedScroll() {
        mChildHelper.stopNestedScroll()
    }

    override fun hasNestedScrollingParent(): Boolean {
        return mChildHelper.hasNestedScrollingParent()
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
        dyUnconsumed: Int, offsetInWindow: IntArray?
    ): Boolean {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
            offsetInWindow)
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?
    ): Boolean {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }

    override fun dispatchNestedFling(
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY)
    }

    // NestedScrollingParent
    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedScrollAccepted(child: View, target: View, nestedScrollAxes: Int) {
        mParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes)
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
    }

    override fun onStopNestedScroll(target: View) {
        mParentHelper.onStopNestedScroll(target)
        stopNestedScroll()
    }

    override fun onNestedScroll(
        target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        val oldScrollY = scrollY
        scrollBy(0, dyUnconsumed)
        val myConsumed = scrollY - oldScrollY
        val myUnconsumed = dyUnconsumed - myConsumed
        dispatchNestedScroll(0, myConsumed, 0, myUnconsumed, null)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        dispatchNestedPreScroll(dx, dy, consumed, null)
    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        if (!consumed) {
            flingWithNestedDispatch(velocityY.toInt())
            return true
        }
        return false
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return dispatchNestedPreFling(velocityX, velocityY)
    }

    override fun getNestedScrollAxes(): Int {
        return mParentHelper.nestedScrollAxes
    }

    // ScrollView import
    override fun shouldDelayChildPressedState(): Boolean {
        return true
    }

    override fun getTopFadingEdgeStrength(): Float {
        if (childCount == 0) {
            return 0.0f
        }
        val length = verticalFadingEdgeLength
        val scrollY = scrollY
        return if (scrollY < length) {
            scrollY / length.toFloat()
        } else 1.0f
    }

    override fun getBottomFadingEdgeStrength(): Float {
        if (childCount == 0) {
            return 0.0f
        }
        val length = verticalFadingEdgeLength
        val bottomEdge = height - paddingBottom
        val span = getChildAt(0).bottom - scrollY - bottomEdge
        return if (span < length) {
            span / length.toFloat()
        } else 1.0f
    }

    /**
     * @return The maximum amount this scroll view will scroll in response to
     * an arrow event.
     */
    val maxScrollAmount: Int
        get() = (MAX_SCROLL_FACTOR * height).toInt()

    private fun initScrollView() {
        mScroller = ScrollerCompat.create(context, null)
        isFocusable = true
        descendantFocusability = FOCUS_AFTER_DESCENDANTS
        setWillNotDraw(false)
        val configuration = ViewConfiguration.get(context)
        mTouchSlop = configuration.scaledTouchSlop
        mMinimumVelocity = configuration.scaledMinimumFlingVelocity
        mMaximumVelocity = configuration.scaledMaximumFlingVelocity
    }

    override fun addView(child: View) {
        check(childCount <= 0) { "ScrollView can host only one direct child" }
        super.addView(child)
    }

    override fun addView(child: View, index: Int) {
        check(childCount <= 0) { "ScrollView can host only one direct child" }
        super.addView(child, index)
    }

    override fun addView(child: View, params: ViewGroup.LayoutParams) {
        check(childCount <= 0) { "ScrollView can host only one direct child" }
        super.addView(child, params)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        check(childCount <= 0) { "ScrollView can host only one direct child" }
        super.addView(child, index, params)
    }

    /**
     * Register a callback to be invoked when the scroll X or Y positions of
     * this view change.
     *
     * This version of the method works on all versions of Android, back to API v4.
     *
     * @param l The listener to notify when the scroll X or Y position changes.
     * @see android.view.View.getScrollX
     * @see android.view.View.getScrollY
     */
    fun setOnScrollChangeListener(l: OnScrollChangeListener?) {
        mOnScrollChangeListener = l
    }

    /**
     * @return Returns true this ScrollView can be scrolled
     */
    private fun canScroll(): Boolean {
        val child = getChildAt(0)
        if (child != null) {
            val childHeight = child.height
            return height < childHeight + paddingTop + paddingBottom
        }
        return false
    }
    /**
     * Indicates whether this ScrollView's content is stretched to fill the viewport.
     *
     * @return True if the content fills the viewport, false otherwise.
     *
     * @attr name android:fillViewport
     */
    /**
     * Set whether this ScrollView should stretch its content height to fill the viewport or not.
     *
     * @param fillViewport True to stretch the content's height to the viewport's
     * boundaries, false otherwise.
     *
     * @attr name android:fillViewport
     */
    var isFillViewport: Boolean
        get() = mFillViewport
        set(fillViewport) {
            if (fillViewport != mFillViewport) {
                mFillViewport = fillViewport
                requestLayout()
            }
        }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (mOnScrollChangeListener != null) {
            mOnScrollChangeListener!!.onScrollChange(this, l, t, oldl, oldt)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!mFillViewport) {
            return
        }
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (heightMode == MeasureSpec.UNSPECIFIED) {
            return
        }
        if (childCount > 0) {
            val child = getChildAt(0)
            var height = measuredHeight
            if (child.measuredHeight < height) {
                val lp = child.layoutParams as LayoutParams
                val childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                    paddingLeft + paddingRight, lp.width)
                height -= paddingTop
                height -= paddingBottom
                val childHeightMeasureSpec =
                    MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
            }
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        // Let the focused view and/or our descendants get the key first
        return super.dispatchKeyEvent(event) || executeKeyEvent(event)
    }

    /**
     * You can call this function yourself to have the scroll view perform
     * scrolling from a key event, just as if the event had been dispatched to
     * it by the view hierarchy.
     *
     * @param event The key event to execute.
     * @return Return true if the event was handled, else false.
     */
    fun executeKeyEvent(event: KeyEvent): Boolean {
        mTempRect.setEmpty()
        if (!canScroll()) {
            if (isFocused && event.keyCode != KeyEvent.KEYCODE_BACK) {
                var currentFocused = findFocus()
                if (currentFocused === this) currentFocused = null
                val nextFocused = FocusFinder.getInstance().findNextFocus(this,
                    currentFocused, FOCUS_DOWN)
                return nextFocused != null && nextFocused !== this && nextFocused.requestFocus(
                    FOCUS_DOWN)
            }
            return false
        }
        var handled = false
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_DPAD_UP -> handled = if (!event.isAltPressed) {
                    arrowScroll(FOCUS_UP)
                } else {
                    fullScroll(FOCUS_UP)
                }
                KeyEvent.KEYCODE_DPAD_DOWN -> handled = if (!event.isAltPressed) {
                    arrowScroll(FOCUS_DOWN)
                } else {
                    fullScroll(FOCUS_DOWN)
                }
                KeyEvent.KEYCODE_SPACE -> pageScroll(if (event.isShiftPressed) FOCUS_UP else FOCUS_DOWN)
            }
        }
        return handled
    }

    private fun inChild(x: Int, y: Int): Boolean {
        if (childCount > 0) {
            val scrollY = scrollY
            val child = getChildAt(0)
            return !(y < child.top - scrollY || y >= child.bottom - scrollY || x < child.left || x >= child.right)
        }
        return false
    }

    private fun initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        } else {
            mVelocityTracker!!.clear()
        }
    }

    private fun initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
    }

    private fun recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }

    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        if (disallowIntercept) {
            recycleVelocityTracker()
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        /*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onMotionEvent will be called and we do the actual
         * scrolling there.
         */

        /*
        * Shortcut the most recurring case: the user is in the dragging
        * state and he is moving his finger.  We want to intercept this
        * motion.
        */
        val action = ev.action
        if (action == MotionEvent.ACTION_MOVE && mIsBeingDragged) {
            return true
        }
        when (action and MotionEventCompat.ACTION_MASK) {
            MotionEvent.ACTION_MOVE -> {

                /*
                 * mIsBeingDragged == false, otherwise the shortcut would have caught it. Check
                 * whether the user has moved far enough from his original down touch.
                 */

                /*
                * Locally do absolute value. mLastMotionY is set to the y value
                * of the down event.
                */
                val activePointerId = mActivePointerId
                // If we don't have a valid id, the touch down wasn't on content.
                if (activePointerId == INVALID_POINTER) return false
                val pointerIndex = ev.findPointerIndex(activePointerId)
                if (pointerIndex == -1) {
                    Log.e(TAG, "Invalid pointerId=" + activePointerId
                            + " in onInterceptTouchEvent")
                    return false
                }
                val y = ev.getY(pointerIndex).toInt()
                val yDiff = Math.abs(y - mLastMotionY)
                if (yDiff > mTouchSlop
                    && nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL == 0
                ) {
                    mIsBeingDragged = true
                    mLastMotionY = y
                    initVelocityTrackerIfNotExists()
                    mVelocityTracker!!.addMovement(ev)
                    mNestedYOffset = 0
                    val parent = parent
                    parent?.requestDisallowInterceptTouchEvent(true)
                }
            }
            MotionEvent.ACTION_DOWN -> {
                val y = ev.y.toInt()
                if (!inChild(ev.x.toInt(), y)) {
                    mIsBeingDragged = false
                    recycleVelocityTracker()
                    return false
                }

                /*
                 * Remember location of down touch.
                 * ACTION_DOWN always refers to pointer index 0.
                 */mLastMotionY = y
                mActivePointerId = ev.getPointerId(0)
                initOrResetVelocityTracker()
                mVelocityTracker!!.addMovement(ev)
                /*
                 * If being flinged and user touches the screen, initiate drag;
                 * otherwise don't. mScroller.isFinished should be false when
                 * being flinged. We need to call computeScrollOffset() first so that
                 * isFinished() is correct.
                */mScroller!!.computeScrollOffset()
                mIsBeingDragged = !mScroller!!.isFinished
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                /* Release the drag */mIsBeingDragged = false
                mActivePointerId = INVALID_POINTER
                recycleVelocityTracker()
                if (mScroller!!.springBack(scrollX, scrollY, 0, 0, 0, scrollRange)) {
                    ViewCompat.postInvalidateOnAnimation(this)
                }
                stopNestedScroll()
            }
            MotionEventCompat.ACTION_POINTER_UP -> onSecondaryPointerUp(ev)
        }

        /*
        * The only time we want to intercept motion events is if we are in the
        * drag mode.
        */return mIsBeingDragged
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        initVelocityTrackerIfNotExists()
        val vtev = MotionEvent.obtain(ev)
        val actionMasked = MotionEventCompat.getActionMasked(ev)
        if (actionMasked == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0
        }
        vtev.offsetLocation(0f, mNestedYOffset.toFloat())
        when (actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (childCount == 0) {
                    return false
                }
                if (!mScroller!!.isFinished.also { mIsBeingDragged = it }) {
                    val parent = parent
                    parent?.requestDisallowInterceptTouchEvent(true)
                }

                /*
                 * If being flinged and user touches, stop the fling. isFinished
                 * will be false if being flinged.
                 */if (!mScroller!!.isFinished) {
                    mScroller!!.abortAnimation()
                }

                // Remember where the motion event started
                mLastMotionY = ev.y.toInt()
                mActivePointerId = ev.getPointerId(0)
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
            }
            MotionEvent.ACTION_MOVE -> {
                val activePointerIndex = ev.findPointerIndex(mActivePointerId)
                if (activePointerIndex == -1) {
                    Log.e(TAG, "Invalid pointerId=$mActivePointerId in onTouchEvent")
                    return false
                }
                val y = ev.getY(activePointerIndex).toInt()
                var deltaY = mLastMotionY - y
                if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
                    deltaY -= mScrollConsumed[1]
                    vtev.offsetLocation(0f, mScrollOffset[1].toFloat())
                    mNestedYOffset += mScrollOffset[1]
                }
                if (!mIsBeingDragged && Math.abs(deltaY) > mTouchSlop) {
                    val parent = parent
                    parent?.requestDisallowInterceptTouchEvent(true)
                    mIsBeingDragged = true
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop
                    } else {
                        deltaY += mTouchSlop
                    }
                }
                if (mIsBeingDragged) {
                    // Scroll to follow the motion event
                    mLastMotionY = y - mScrollOffset[1]
                    val oldY = scrollY
                    val range = scrollRange
                    val overscrollMode = overScrollMode
                    val canOverscroll = (overscrollMode == OVER_SCROLL_ALWAYS
                            || overscrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && range > 0)

                    // Calling overScrollByCompat will call onOverScrolled, which
                    // calls onScrollChanged if applicable.
                    if (overScrollByCompat(0, deltaY, 0, scrollY, 0, range, 0,
                            0, true) && !hasNestedScrollingParent()
                    ) {
                        // Break our velocity if we hit a scroll barrier.
                        mVelocityTracker!!.clear()
                    }
                    val scrolledDeltaY = scrollY - oldY
                    val unconsumedY = deltaY - scrolledDeltaY
                    if (dispatchNestedScroll(0, scrolledDeltaY, 0, unconsumedY, mScrollOffset)) {
                        mLastMotionY -= mScrollOffset[1]
                        vtev.offsetLocation(0f, mScrollOffset[1].toFloat())
                        mNestedYOffset += mScrollOffset[1]
                    } else if (canOverscroll) {
                        ensureGlows()
                        val pulledToY = oldY + deltaY
                        if (pulledToY < 0) {
                            mEdgeGlowTop!!.onPull(deltaY.toFloat() / height,
                                ev.getX(activePointerIndex) / width)
                            if (!mEdgeGlowBottom!!.isFinished) {
                                mEdgeGlowBottom!!.onRelease()
                            }
                        } else if (pulledToY > range) {
                            mEdgeGlowBottom!!.onPull(deltaY.toFloat() / height,
                                1f - ev.getX(activePointerIndex)
                                        / width)
                            if (!mEdgeGlowTop!!.isFinished) {
                                mEdgeGlowTop!!.onRelease()
                            }
                        }
                        if (mEdgeGlowTop != null
                            && (!mEdgeGlowTop!!.isFinished || !mEdgeGlowBottom!!.isFinished)
                        ) {
                            ViewCompat.postInvalidateOnAnimation(this)
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                val velocityTracker = mVelocityTracker
                velocityTracker!!.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                val initialVelocity = VelocityTrackerCompat.getYVelocity(velocityTracker,
                    mActivePointerId).toInt()
                if (Math.abs(initialVelocity) > mMinimumVelocity) {
                    flingWithNestedDispatch(-initialVelocity)
                } else if (mScroller!!.springBack(scrollX, scrollY, 0, 0, 0,
                        scrollRange)
                ) {
                    ViewCompat.postInvalidateOnAnimation(this)
                }
                mActivePointerId = INVALID_POINTER
                endDrag()
            }
            MotionEvent.ACTION_CANCEL -> {
                if (mIsBeingDragged && childCount > 0) {
                    if (mScroller!!.springBack(scrollX, scrollY, 0, 0, 0,
                            scrollRange)
                    ) {
                        ViewCompat.postInvalidateOnAnimation(this)
                    }
                }
                mActivePointerId = INVALID_POINTER
                endDrag()
            }
            MotionEventCompat.ACTION_POINTER_DOWN -> {
                val index = MotionEventCompat.getActionIndex(ev)
                mLastMotionY = ev.getY(index).toInt()
                mActivePointerId = ev.getPointerId(index)
            }
            MotionEventCompat.ACTION_POINTER_UP -> {
                onSecondaryPointerUp(ev)
                mLastMotionY = ev.getY(ev.findPointerIndex(mActivePointerId)).toInt()
            }
        }
        if (mVelocityTracker != null) {
            mVelocityTracker!!.addMovement(vtev)
        }
        vtev.recycle()
        return true
    }

    private fun onSecondaryPointerUp(ev: MotionEvent) {
        val pointerIndex = (ev.action and MotionEventCompat.ACTION_POINTER_INDEX_MASK
                shr MotionEventCompat.ACTION_POINTER_INDEX_SHIFT)
        val pointerId = ev.getPointerId(pointerIndex)
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            // TODO: Make this decision more intelligent.
            val newPointerIndex = if (pointerIndex == 0) 1 else 0
            mLastMotionY = ev.getY(newPointerIndex).toInt()
            mActivePointerId = ev.getPointerId(newPointerIndex)
            if (mVelocityTracker != null) {
                mVelocityTracker!!.clear()
            }
        }
    }

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        if (event.source and InputDeviceCompat.SOURCE_CLASS_POINTER != 0) {
            when (event.action) {
                MotionEventCompat.ACTION_SCROLL -> {
                    if (!mIsBeingDragged) {
                        val vscroll = MotionEventCompat.getAxisValue(event,
                            MotionEventCompat.AXIS_VSCROLL)
                        if (vscroll != 0f) {
                            val delta = (vscroll * verticalScrollFactorCompat).toInt()
                            val range = scrollRange
                            val oldScrollY = scrollY
                            var newScrollY = oldScrollY - delta
                            if (newScrollY < 0) {
                                newScrollY = 0
                            } else if (newScrollY > range) {
                                newScrollY = range
                            }
                            if (newScrollY != oldScrollY) {
                                super.scrollTo(scrollX, newScrollY)
                                return true
                            }
                        }
                    }
                }
            }
        }
        return false
    }

    private val verticalScrollFactorCompat: Float
        private get() {
            if (mVerticalScrollFactor == 0f) {
                val outValue = TypedValue()
                val context = context
                check(context.theme.resolveAttribute(
                    R.attr.listPreferredItemHeight,
                    outValue,
                    true)) { "Expected theme to define listPreferredItemHeight." }
                mVerticalScrollFactor = outValue.getDimension(
                    context.resources.displayMetrics)
            }
            return mVerticalScrollFactor
        }

    override fun onOverScrolled(
        scrollX: Int, scrollY: Int,
        clampedX: Boolean, clampedY: Boolean
    ) {
        super.scrollTo(scrollX, scrollY)
    }

    fun overScrollByCompat(
        deltaX: Int, deltaY: Int,
        scrollX: Int, scrollY: Int,
        scrollRangeX: Int, scrollRangeY: Int,
        maxOverScrollX: Int, maxOverScrollY: Int,
        isTouchEvent: Boolean
    ): Boolean {
        var maxOverScrollX = maxOverScrollX
        var maxOverScrollY = maxOverScrollY
        val overScrollMode = overScrollMode
        val canScrollHorizontal = computeHorizontalScrollRange() > computeHorizontalScrollExtent()
        val canScrollVertical = computeVerticalScrollRange() > computeVerticalScrollExtent()
        val overScrollHorizontal = (overScrollMode == OVER_SCROLL_ALWAYS
                || overScrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && canScrollHorizontal)
        val overScrollVertical = (overScrollMode == OVER_SCROLL_ALWAYS
                || overScrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && canScrollVertical)
        var newScrollX = scrollX + deltaX
        if (!overScrollHorizontal) {
            maxOverScrollX = 0
        }
        var newScrollY = scrollY + deltaY
        if (!overScrollVertical) {
            maxOverScrollY = 0
        }

        // Clamp values if at the limits and record
        val left = -maxOverScrollX
        val right = maxOverScrollX + scrollRangeX
        val top = -maxOverScrollY
        val bottom = maxOverScrollY + scrollRangeY
        var clampedX = false
        if (newScrollX > right) {
            newScrollX = right
            clampedX = true
        } else if (newScrollX < left) {
            newScrollX = left
            clampedX = true
        }
        var clampedY = false
        if (newScrollY > bottom) {
            newScrollY = bottom
            clampedY = true
        } else if (newScrollY < top) {
            newScrollY = top
            clampedY = true
        }
        if (clampedY) {
            mScroller!!.springBack(newScrollX, newScrollY, 0, 0, 0, scrollRange)
        }
        onOverScrolled(newScrollX, newScrollY, clampedX, clampedY)
        return clampedX || clampedY
    }

    val scrollRange: Int
        get() {
            var scrollRange = 0
            if (childCount > 0) {
                val child = getChildAt(0)
                scrollRange = Math.max(0,
                    child.height - (height - paddingBottom - paddingTop))
            }
            return scrollRange
        }

    /**
     *
     *
     * Finds the next focusable component that fits in the specified bounds.
     *
     *
     * @param topFocus look for a candidate is the one at the top of the bounds
     * if topFocus is true, or at the bottom of the bounds if topFocus is
     * false
     * @param top      the top offset of the bounds in which a focusable must be
     * found
     * @param bottom   the bottom offset of the bounds in which a focusable must
     * be found
     * @return the next focusable component in the bounds or null if none can
     * be found
     */
    private fun findFocusableViewInBounds(topFocus: Boolean, top: Int, bottom: Int): View? {
        val focusables: List<View> = getFocusables(FOCUS_FORWARD)
        var focusCandidate: View? = null

        /*
         * A fully contained focusable is one where its top is below the bound's
         * top, and its bottom is above the bound's bottom. A partially
         * contained focusable is one where some part of it is within the
         * bounds, but it also has some part that is not within bounds.  A fully contained
         * focusable is preferred to a partially contained focusable.
         */
        var foundFullyContainedFocusable = false
        val count = focusables.size
        for (i in 0 until count) {
            val view = focusables[i]
            val viewTop = view.top
            val viewBottom = view.bottom
            if (top < viewBottom && viewTop < bottom) {
                /*
                 * the focusable is in the target area, it is a candidate for
                 * focusing
                 */
                val viewIsFullyContained = top < viewTop && viewBottom < bottom
                if (focusCandidate == null) {
                    /* No candidate, take this one */
                    focusCandidate = view
                    foundFullyContainedFocusable = viewIsFullyContained
                } else {
                    val viewIsCloserToBoundary = (topFocus && viewTop < focusCandidate.top
                            || !topFocus && viewBottom > focusCandidate.bottom)
                    if (foundFullyContainedFocusable) {
                        if (viewIsFullyContained && viewIsCloserToBoundary) {
                            /*
                             * We're dealing with only fully contained views, so
                             * it has to be closer to the boundary to beat our
                             * candidate
                             */
                            focusCandidate = view
                        }
                    } else {
                        if (viewIsFullyContained) {
                            /* Any fully contained view beats a partially contained view */
                            focusCandidate = view
                            foundFullyContainedFocusable = true
                        } else if (viewIsCloserToBoundary) {
                            /*
                             * Partially contained view beats another partially
                             * contained view if it's closer
                             */
                            focusCandidate = view
                        }
                    }
                }
            }
        }
        return focusCandidate
    }

    /**
     *
     * Handles scrolling in response to a "page up/down" shortcut press. This
     * method will scroll the view by one page up or down and give the focus
     * to the topmost/bottommost component in the new visible area. If no
     * component is a good candidate for focus, this scrollview reclaims the
     * focus.
     *
     * @param direction the scroll direction: [android.view.View.FOCUS_UP]
     * to go one page up or
     * [android.view.View.FOCUS_DOWN] to go one page down
     * @return true if the key event is consumed by this method, false otherwise
     */
    fun pageScroll(direction: Int): Boolean {
        val down = direction == FOCUS_DOWN
        val height = height
        if (down) {
            mTempRect.top = scrollY + height
            val count = childCount
            if (count > 0) {
                val view = getChildAt(count - 1)
                if (mTempRect.top + height > view.bottom) {
                    mTempRect.top = view.bottom - height
                }
            }
        } else {
            mTempRect.top = scrollY - height
            if (mTempRect.top < 0) {
                mTempRect.top = 0
            }
        }
        mTempRect.bottom = mTempRect.top + height
        return scrollAndFocus(direction, mTempRect.top, mTempRect.bottom)
    }

    /**
     *
     * Handles scrolling in response to a "home/end" shortcut press. This
     * method will scroll the view to the top or bottom and give the focus
     * to the topmost/bottommost component in the new visible area. If no
     * component is a good candidate for focus, this scrollview reclaims the
     * focus.
     *
     * @param direction the scroll direction: [android.view.View.FOCUS_UP]
     * to go the top of the view or
     * [android.view.View.FOCUS_DOWN] to go the bottom
     * @return true if the key event is consumed by this method, false otherwise
     */
    fun fullScroll(direction: Int): Boolean {
        val down = direction == FOCUS_DOWN
        val height = height
        mTempRect.top = 0
        mTempRect.bottom = height
        if (down) {
            val count = childCount
            if (count > 0) {
                val view = getChildAt(count - 1)
                mTempRect.bottom = view.bottom + paddingBottom
                mTempRect.top = mTempRect.bottom - height
            }
        }
        return scrollAndFocus(direction, mTempRect.top, mTempRect.bottom)
    }

    /**
     *
     * Scrolls the view to make the area defined by `top` and
     * `bottom` visible. This method attempts to give the focus
     * to a component visible in this area. If no component can be focused in
     * the new visible area, the focus is reclaimed by this ScrollView.
     *
     * @param direction the scroll direction: [android.view.View.FOCUS_UP]
     * to go upward, [android.view.View.FOCUS_DOWN] to downward
     * @param top       the top offset of the new area to be made visible
     * @param bottom    the bottom offset of the new area to be made visible
     * @return true if the key event is consumed by this method, false otherwise
     */
    private fun scrollAndFocus(direction: Int, top: Int, bottom: Int): Boolean {
        var handled = true
        val height = height
        val containerTop = scrollY
        val containerBottom = containerTop + height
        val up = direction == FOCUS_UP
        var newFocused = findFocusableViewInBounds(up, top, bottom)
        if (newFocused == null) {
            newFocused = this
        }
        if (top >= containerTop && bottom <= containerBottom) {
            handled = false
        } else {
            val delta = if (up) top - containerTop else bottom - containerBottom
            doScrollY(delta)
        }
        if (newFocused !== findFocus()) newFocused.requestFocus(direction)
        return handled
    }

    /**
     * Handle scrolling in response to an up or down arrow click.
     *
     * @param direction The direction corresponding to the arrow key that was
     * pressed
     * @return True if we consumed the event, false otherwise
     */
    fun arrowScroll(direction: Int): Boolean {
        var currentFocused = findFocus()
        if (currentFocused === this) currentFocused = null
        val nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction)
        val maxJump = maxScrollAmount
        if (nextFocused != null && isWithinDeltaOfScreen(nextFocused, maxJump, height)) {
            nextFocused.getDrawingRect(mTempRect)
            offsetDescendantRectToMyCoords(nextFocused, mTempRect)
            val scrollDelta = computeScrollDeltaToGetChildRectOnScreen(mTempRect)
            doScrollY(scrollDelta)
            nextFocused.requestFocus(direction)
        } else {
            // no new focus
            var scrollDelta = maxJump
            if (direction == FOCUS_UP && scrollY < scrollDelta) {
                scrollDelta = scrollY
            } else if (direction == FOCUS_DOWN) {
                if (childCount > 0) {
                    val daBottom = getChildAt(0).bottom
                    val screenBottom = scrollY + height - paddingBottom
                    if (daBottom - screenBottom < maxJump) {
                        scrollDelta = daBottom - screenBottom
                    }
                }
            }
            if (scrollDelta == 0) {
                return false
            }
            doScrollY(if (direction == FOCUS_DOWN) scrollDelta else -scrollDelta)
        }
        if (currentFocused != null && currentFocused.isFocused
            && isOffScreen(currentFocused)
        ) {
            // previously focused item still has focus and is off screen, give
            // it up (take it back to ourselves)
            // (also, need to temporarily force FOCUS_BEFORE_DESCENDANTS so we are
            // sure to
            // get it)
            val descendantFocusability = descendantFocusability // save
            setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS)
            requestFocus()
            setDescendantFocusability(descendantFocusability) // restore
        }
        return true
    }

    /**
     * @return whether the descendant of this scroll view is scrolled off
     * screen.
     */
    private fun isOffScreen(descendant: View): Boolean {
        return !isWithinDeltaOfScreen(descendant, 0, height)
    }

    /**
     * @return whether the descendant of this scroll view is within delta
     * pixels of being on the screen.
     */
    private fun isWithinDeltaOfScreen(descendant: View, delta: Int, height: Int): Boolean {
        descendant.getDrawingRect(mTempRect)
        offsetDescendantRectToMyCoords(descendant, mTempRect)
        return (mTempRect.bottom + delta >= scrollY
                && mTempRect.top - delta <= scrollY + height)
    }

    /**
     * Smooth scroll by a Y delta
     *
     * @param delta the number of pixels to scroll by on the Y axis
     */
    private fun doScrollY(delta: Int) {
        if (delta != 0) {
            if (isSmoothScrollingEnabled) {
                smoothScrollBy(0, delta)
            } else {
                scrollBy(0, delta)
            }
        }
    }

    /**
     * Like [View.scrollBy], but scroll smoothly instead of immediately.
     *
     * @param dx the number of pixels to scroll by on the X axis
     * @param dy the number of pixels to scroll by on the Y axis
     */
    fun smoothScrollBy(dx: Int, dy: Int) {
        var dy = dy
        if (childCount == 0) {
            // Nothing to do.
            return
        }
        val duration = AnimationUtils.currentAnimationTimeMillis() - mLastScroll
        if (duration > ANIMATED_SCROLL_GAP) {
            val height = height - paddingBottom - paddingTop
            val bottom = getChildAt(0).height
            val maxY = Math.max(0, bottom - height)
            val scrollY = scrollY
            dy = Math.max(0, Math.min(scrollY + dy, maxY)) - scrollY
            mScroller!!.startScroll(scrollX, scrollY, 0, dy)
            ViewCompat.postInvalidateOnAnimation(this)
        } else {
            if (!mScroller!!.isFinished) {
                mScroller!!.abortAnimation()
            }
            scrollBy(dx, dy)
        }
        mLastScroll = AnimationUtils.currentAnimationTimeMillis()
    }

    /**
     * Like [.scrollTo], but scroll smoothly instead of immediately.
     *
     * @param x the position where to scroll on the X axis
     * @param y the position where to scroll on the Y axis
     */
    fun smoothScrollTo(x: Int, y: Int) {
        smoothScrollBy(x - scrollX, y - scrollY)
    }

    /**
     *
     * The scroll range of a scroll view is the overall height of all of its
     * children.
     * @hide
     */
    override fun computeVerticalScrollRange(): Int {
        val count = childCount
        val contentHeight = height - paddingBottom - paddingTop
        if (count == 0) {
            return contentHeight
        }
        var scrollRange = getChildAt(0).bottom
        val scrollY = scrollY
        val overscrollBottom = Math.max(0, scrollRange - contentHeight)
        if (scrollY < 0) {
            scrollRange -= scrollY
        } else if (scrollY > overscrollBottom) {
            scrollRange += scrollY - overscrollBottom
        }
        return scrollRange
    }

    /** @hide
     */
    override fun computeVerticalScrollOffset(): Int {
        return Math.max(0, super.computeVerticalScrollOffset())
    }

    /** @hide
     */
    override fun computeVerticalScrollExtent(): Int {
        return super.computeVerticalScrollExtent()
    }

    /** @hide
     */
    override fun computeHorizontalScrollRange(): Int {
        return super.computeHorizontalScrollRange()
    }

    /** @hide
     */
    override fun computeHorizontalScrollOffset(): Int {
        return super.computeHorizontalScrollOffset()
    }

    /** @hide
     */
    override fun computeHorizontalScrollExtent(): Int {
        return super.computeHorizontalScrollExtent()
    }

    override fun measureChild(
        child: View, parentWidthMeasureSpec: Int,
        parentHeightMeasureSpec: Int
    ) {
        val lp = child.layoutParams
        val childWidthMeasureSpec: Int
        val childHeightMeasureSpec: Int
        childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, paddingLeft
                + paddingRight, lp.width)
        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
    }

    override fun measureChildWithMargins(
        child: View, parentWidthMeasureSpec: Int, widthUsed: Int,
        parentHeightMeasureSpec: Int, heightUsed: Int
    ) {
        val lp = child.layoutParams as MarginLayoutParams
        val childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
            paddingLeft + paddingRight + lp.leftMargin + lp.rightMargin
                    + widthUsed, lp.width)
        val childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
            lp.topMargin + lp.bottomMargin, MeasureSpec.UNSPECIFIED)
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
    }

    override fun computeScroll() {
        if (mScroller!!.computeScrollOffset()) {
            val oldX = scrollX
            val oldY = scrollY
            val x = mScroller!!.currX
            val y = mScroller!!.currY
            if (oldX != x || oldY != y) {
                val range = scrollRange
                val overscrollMode = overScrollMode
                val canOverscroll = (overscrollMode == OVER_SCROLL_ALWAYS
                        || overscrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && range > 0)
                overScrollByCompat(x - oldX, y - oldY, oldX, oldY, 0, range,
                    0, 0, false)
                if (canOverscroll) {
                    ensureGlows()
                    if (y <= 0 && oldY > 0) {
                        mEdgeGlowTop!!.onAbsorb(mScroller!!.currVelocity.toInt())
                    } else if (y >= range && oldY < range) {
                        mEdgeGlowBottom!!.onAbsorb(mScroller!!.currVelocity.toInt())
                    }
                }
            }
        }
    }

    /**
     * Scrolls the view to the given child.
     *
     * @param child the View to scroll to
     */
    private fun scrollToChild(child: View) {
        child.getDrawingRect(mTempRect)

        /* Offset from child's local coordinates to ScrollView coordinates */offsetDescendantRectToMyCoords(
            child,
            mTempRect)
        val scrollDelta = computeScrollDeltaToGetChildRectOnScreen(mTempRect)
        if (scrollDelta != 0) {
            scrollBy(0, scrollDelta)
        }
    }

    /**
     * If rect is off screen, scroll just enough to get it (or at least the
     * first screen size chunk of it) on screen.
     *
     * @param rect      The rectangle.
     * @param immediate True to scroll immediately without animation
     * @return true if scrolling was performed
     */
    private fun scrollToChildRect(rect: Rect, immediate: Boolean): Boolean {
        val delta = computeScrollDeltaToGetChildRectOnScreen(rect)
        val scroll = delta != 0
        if (scroll) {
            if (immediate) {
                scrollBy(0, delta)
            } else {
                smoothScrollBy(0, delta)
            }
        }
        return scroll
    }

    /**
     * Compute the amount to scroll in the Y direction in order to get
     * a rectangle completely on the screen (or, if taller than the screen,
     * at least the first screen size chunk of it).
     *
     * @param rect The rect.
     * @return The scroll delta.
     */
    protected fun computeScrollDeltaToGetChildRectOnScreen(rect: Rect): Int {
        if (childCount == 0) return 0
        val height = height
        var screenTop = scrollY
        var screenBottom = screenTop + height
        val fadingEdge = verticalFadingEdgeLength

        // leave room for top fading edge as long as rect isn't at very top
        if (rect.top > 0) {
            screenTop += fadingEdge
        }

        // leave room for bottom fading edge as long as rect isn't at very bottom
        if (rect.bottom < getChildAt(0).height) {
            screenBottom -= fadingEdge
        }
        var scrollYDelta = 0
        if (rect.bottom > screenBottom && rect.top > screenTop) {
            // need to move down to get it in view: move down just enough so
            // that the entire rectangle is in view (or at least the first
            // screen size chunk).
            scrollYDelta += if (rect.height() > height) {
                // just enough to get screen size chunk on
                rect.top - screenTop
            } else {
                // get entire rect at bottom of screen
                rect.bottom - screenBottom
            }

            // make sure we aren't scrolling beyond the end of our content
            val bottom = getChildAt(0).bottom
            val distanceToBottom = bottom - screenBottom
            scrollYDelta = Math.min(scrollYDelta, distanceToBottom)
        } else if (rect.top < screenTop && rect.bottom < screenBottom) {
            // need to move up to get it in view: move up just enough so that
            // entire rectangle is in view (or at least the first screen
            // size chunk of it).
            scrollYDelta -= if (rect.height() > height) {
                // screen size chunk
                screenBottom - rect.bottom
            } else {
                // entire rect at top
                screenTop - rect.top
            }

            // make sure we aren't scrolling any further than the top our content
            scrollYDelta = Math.max(scrollYDelta, -scrollY)
        }
        return scrollYDelta
    }

    override fun requestChildFocus(child: View, focused: View) {
        if (!mIsLayoutDirty) {
            scrollToChild(focused)
        } else {
            // The child may not be laid out yet, we can't compute the scroll yet
            mChildToScrollTo = focused
        }
        super.requestChildFocus(child, focused)
    }

    /**
     * When looking for focus in children of a scroll view, need to be a little
     * more careful not to give focus to something that is scrolled off screen.
     *
     * This is more expensive than the default [android.view.ViewGroup]
     * implementation, otherwise this behavior might have been made the default.
     */
    override fun onRequestFocusInDescendants(
        direction: Int,
        previouslyFocusedRect: Rect
    ): Boolean {

        // convert from forward / backward notation to up / down / left / right
        // (ugh).
        var direction = direction
        if (direction == FOCUS_FORWARD) {
            direction = FOCUS_DOWN
        } else if (direction == FOCUS_BACKWARD) {
            direction = FOCUS_UP
        }
        val nextFocus = if (previouslyFocusedRect == null) FocusFinder.getInstance()
            .findNextFocus(this, null, direction) else FocusFinder.getInstance()
            .findNextFocusFromRect(
                this, previouslyFocusedRect, direction)
        if (nextFocus == null) {
            return false
        }
        return if (isOffScreen(nextFocus)) {
            false
        } else nextFocus.requestFocus(direction, previouslyFocusedRect)
    }

    override fun requestChildRectangleOnScreen(
        child: View, rectangle: Rect,
        immediate: Boolean
    ): Boolean {
        // offset into coordinate space of this scroll view
        rectangle.offset(child.left - child.scrollX,
            child.top - child.scrollY)
        return scrollToChildRect(rectangle, immediate)
    }

    override fun requestLayout() {
        mIsLayoutDirty = true
        super.requestLayout()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        mIsLayoutDirty = false
        // Give a child focus if it needs it
        if (mChildToScrollTo != null && isViewDescendantOf(
                mChildToScrollTo!!, this)
        ) {
            scrollToChild(mChildToScrollTo!!)
        }
        mChildToScrollTo = null
        if (!mIsLaidOut) {
            if (mSavedState != null) {
                scrollTo(scrollX, mSavedState!!.scrollPosition)
                mSavedState = null
            } // mScrollY default value is "0"
            val childHeight = if (childCount > 0) getChildAt(0).measuredHeight else 0
            val scrollRange = Math.max(0,
                childHeight - (b - t - paddingBottom - paddingTop))

            // Don't forget to clamp
            if (scrollY > scrollRange) {
                scrollTo(scrollX, scrollRange)
            } else if (scrollY < 0) {
                scrollTo(scrollX, 0)
            }
        }

        // Calling this with the present values causes it to re-claim them
        scrollTo(scrollX, scrollY)
        mIsLaidOut = true
    }

    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mIsLaidOut = false
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val currentFocused = findFocus()
        if (null == currentFocused || this === currentFocused) {
            return
        }

        // If the currently-focused view was visible on the screen when the
        // screen was at the old height, then scroll the screen to make that
        // view visible with the new screen height.
        if (isWithinDeltaOfScreen(currentFocused, 0, oldh)) {
            currentFocused.getDrawingRect(mTempRect)
            offsetDescendantRectToMyCoords(currentFocused, mTempRect)
            val scrollDelta = computeScrollDeltaToGetChildRectOnScreen(mTempRect)
            doScrollY(scrollDelta)
        }
    }

    /**
     * Fling the scroll view
     *
     * @param velocityY The initial velocity in the Y direction. Positive
     * numbers mean that the finger/cursor is moving down the screen,
     * which means we want to scroll towards the top.
     */
    fun fling(velocityY: Int) {
        if (childCount > 0) {
            val height = height - paddingBottom - paddingTop
            val bottom = getChildAt(0).height
            mScroller!!.fling(scrollX, scrollY, 0, velocityY, 0, 0, 0,
                Math.max(0, bottom - height), 0, height / 2)
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    private fun flingWithNestedDispatch(velocityY: Int) {
        val scrollY = scrollY
        val canFling = ((scrollY > 0 || velocityY > 0)
                && (scrollY < scrollRange || velocityY < 0))
        if (!dispatchNestedPreFling(0f, velocityY.toFloat())) {
            dispatchNestedFling(0f, velocityY.toFloat(), canFling)
            if (canFling) {
                fling(velocityY)
            }
        }
    }

    private fun endDrag() {
        mIsBeingDragged = false
        recycleVelocityTracker()
        stopNestedScroll()
        if (mEdgeGlowTop != null) {
            mEdgeGlowTop!!.onRelease()
            mEdgeGlowBottom!!.onRelease()
        }
    }

    /**
     * {@inheritDoc}
     *
     *
     * This version also clamps the scrolling to the bounds of our child.
     */
    override fun scrollTo(x: Int, y: Int) {
        // we rely on the fact the View.scrollBy calls scrollTo.
        var x = x
        var y = y
        if (childCount > 0) {
            val child = getChildAt(0)
            x = clamp(x, width - paddingRight - paddingLeft, child.width)
            y = clamp(y, height - paddingBottom - paddingTop, child.height)
            if (x != scrollX || y != scrollY) {
                super.scrollTo(x, y)
            }
        }
    }

    private fun ensureGlows() {
        if (overScrollMode != OVER_SCROLL_NEVER) {
            if (mEdgeGlowTop == null) {
                val context = context
                mEdgeGlowTop = EdgeEffectCompat(context)
                mEdgeGlowBottom = EdgeEffectCompat(context)
            }
        } else {
            mEdgeGlowTop = null
            mEdgeGlowBottom = null
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (mEdgeGlowTop != null) {
            val scrollY = scrollY
            if (!mEdgeGlowTop!!.isFinished) {
                val restoreCount = canvas.save()
                val width = width - paddingLeft - paddingRight
                canvas.translate(paddingLeft.toFloat(), Math.min(0, scrollY).toFloat())
                mEdgeGlowTop!!.setSize(width, height)
                if (mEdgeGlowTop!!.draw(canvas)) {
                    ViewCompat.postInvalidateOnAnimation(this)
                }
                canvas.restoreToCount(restoreCount)
            }
            if (!mEdgeGlowBottom!!.isFinished) {
                val restoreCount = canvas.save()
                val width = width - paddingLeft - paddingRight
                val height = height
                canvas.translate((-width + paddingLeft).toFloat(), (
                        Math.max(scrollRange, scrollY) + height).toFloat())
                canvas.rotate(180f, width.toFloat(), 0f)
                mEdgeGlowBottom!!.setSize(width, height)
                if (mEdgeGlowBottom!!.draw(canvas)) {
                    ViewCompat.postInvalidateOnAnimation(this)
                }
                canvas.restoreToCount(restoreCount)
            }
        }
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        val ss = state
        super.onRestoreInstanceState(ss.superState)
        mSavedState = ss
        requestLayout()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.scrollPosition = scrollY
        return ss
    }

    internal class SavedState : BaseSavedState {
        var scrollPosition = 0

        constructor(superState: Parcelable?) : super(superState) {}
        constructor(source: Parcel) : super(source) {
            scrollPosition = source.readInt()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(scrollPosition)
        }

        override fun toString(): String {
            return ("HorizontalScrollView.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " scrollPosition=" + scrollPosition + "}")
        }

        companion object {
            val CREATOR: Parcelable.Creator<SavedState?> = object : Parcelable.Creator<SavedState?> {
                override fun createFromParcel(`in`: Parcel): SavedState? {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    internal class AccessibilityDelegate : AccessibilityDelegateCompat() {
        override fun performAccessibilityAction(
            host: View,
            action: Int,
            arguments: Bundle
        ): Boolean {
            if (super.performAccessibilityAction(host, action, arguments)) {
                return true
            }
            val nsvHost = host as NestedFixFlingScrollView
            if (!nsvHost.isEnabled) {
                return false
            }
            when (action) {
                AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD -> {
                    run {
                        val viewportHeight = (nsvHost.height - nsvHost.paddingBottom
                                - nsvHost.paddingTop)
                        val targetScrollY = Math.min(nsvHost.scrollY + viewportHeight,
                            nsvHost.scrollRange)
                        if (targetScrollY != nsvHost.scrollY) {
                            nsvHost.smoothScrollTo(0, targetScrollY)
                            return true
                        }
                    }
                    return false
                }
                AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD -> {
                    run {
                        val viewportHeight = (nsvHost.height - nsvHost.paddingBottom
                                - nsvHost.paddingTop)
                        val targetScrollY = Math.max(nsvHost.scrollY - viewportHeight, 0)
                        if (targetScrollY != nsvHost.scrollY) {
                            nsvHost.smoothScrollTo(0, targetScrollY)
                            return true
                        }
                    }
                    return false
                }
            }
            return false
        }

        override fun onInitializeAccessibilityNodeInfo(
            host: View,
            info: AccessibilityNodeInfoCompat
        ) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            val nsvHost = host as NestedFixFlingScrollView
            info.className = ScrollView::class.java.name
            if (nsvHost.isEnabled) {
                val scrollRange = nsvHost.scrollRange
                if (scrollRange > 0) {
                    info.isScrollable = true
                    if (nsvHost.scrollY > 0) {
                        info.addAction(AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD)
                    }
                    if (nsvHost.scrollY < scrollRange) {
                        info.addAction(AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD)
                    }
                }
            }
        }

        override fun onInitializeAccessibilityEvent(host: View, event: AccessibilityEvent) {
            super.onInitializeAccessibilityEvent(host, event)
            val nsvHost = host as NestedFixFlingScrollView
            event.className = ScrollView::class.java.name
            val record = AccessibilityEventCompat.asRecord(event)
            val scrollable = nsvHost.scrollRange > 0
            record.isScrollable = scrollable
            record.scrollX = nsvHost.scrollX
            record.scrollY = nsvHost.scrollY
            record.maxScrollX = nsvHost.scrollX
            record.maxScrollY = nsvHost.scrollRange
        }
    }

    companion object {
        const val ANIMATED_SCROLL_GAP = 250
        const val MAX_SCROLL_FACTOR = 0.5f
        private const val TAG = "NestedScrollView"

        /**
         * Sentinel value for no current active pointer.
         * Used by [.mActivePointerId].
         */
        private const val INVALID_POINTER = -1
        private val ACCESSIBILITY_DELEGATE = AccessibilityDelegate()
        private val SCROLLVIEW_STYLEABLE = intArrayOf(
            R.attr.fillViewport
        )

        /**
         * Return true if child is a descendant of parent, (or equal to the parent).
         */
        private fun isViewDescendantOf(child: View, parent: View): Boolean {
            if (child === parent) {
                return true
            }
            val theParent = child.parent
            return theParent is ViewGroup && isViewDescendantOf(theParent as View, parent)
        }

        private fun clamp(n: Int, my: Int, child: Int): Int {
            if (my >= child || n < 0) {
                /* my >= child is this case:
             *                    |--------------- me ---------------|
             *     |------ child ------|
             * or
             *     |--------------- me ---------------|
             *            |------ child ------|
             * or
             *     |--------------- me ---------------|
             *                                  |------ child ------|
             *
             * n < 0 is this case:
             *     |------ me ------|
             *                    |-------- child --------|
             *     |-- mScrollX --|
             */
                return 0
            }
            return if (my + n > child) {
                /* this case:
             *                    |------ me ------|
             *     |------ child ------|
             *     |-- mScrollX --|
             */
                child - my
            } else n
        }
    }

    init {
        initScrollView()
        val a = context.obtainStyledAttributes(
            attrs, SCROLLVIEW_STYLEABLE, defStyleAttr, 0)
        isFillViewport = a.getBoolean(0, false)
        a.recycle()
        mParentHelper = NestedScrollingParentHelper(this)
        mChildHelper = NestedScrollingChildHelper(this)

        // ...because why else would you be using this widget?
        isNestedScrollingEnabled = true
        ViewCompat.setAccessibilityDelegate(this, ACCESSIBILITY_DELEGATE)
    }
}