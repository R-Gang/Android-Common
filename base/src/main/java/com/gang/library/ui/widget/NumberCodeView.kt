package com.gang.library.ui.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.text.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.collection.SparseArrayCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.gang.library.R
import com.gang.library.common.utils.U
/**
 *
 * @ClassName:      haoruigang
 * @Description:     自定义输入框
 * @Author:         haoruigang
 * @CreateDate:     2021/9/4 17:40
 */
class NumberCodeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    /**
     * 输入View
     */
    private lateinit var mEditText: EditText
    private var mLastIndex = 0
    private var mCurIndex = 0
    private var mCodeLength = 0
    private var mCodeTextPaint: Paint? = null
    private val mTextRect: Rect
    var inputCode = ""
        private set
    private var mFrameSize = -1
    private var mFramePadding = -1
    private var mCodeTextColor = -1
    private var mCodeTextSize = -1
    private var mNormalId: Int = R.drawable.rect_c6_f2_solid
    private var mSelectId: Int = R.drawable.rect_c11_ff58_solid
    private var mShowSystemKeyboard = true

    @DrawableRes
    private var mFrameDrawableId = -1
    private val mInputDrawable: SparseArrayCompat<Drawable> = SparseArrayCompat()
    private val mInputMethodManager: InputMethodManager
    private var mOnNumberInputListener: OnNumberInputListener? = null
    private val mTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val isBack = inputCode.length > s.length
            mLastIndex = mCurIndex
            if (!TextUtils.isEmpty(s)) {
                inputCode = s.toString()
                mCurIndex = if (isBack) inputCode.length - 1 else inputCode.length
                mCurIndex = if (mCurIndex == mCodeLength) mCurIndex - 1 else mCurIndex
            } else {
                mCurIndex = 0
                inputCode = ""
            }
            setDrawableState(mLastIndex, STATE_NORMAL)
            if (inputCode.length == mCodeLength) {
                if (mOnNumberInputListener != null) {
                    mOnNumberInputListener!!.onInputFinish()
                }
            } else {
                setDrawableState(mCurIndex, STATE_SELECTED)
                if (mOnNumberInputListener != null) {
                    mOnNumberInputListener!!.onInputIng()
                }
            }
            invalidate()
        }

        override fun afterTextChanged(s: Editable) {}
    }

    private fun initEditText() {
        mEditText = EditText(getContext())
        mEditText.addTextChangedListener(mTextWatcher)
        mEditText.setCursorVisible(false)
        ViewCompat.setBackground(mEditText, ColorDrawable(Color.TRANSPARENT))
        mEditText.setTextColor(Color.TRANSPARENT)
        mEditText.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(mCodeLength)))
        mEditText.setFocusable(true)
        mEditText.requestFocus()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mEditText.setShowSoftInputOnFocus(true)
        }
        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER)
        mEditText.setSingleLine()
        addView(
            mEditText,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    /**
     * 是否显示键盘
     *
     * @param showSystemKeyboard true为显示,false为不显示
     */
    fun setShowKeyboard(showSystemKeyboard: Boolean) {
        if (mShowSystemKeyboard == showSystemKeyboard) return
        mShowSystemKeyboard = showSystemKeyboard
        if (mShowSystemKeyboard) {
            mEditText.setInputType(InputType.TYPE_CLASS_NUMBER)
        } else {
            mEditText.setInputType(InputType.TYPE_NULL)
        }
    }

    val inputView: EditText?
        get() = mEditText

    fun setText(text: CharSequence?) {
        if (mEditText != null) {
            mEditText.setText(text)
        }
    }

    private fun initTextPaint() {
        mCodeTextPaint = TextPaint()
        mCodeTextPaint!!.color = mCodeTextColor
        mCodeTextPaint!!.isAntiAlias = true
        mCodeTextPaint!!.textSize = mCodeTextSize.toFloat()
        mCodeTextPaint!!.isFakeBoldText = true
        mCodeTextPaint!!.textAlign = Paint.Align.CENTER
    }

    private val frameDrawable: Drawable?
        private get() = if (mFrameDrawableId == -1) {
            val drawable = StateListDrawable()
            drawable.addState(
                STATE_NORMAL,
                ContextCompat.getDrawable(getContext(), mNormalId)
            )
            drawable.addState(
                STATE_SELECTED,
                ContextCompat.getDrawable(getContext(), mSelectId)
            )
            drawable
        } else {
            ContextCompat.getDrawable(getContext(), mFrameDrawableId)
        }

    private fun initStateListDrawable() {
        for (i in 0 until mCodeLength) {
            mInputDrawable.put(i, frameDrawable)
        }
        mLastIndex = 0
        mCurIndex = mLastIndex
        setDrawableState(mCurIndex, STATE_SELECTED)
    }

    //    private static boolean isAttrPxType(TypedArray typeArray, int index) {
    //        return typeArray.peekValue(index).type == TypedValue.COMPLEX_UNIT_PX;
    //    }
    fun setOnNumberInputListener(listener: OnNumberInputListener?) {
        mOnNumberInputListener = listener
    }

    /**
     * 设置drawable state
     */
    private fun setDrawableState(index: Int, state: IntArray) {
        if (index < 0 || index > mInputDrawable.size() - 1) return
        mInputDrawable.get(index)?.state = state
    }

    protected override fun onVisibilityChanged(@NonNull changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (getVisibility() != View.VISIBLE) {
            mInputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0)
        }
    }

    protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width: Int = MeasureSpec.getSize(widthMeasureSpec)
        var height: Int = MeasureSpec.getSize(heightMeasureSpec)
        val heightSpecMode: Int = MeasureSpec.getMode(heightMeasureSpec)
        val widthSpecMode: Int = MeasureSpec.getMode(widthMeasureSpec)
        if (heightSpecMode == MeasureSpec.AT_MOST) {
            height = mFrameSize
        }
        if (widthSpecMode != MeasureSpec.EXACTLY) {
            width = mCodeLength * mFrameSize + mFramePadding * (mCodeLength - 1)
        }
        val childWidthSpec: Int = ViewGroup.getChildMeasureSpec(widthMeasureSpec, 0, width)
        val childHeightSpec: Int = ViewGroup.getChildMeasureSpec(heightMeasureSpec, 0, height)
        mEditText.measure(childWidthSpec, childHeightSpec)
        setMeasuredDimension(width, height)
    }

    private fun indexOfCode(index: Int): String {
        if (TextUtils.isEmpty(inputCode)) {
            return ""
        }
        return if (index < 0 || index > inputCode.length - 1) {
            ""
        } else inputCode[index].toString()
    }

    protected override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var left = 0
        var right = mFrameSize
        val size: Int = mInputDrawable.size()
        for (i in 0 until size) {
            val drawable: Drawable = mInputDrawable.get(i)!!
            drawable.setBounds(left, 0, right, getMeasuredHeight())
            drawable.draw(canvas)

            //绘制文本
            drawCodeText(canvas, drawable.bounds, indexOfCode(i))
            left = right + mFramePadding
            right = left + mFrameSize
        }
    }

    private fun drawCodeText(canvas: Canvas, bound: Rect, text: String) {
        if (!TextUtils.isEmpty(text)) {
            mCodeTextPaint!!.getTextBounds(text, 0, text.length, mTextRect)
            canvas.drawText(
                text,
                bound.centerX().toFloat(),
                (bound.height() / 2 + mTextRect.height() / 2).toFloat(),
                mCodeTextPaint!!
            )
        }
    }

    interface OnNumberInputListener {
        fun onInputFinish()
        fun onInputIng()
    }

    companion object {
        private val STATE_NORMAL = intArrayOf(-android.R.attr.state_selected)
        private val STATE_SELECTED = intArrayOf(android.R.attr.state_selected)
        private const val DEFAULT_TEXT_COLOR = -0x1
        private const val DEFAULT_TEXT_SIZE = 30f //dp
        private const val DEFAULT_FRAME_SIZE = 50f
        private const val DEFAULT_FRAME_PADDING = 14f
        private const val DEFAULT_CODE_LENGTH = 4
    }

    init {
        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.NumberCodeView)
        val size: Int = typedArray.getIndexCount()
        if (size > 0) {
            for (i in 0 until size) {
                val attr: Int = typedArray.getIndex(i)
                when (attr) {
                    R.styleable.NumberCodeView_codeTextColor -> mCodeTextColor =
                        typedArray.getColor(attr, -1)
                    R.styleable.NumberCodeView_codeTextSize -> mCodeTextSize =
                        typedArray.getDimensionPixelSize(attr, -1)
                    R.styleable.NumberCodeView_frameSize -> mFrameSize =
                        typedArray.getDimensionPixelSize(attr, -1)
                    R.styleable.NumberCodeView_framePadding -> mFramePadding =
                        typedArray.getDimensionPixelOffset(attr, -1)
                    R.styleable.NumberCodeView_codeLength -> mCodeLength =
                        typedArray.getInt(attr, -1)
                    R.styleable.NumberCodeView_frameDrawableId -> mFrameDrawableId =
                        typedArray.getResourceId(attr, -1)
                    R.styleable.NumberCodeView_normal -> mNormalId =
                        typedArray.getResourceId(attr, R.drawable.rect_c6_f2_solid)
                    R.styleable.NumberCodeView_select -> mSelectId =
                        typedArray.getResourceId(attr, R.drawable.rect_c11_ff58_solid)
                }
            }
        }
        typedArray.recycle()
        if (mCodeTextColor == -1) {
            mCodeTextColor = DEFAULT_TEXT_COLOR
        }
        if (mCodeTextSize == -1) {
            mCodeTextSize = U.dip2px(context, DEFAULT_TEXT_SIZE)
        }
        if (mFrameSize == -1) {
            mFrameSize = U.dip2px(context, DEFAULT_FRAME_SIZE)
        }
        if (mFramePadding == -1) {
            mFramePadding = U.dip2px(context, DEFAULT_FRAME_PADDING)
        }
        if (mCodeLength <= 0) {
            mCodeLength = DEFAULT_CODE_LENGTH
        }
        mTextRect = Rect()
        mInputMethodManager =
            getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        initEditText()
        initTextPaint()
        initStateListDrawable()
        setWillNotDraw(false)
    }
}