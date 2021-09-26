package com.gang.library.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import com.gang.library.R
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.base_title_bar.*

/**
 * Created by haoruigang on 2019-3-8 18:19:23.
 * 关于
 */
open class WebViewActivity : BaseActivity() {


    lateinit var tvTitle: TextView
    lateinit var wbXy: WebView

    private var mUrl: String? = ""
    private var mTitleName: String? = ""

    override val layoutId: Int
        get() = R.layout.activity_webview

    override fun initView(savedInstanceState: Bundle?) {
        tvTitle = findViewById(R.id.tv_title)
        wbXy = findViewById(R.id.wb_xy)
        fileUrlByIntent
        mUrl?.let { setUrl(it) }
    }

    override fun initData() {
        dark()
    }

    private fun setUrl(url: String) { //声明WebSettings子类
        val webSettings = wbXy.settings
        webSettings.javaScriptEnabled = true
        //设置自适应屏幕，两者合用
        webSettings.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        webSettings.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.displayZoomControls = false //隐藏原生的缩放控件
        //其他细节操作
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK //关闭webview中缓存
        webSettings.allowFileAccess = true //设置可以访问文件
        webSettings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        webSettings.loadsImagesAutomatically = true //支持自动加载图片
        webSettings.defaultTextEncodingName = "utf-8" //设置编码格式
        //步骤3. 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        wbXy.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String,
            ): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        wbXy.loadUrl(url)
    }

    /**
     * 获取传过来的网络url和标题
     */
    private val fileUrlByIntent: Unit
        private get() {
            val intent = intent
            mUrl = intent.getStringExtra("Url")
            mTitleName = intent.getStringExtra("titleName")
            tvTitle.text = mTitleName
        }

    companion object {
        /**
         * 跳转页面
         *
         * @param context
         * @param Url  网络url
         * @param titleName 标题
         */
        fun actionStart(
            context: Context,
            mUrl: String?,
            mTitleName: String?,
        ) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("Url", mUrl)
            intent.putExtra("titleName", mTitleName)
            context.startActivity(intent)
        }
    }
}