package com.gang.app.ui.activity

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.*
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.gang.app.R
import com.gang.library.common.utils.ClickableSpans
import com.gang.library.common.utils.setSpannable
import com.gang.library.common.utils.showToast
import kotlinx.android.synthetic.main.activity_spannable.*

/**
 * 文本一系列处理
 */
class SpannableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spannable)

        setSpannable(userAgreement, 2, 18, object : ClickableSpans {
            override fun clickable(widget: View) {
                showToast("点击了用户协议")
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.parseColor("#697CAD")

                // 去掉点击背景
                val spanUser = SpannableStringBuilder(userAgreement.text)
                val span = BackgroundColorSpan(Color.TRANSPARENT)
                spanUser.setSpan(span, 2, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                userAgreement.setText(spanUser)
            }
        })

        addUrlSpan()
        addBackColorSpan()
        addForeColorSpan()
        addFontSpan()
        addStyleSpan()
        addStrikeSpan()
        addUnderLineSpan()
        addImageSpan()
        addConbine()

    }


    /**
     * 超链接
     */
    private fun addUrlSpan() {
        val spanString = SpannableString("超链接")
        val span = URLSpan("tel:0123456789")
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv1.setText(spanString)
    }


    /**
     * 文字背景颜色
     */
    private fun addBackColorSpan() {
        val spanString = SpannableString("背景色")
        val span = BackgroundColorSpan(Color.YELLOW)
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv2.setText(spanString)
    }


    /**
     * 文字颜色
     */
    private fun addForeColorSpan() {
        val spanString = SpannableString("字体色")
        val span = ForegroundColorSpan(Color.BLUE)
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv3.setText(spanString)
    }


    /**
     * 字体大小
     */
    private fun addFontSpan() {
        val spanString = SpannableString("36号字体")
        val span = AbsoluteSizeSpan(36)
        spanString.setSpan(span, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv4.setText(spanString)
    }


    /**
     * 粗体，斜体
     */
    private fun addStyleSpan() {
        val spanString = SpannableString("BIBI")
        val span = StyleSpan(Typeface.BOLD_ITALIC)
        spanString.setSpan(span, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv5.setText(spanString)
    }


    /**
     * 删除线
     */
    private fun addStrikeSpan() {
        val spanString = SpannableString("删除线")
        val span = StrikethroughSpan()
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv6.setText(spanString)
    }


    /**
     * 下划线
     */
    private fun addUnderLineSpan() {
        val spanString = SpannableString("下划线")
        val span = UnderlineSpan()
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv7.setText(spanString)
    }


    /**
     * 图片
     */
    private fun addImageSpan() {
        val spanString = SpannableString(" ")
        val d = ContextCompat.getDrawable(this, R.mipmap.laopo)
        d?.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
        d?.apply {
            val span = ImageSpan(d, ImageSpan.ALIGN_BASELINE)
            spanString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            tv8.setText(spanString)
        }
    }


    /**
     * 混合
     */
    private fun addConbine() {
        val spannable = SpannableStringBuilder("组合运用啊")
        val span1: CharacterStyle = BackgroundColorSpan(Color.BLUE)
        val span2: CharacterStyle = ForegroundColorSpan(Color.RED)
        spannable.setSpan(span1, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(span2, 2, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv9.setText(spannable)
    }
}