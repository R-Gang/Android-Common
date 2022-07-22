package com.gang.library.ui.widget.numbercode

import android.content.Context
import android.graphics.drawable.Drawable
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
import androidx.core.content.ContextCompat
import com.gang.library.R
import com.gang.library.common.utils.flash
import com.gang.library.common.utils.gone
import com.gang.library.common.utils.show
import com.gang.library.common.utils.showKeyBoard
import kotlinx.android.synthetic.main.phone_code.view.*

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

    private var vLine1: View? = null
    private var vLine2: View? = null
    private var vLine3: View? = null
    private var vLine4: View? = null

    private var etCode: EditText? = null

    private var tvCode1: TextView? = null
    private var tvCode2: TextView? = null
    private var tvCode3: TextView? = null
    private var tvCode4: TextView? = null

    private val codes = arrayListOf<String>()

    var color_default = R.drawable.rect_c6_f2_solid
    var color_focus = R.drawable.rect_c11_ff58_solid
    var code4Color = R.color.color_f
    var vLine4Bg: Drawable?
    var flashDuration = 500

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        loadView()
        vLine4Bg = ContextCompat.getDrawable(context, R.drawable.edit_cursor_color3)
        val types = context.obtainStyledAttributes(attrs, R.styleable.PhoneCodeView)
        try {
            // 默认颜色
            color_default =
                types.getColor(R.styleable.PhoneCodeView_color_default, R.drawable.rect_c6_f2_solid)
            // 选择颜色
            color_focus =
                types.getColor(R.styleable.PhoneCodeView_color_default,
                    R.drawable.rect_c11_ff58_solid)
            // 第四个颜色
            code4Color =
                types.getColor(R.styleable.PhoneCodeView_code4_color,
                    context.getColor(R.color.color_f))
            // 第四个光标背景
            vLine4Bg = types.getDrawable(R.styleable.PhoneCodeView_vline4_bg)
            // 光标闪烁频率
            flashDuration =
                types.getDimensionPixelOffset(R.styleable.PhoneCodeView_flash_duration, 500)
        } finally {
            types.recycle() //TypeArray用完需要recycle
        }
    }

    private fun loadView() {
        val view = getView()
        vLine1 = view.findViewById<View>(R.id.v_line1)
        vLine2 = view.findViewById<View>(R.id.v_line2)
        vLine3 = view.findViewById<View>(R.id.v_line3)
        vLine4 = view.findViewById<View>(R.id.v_line4)

        etCode = view.findViewById<EditText>(R.id.et_code)
        tvCode1 = view.findViewById<TextView>(R.id.tv_code1)
        tvCode2 = view.findViewById<TextView>(R.id.tv_code2)
        tvCode3 = view.findViewById<TextView>(R.id.tv_code3)
        tvCode4 = view.findViewById<TextView>(R.id.tv_code4)
        initEvent()
    }

    fun getView(): View {
        return LayoutInflater.from(context).inflate(R.layout.phone_code, this)
    }

    private fun initEvent() {
        //验证码输入
        etCode?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.isNotEmpty()) {
                    etCode?.setText("")
                    if (codes.size < 4) {
                        codes.add(editable.toString())
                        showCode()
                    }
                }
            }
        })
        // 监听验证码删除按键
        etCode?.setOnKeyListener(OnKeyListener { view, keyCode, keyEvent ->
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

        tvCode1?.setText(code1, TextView.BufferType.NORMAL)
        tvCode2?.setText(code2, TextView.BufferType.NORMAL)
        tvCode3?.setText(code3, TextView.BufferType.NORMAL)
        tvCode4?.setText(code4, TextView.BufferType.NORMAL)
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

        vLine1?.gone()
        vLine2?.gone()
        vLine3?.gone()
        vLine4?.gone()

        if (codes.size == 0) {
            v1.setBackgroundResource(color_focus)

            vLine1?.show()
            vLine1?.let { flash(it, flashDuration.toLong()) }
        }
        if (codes.size == 1) {
            v2.setBackgroundResource(color_focus)

            vLine2?.show()
            vLine2?.let { flash(it, flashDuration.toLong()) }

        }
        if (codes.size == 2) {
            v3.setBackgroundResource(color_focus)

            vLine3?.show()
            vLine3?.let { flash(it, flashDuration.toLong()) }

        }
        if (codes.size >= 3) {
            v4.setBackgroundResource(color_focus)
            tv_code4.setTextColor(code4Color)
            vLine4?.background = vLine4Bg

            if (tv_code4.text.isNotEmpty()) {
                vLine4?.gone()
            } else {
                vLine4?.show()
                vLine4?.let { flash(it, flashDuration.toLong()) }
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
    fun showSoftInputFlash() {
        //显示软键盘
        if (et_code != null) {
            et_code.postDelayed({ showKeyBoard(et_code) }, 200)
        }
        defaultFlash()
    }

    /**
     * 默认动画
     */
    fun defaultFlash() {
        // 默认第一个显示闪烁
        vLine1?.show()
        vLine1?.let { flash(it, flashDuration.toLong()) }
    }

    fun getetCode(): EditText? {
        return etCode
    }

    fun getetCode1(): TextView? {
        return tvCode1
    }

    fun getetCode2(): TextView? {
        return tvCode2
    }

    fun getetCode3(): TextView? {
        return tvCode3
    }

    fun getetCode4(): TextView? {
        return tvCode4
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
