package com.gang.app.ui.activity

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.*
import android.util.Range
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.gang.app.R
import com.gang.tools.kotlin.utils.ClickableSpans
import com.gang.tools.kotlin.utils.setSpannable
import com.gang.tools.kotlin.utils.showToast
import org.w3c.dom.Text

/**
 * 文本一系列处理
 */
class SpannableActivity : AppCompatActivity() {

    private var userAgreement: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spannable)

        userAgreement = findViewById<TextView>(R.id.userAgreement)
        userAgreement?.highlightColor = Color.TRANSPARENT; //设置点击后的颜色为透明，否则会一直出现高亮
        setSpannable(textView = userAgreement!!,
            range = arrayOf(Range<Int>(2, 18)),
            click = object : ClickableSpans {
                override fun clickable(view: View, key: String) {
                    showToast("点击了用户协议")
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = Color.parseColor("#697CAD")
                }
            })
        /*// 去掉点击背景
        val spanUser = SpannableStringBuilder(userAgreement.text)
        val span = BackgroundColorSpan(Color.TRANSPARENT)
        spanUser.setSpan(span, 2, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        userAgreement.text = spanUser*/

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
        findViewById<TextView>(R.id.tv1).text = spanString
    }


    /**
     * 文字背景颜色
     */
    private fun addBackColorSpan() {
        val spanString = SpannableString("背景色")
        val span = BackgroundColorSpan(Color.YELLOW)
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        findViewById<TextView>(R.id.tv2).text = spanString
    }


    /**
     * 文字颜色
     */
    private fun addForeColorSpan() {
        val spanString = SpannableString("字体色")
        val span = ForegroundColorSpan(Color.BLUE)
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        findViewById<TextView>(R.id.tv3).text = spanString
    }


    /**
     * 字体大小
     */
    private fun addFontSpan() {
        val spanString = SpannableString("36号字体")
        val span = AbsoluteSizeSpan(36)
        spanString.setSpan(span, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        findViewById<TextView>(R.id.tv4).text = spanString
    }


    /**
     * 粗体，斜体
     */
    private fun addStyleSpan() {
        val spanString = SpannableString("BIBI")
        val span = StyleSpan(Typeface.BOLD_ITALIC)
        spanString.setSpan(span, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        findViewById<TextView>(R.id.tv5).text = spanString
    }


    /**
     * 删除线
     */
    private fun addStrikeSpan() {
        val spanString = SpannableString("删除线")
        val span = StrikethroughSpan()
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        findViewById<TextView>(R.id.tv6).text = spanString
    }


    /**
     * 下划线
     */
    private fun addUnderLineSpan() {
        val spanString = SpannableString("下划线")
        val span = UnderlineSpan()
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        findViewById<TextView>(R.id.tv7).text = spanString
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
            findViewById<TextView>(R.id.tv8).text = spanString
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
        findViewById<TextView>(R.id.tv9).text = spannable
    }
}