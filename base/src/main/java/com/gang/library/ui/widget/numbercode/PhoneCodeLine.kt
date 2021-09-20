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
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import com.gang.library.R
import java.util.*

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

    private var tv_code1: TextView? = null
    private var tv_code2: TextView? = null
    private var tv_code3: TextView? = null
    private var tv_code4: TextView? = null
    private var v1: View? = null
    private var v2: View? = null
    private var v3: View? = null
    private var v4: View? = null
    private var et_code: EditText? = null
    private val codes: MutableList<String> = ArrayList()
    private var imm: InputMethodManager? = null

    constructor(context: Context) : super(context) {
        loadView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        loadView()
    }

    private fun loadView() {
        imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view: View = LayoutInflater.from(context).inflate(R.layout.phone_code_line, this)
        initView(view)
        initEvent()
    }

    private fun initView(view: View) {
        tv_code1 = view.findViewById<View>(R.id.tv_code1) as TextView
        tv_code2 = view.findViewById<View>(R.id.tv_code2) as TextView
        tv_code3 = view.findViewById<View>(R.id.tv_code3) as TextView
        tv_code4 = view.findViewById<View>(R.id.tv_code4) as TextView
        et_code = view.findViewById<View>(R.id.et_code) as EditText
        v1 = view.findViewById(R.id.v1)
        v2 = view.findViewById(R.id.v2)
        v3 = view.findViewById(R.id.v3)
        v4 = view.findViewById(R.id.v4)
    }

    private fun initEvent() {
        //验证码输入
        et_code!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable != null && editable.length > 0) {
                    et_code!!.setText("")
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
        tv_code1!!.text = code1
        tv_code2!!.text = code2
        tv_code3!!.text = code3
        tv_code4!!.text = code4
        setColor() //设置高亮颜色
        callBack() //回调
    }

    /**
     * 设置高亮颜色
     */
    private fun setColor() {
        val color_default = Color.parseColor("#999999")
        val color_focus = Color.parseColor("#3F8EED")
        v1!!.setBackgroundColor(color_default)
        v2!!.setBackgroundColor(color_default)
        v3!!.setBackgroundColor(color_default)
        v4!!.setBackgroundColor(color_default)
        if (codes.size == 0) {
            v1!!.setBackgroundColor(color_focus)
        }
        if (codes.size == 1) {
            v2!!.setBackgroundColor(color_focus)
        }
        if (codes.size == 2) {
            v3!!.setBackgroundColor(color_focus)
        }
        if (codes.size >= 3) {
            v4!!.setBackgroundColor(color_focus)
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
        //显示软键盘
        if (imm != null && et_code != null) {
            et_code!!.postDelayed({ imm!!.showSoftInput(et_code, 0) }, 200)
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
