package com.gang.library.ui.widget.numbercode

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnKeyListener
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import com.gang.library.R
import com.gang.library.databinding.PhoneCodeBinding
import com.gang.library.databinding.PhoneCodeLineBinding
import com.gang.tools.kotlin.utils.showKeyBoard

/**
 *
 * @ClassName:      PhoneCodeLine
 * @Description:     java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     2021/9/17 20:29
 * @UpdateUser:     更新者：
 * @UpdateDate:     2021/9/17 20:29
 * @UpdateRemark:   更新说明：
 * @Version:
 * 自定义验证码 带下划线
 */
class PhoneCodeLine : RelativeLayout {

    private var mContext: Context

    private val codes: MutableList<String> = ArrayList()

    var color_default = Color.parseColor("#999999")
    var color_focus = Color.parseColor("#3F8EED")

    var mBinding: PhoneCodeLineBinding? = null

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        mContext = context
        mBinding = PhoneCodeLineBinding.inflate(LayoutInflater.from(context), this, true)

        initEvent()

        val types = context.obtainStyledAttributes(attrs, R.styleable.PhoneCodeView)
        // 默认颜色
        color_default =
            types.getColor(R.styleable.PhoneCodeView_color_default, Color.parseColor("#999999"))
        // 选择颜色
        color_focus =
            types.getColor(R.styleable.PhoneCodeView_color_default, Color.parseColor("#3F8EED"))
    }

    fun getView(): View? {
        return mBinding?.root
    }

    private fun initEvent() {
        mBinding?.apply {
            //验证码输入
            etCode.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int,
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {
                    if (editable.isNotEmpty()) {
                        etCode.setText("")
                        if (codes.size < 4) {
                            codes.add(editable.toString())
                            showCode()
                        }
                    }
                }
            })
            // 监听验证码删除按键
            etCode.setOnKeyListener(OnKeyListener { view, keyCode, keyEvent ->
                if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.action == KeyEvent.ACTION_DOWN && codes.size > 0) {
                    codes.removeAt(codes.size - 1)
                    showCode()
                    return@OnKeyListener true
                }
                false
            })
        }
    }

    /**
     * 显示输入的验证码
     */
    private fun showCode() {
        var code1 = ""
        var code2 = ""
        var code3 = ""
        var code4 = ""
        if (codes.size >= 1) {
            code1 = codes[0]
        }
        if (codes.size >= 2) {
            code2 = codes[1]
        }
        if (codes.size >= 3) {
            code3 = codes[2]
        }
        if (codes.size >= 4) {
            code4 = codes[3]
        }
        mBinding?.apply {
            tvCode1.setText(code1, TextView.BufferType.NORMAL)
            tvCode2.setText(code2, TextView.BufferType.NORMAL)
            tvCode3.setText(code3, TextView.BufferType.NORMAL)
            tvCode4.setText(code4, TextView.BufferType.NORMAL)
            setColor() //设置高亮颜色
            callBack() //回调
        }
    }

    /**
     * 设置高亮颜色
     */
    private fun setColor() {
        mBinding?.apply {
            v1.setBackgroundColor(color_default)
            v2.setBackgroundColor(color_default)
            v3.setBackgroundColor(color_default)
            v4.setBackgroundColor(color_default)
            if (codes.size == 0) {
                v1.setBackgroundColor(color_focus)
            }
            if (codes.size == 1) {
                v2.setBackgroundColor(color_focus)
            }
            if (codes.size == 2) {
                v3.setBackgroundColor(color_focus)
            }
            if (codes.size >= 3) {
                v4.setBackgroundColor(color_focus)
            }
        }
    }

    /**
     * 回调
     */
    private fun callBack() {
        if (onInputListener == null) {
            return
        }
        if (codes.size == 4) {
            onInputListener!!.onSucess(phoneCode)
        } else {
            onInputListener!!.onInput()
        }
    }

    //定义回调
    interface OnInputListener {
        fun onSucess(code: String?)
        fun onInput()
    }

    private var onInputListener: OnInputListener? = null
    fun setOnInputListener(onInputListener: OnInputListener?) {
        this.onInputListener = onInputListener
    }

    /**
     * 显示键盘
     */
    fun showSoftInput() {
        mBinding?.apply {
            //显示软键盘
            etCode.postDelayed({ showKeyBoard(etCode) }, 200)
        }
    }

    fun getetCode(): EditText? {
        return mBinding?.etCode
    }

    fun getetCode1(): TextView? {
        return mBinding?.tvCode1
    }

    fun getetCode2(): TextView? {
        return mBinding?.tvCode2
    }

    fun getetCode3(): TextView? {
        return mBinding?.tvCode3
    }

    fun getetCode4(): TextView? {
        return mBinding?.tvCode4
    }

    // 验证码输入错误清空
    fun clearCode() {
        codes.clear()
        showCode()
    }

    /**
     * 获得手机号验证码
     * @return 验证码
     */
    val phoneCode: String
        get() {
            val sb = StringBuilder()
            for (code in codes) {
                sb.append(code)
            }
            return sb.toString()
        }
}
