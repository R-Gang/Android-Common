package com.gang.app.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.text.TextPaint
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gang.app.R
import com.gang.library.common.utils.ClickableSpans
import com.gang.library.common.utils.setSpannable
import com.gang.library.common.utils.showToast
import kotlinx.android.synthetic.main.activity_picker.*

/**
 * 1.地址选择器底部弹框
 * 2.页面选择器
 */
class PickerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker)
        showPickerView.setOnClickListener { showPickerView() }
    }

    private fun showPickerView() { // 城市弹出选择器
        setSpannable(userAgreement, 2, 18, object : ClickableSpans {
            override fun clickable(widget: View) {
                showToast("点击了用户协议")
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.parseColor("#697CAD")
            }
        })
    }

    companion object {

        private var pname: String? = null
        private var cname: String? = null
        private var dname: String? = null
    }

}