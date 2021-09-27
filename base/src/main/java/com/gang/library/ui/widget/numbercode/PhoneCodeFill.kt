package com.gang.library.ui.widget.numbercode

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnKeyListener
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.TextView
import com.gang.library.R
import com.gang.library.common.utils.flash
import com.gang.library.common.utils.gone
import com.gang.library.common.utils.show
import kotlinx.android.synthetic.main.phone_code.view.*
import java.util.*

/**
 *
 * @ClassName:      PhoneCodeFill
 * @Description:     java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     2021/9/17 20:29
 * @UpdateUser:     更新者：
 * @UpdateDate:     2021/9/17 20:29
 * @UpdateRemark:   更新说明：
 * @Version:
 * 自定义验证码 填充框
 */

class PhoneCodeFill : RelativeLayout {

    private val codes = arrayListOf<String>()
    private var imm: InputMethodManager? = null

    var color_default = R.drawable.rect_c6_f2_solid
    var color_focus = R.drawable.rect_c11_ff58_solid

    constructor(context: Context) : this(context, null) {
        loadView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        loadView()

        val types = context.obtainStyledAttributes(attrs, R.styleable.PhoneCodeView)
        // 默认颜色
        color_default =
            types.getColor(R.styleable.PhoneCodeView_color_default, R.drawable.rect_c6_f2_solid)
        // 选择颜色
        color_focus =
            types.getColor(R.styleable.PhoneCodeView_color_default, R.drawable.rect_c11_ff58_solid)
    }

    private fun loadView() {
        imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view: View = LayoutInflater.from(context).inflate(R.layout.phone_code, this)
        initEvent()
    }

    private fun initEvent() {
        //验证码输入
        et_code?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.isNotEmpty()) {
                    et_code.setText("")
                    if (codes.size < 4) {
                        codes.add(editable.toString())
                        showCode()
                    }
                }
            }
        })
        // 监听验证码删除按键
        et_code!!.setOnKeyListener(OnKeyListener { view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.action == KeyEvent.ACTION_DOWN && codes.size > 0) {
                codes.removeAt(codes.size - 1)
                showCode()
                return@OnKeyListener true
            }
            false
        })
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

        v_line1.show()
        flash(v_line1)

        tv_code1?.setText(code1, TextView.BufferType.NORMAL)
        tv_code2?.setText(code2, TextView.BufferType.NORMAL)
        tv_code3?.setText(code3, TextView.BufferType.NORMAL)
        tv_code4?.setText(code4, TextView.BufferType.NORMAL)
        setColor() //设置高亮颜色
        callBack() //回调
    }

    /**
     * 设置高亮颜色
     */
    private fun setColor() {

        v1.setBackgroundResource(color_default)
        v2.setBackgroundResource(color_default)
        v3.setBackgroundResource(color_default)
        v4.setBackgroundResource(color_default)

        v_line1.gone()
        v_line2.gone()
        v_line3.gone()
        v_line4.gone()

        if (codes.size == 0) {
            v1.setBackgroundResource(color_focus)

            v_line1.show()
            flash(v_line1)
        }
        if (codes.size == 1) {
            v2.setBackgroundResource(color_focus)

            v_line2.show()
            flash(v_line2)

        }
        if (codes.size == 2) {
            v3.setBackgroundResource(color_focus)

            v_line3.show()
            flash(v_line3)

        }
        if (codes.size >= 3) {
            v4.setBackgroundResource(color_focus)

            v_line4.show()
            flash(v_line4)

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
            onInputListener?.onSucess(phoneCode)
        } else {
            onInputListener?.onInput()
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
        //显示软键盘
        if (imm != null && et_code != null) {
            et_code.postDelayed({ imm?.showSoftInput(et_code, 0) }, 200)
        }
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
