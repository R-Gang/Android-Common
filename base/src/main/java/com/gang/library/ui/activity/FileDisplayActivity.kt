package com.gang.library.ui.activity

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.gang.library.R
import com.gang.library.common.utils.showToast
import com.tencent.smtt.sdk.*
import java.io.File
import java.text.DecimalFormat

class FileDisplayActivity : Activity(), TbsReaderView.ReaderCallback {
    private lateinit var tv_title: TextView
    private var mTbsReaderView: TbsReaderView? = null
    private var tv_download: TextView? = null
    private var rl_tbsView //rl_tbsView为装载TbsReaderView的视图
            : RelativeLayout? = null
    private var progressBar_download: ProgressBar? = null
    private var mDownloadManager: DownloadManager? = null
    private var mRequestId: Long = 0
    private var mDownloadObserver: DownloadObserver? = null
    private var mFileUrl: String? = ""
    private var mFileName: String? = null
    private var fileName //文件url 由文件url截取的文件名 上个页面传过来用于显示的文件名
            : String? = null
    private lateinit var mWebView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_file_display)
        findViewById()
        fileUrlByIntent
        mTbsReaderView = TbsReaderView(this, this)
        rl_tbsView?.addView(
            mTbsReaderView, RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        if (mFileUrl == null || mFileUrl!!.length <= 0) {
            Toast.makeText(
                this@FileDisplayActivity, "获取文件url出错了",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        mFileName = parseName(mFileUrl!!)
        mWebView = findViewById(R.id.web)
        val settings = mWebView.getSettings()
        settings.savePassword = false
        settings.javaScriptEnabled = true
        settings.setAllowFileAccessFromFileURLs(true)
        settings.setAllowUniversalAccessFromFileURLs(true)
        settings.setSupportZoom(true)
        //设置是否可缩放，会出现缩放工具（若为true则上面的设值也默认为true）
        settings.builtInZoomControls = true
        //是否隐藏缩放工具
        settings.displayZoomControls = true
        settings.setAppCacheEnabled(false) //禁止缓存
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.loadWithOverviewMode = true
        mWebView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String,
            ): Boolean {
                view.loadUrl(url)
                return true
            }
        })
        mWebView.setWebChromeClient(WebChromeClient())
        if (mFileUrl!!.contains(".mp4")) {
            mWebView.loadUrl(mFileUrl)
            mWebView.setVisibility(View.VISIBLE)
            rl_tbsView?.visibility = View.GONE
        } else if (isLocalExist) {
            tv_download!!.text = "打开文件"
            tv_download!!.visibility = View.GONE
            displayFile()
        } else {
            if (!mFileUrl!!.contains("http")) {
                AlertDialog.Builder(this@FileDisplayActivity)
                    .setTitle("温馨提示:")
                    .setMessage("文件的url地址不合法哟，无法进行下载")
                    .setCancelable(false)
                    .setPositiveButton(
                        "确定"
                    ) { arg0: DialogInterface?, arg1: Int -> }.create().show()
            }
            startDownload()
        }
    }

    /**
     * 将url进行encode，解决部分手机无法下载含有中文url的文件的问题（如OPPO R9）
     *
     * @param url
     * @return
     * @author xch
     */
    private fun toUtf8String(url: String?): String {
        val sb = StringBuffer()
        for (i in 0 until url!!.length) {
            val c = url[i]
            if (c.toInt() >= 0 && c.toInt() <= 255) {
                sb.append(c)
            } else {
                var b: ByteArray
                b = try {
                    c.toString().toByteArray(charset("utf-8"))
                } catch (ex: Exception) {
                    println(ex)
                    ByteArray(0)
                }
                for (j in b.indices) {
                    var k = b[j].toInt()
                    if (k < 0) k += 256
                    sb.append("%" + Integer.toHexString(k).toUpperCase())
                }
            }
        }
        return sb.toString()
    }

    private fun findViewById() {
        tv_download = findViewById(R.id.tv_download)
        val back_icon = findViewById<RelativeLayout>(R.id.rl_back_button)
        tv_title = findViewById(R.id.tv_title)
        progressBar_download = findViewById(R.id.progressBar_download)
        rl_tbsView = findViewById(R.id.rl_tbsView)
        back_icon.setOnClickListener { v: View? -> finish() }
    }

    /**
     * 获取传过来的文件url和文件名
     */
    private val fileUrlByIntent: Unit
        private get() {
            val intent = intent
            mFileUrl = intent.getStringExtra("fileUrl")
            fileName = intent.getStringExtra("fileName")
            tv_title.text = fileName
        }

    /**
     * 加载显示文件内容
     */
    private fun displayFile() {
        val bundle = Bundle()
        bundle.putString("filePath", localFile.path)
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().path)
        val parseFormat = parseFormat(mFileName)
        val result = mTbsReaderView?.preOpen(parseFormat, false) as Boolean
        if (result) {
            mTbsReaderView?.visibility = View.VISIBLE
            mTbsReaderView?.openFile(bundle)
        } else {
            showToast("暂不支持")
        }
    }

    private fun parseFormat(fileName: String?): String {
        return fileName!!.substring(fileName.lastIndexOf(".") + 1)
    }

    /**
     * 利用文件url转换出文件名
     *
     * @param url
     * @return
     */
    private fun parseName(url: String): String? {
        var fileName: String? = null
        try {
            fileName = url.substring(url.lastIndexOf("/") + 1)
        } finally {
            if (TextUtils.isEmpty(fileName)) {
                fileName = System.currentTimeMillis().toString()
            }
        }
        return fileName
    }

    private val isLocalExist: Boolean
        private get() = localFile.exists()

    private val localFile: File
        private get() = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            mFileName
        )

    /**
     * 下载文件
     */
    @SuppressLint("NewApi")
    private fun startDownload() {
        mDownloadObserver = DownloadObserver(Handler())
        contentResolver.registerContentObserver(
            Uri.parse("content://downloads/my_downloads"),
            true,
            mDownloadObserver!!
        )
        mDownloadManager =
            getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        //将含有中文的url进行encode
        val fileUrl = toUtf8String(mFileUrl)
        try {
            val request = DownloadManager.Request(
                Uri.parse(fileUrl)
            )
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS, mFileName
            )
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
            mRequestId = mDownloadManager!!.enqueue(request)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private fun queryDownloadStatus() {
        val query = DownloadManager.Query()
            .setFilterById(mRequestId)
        var cursor: Cursor? = null
        try {
            cursor = mDownloadManager!!.query(query)
            if (cursor != null && cursor.moveToFirst()) { // 已经下载的字节数
                val currentBytes = cursor
                    .getLong(
                        cursor
                            .getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                    )
                // 总需下载的字节数
                val totalBytes = cursor
                    .getLong(
                        cursor
                            .getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                    )
                // 状态所在的列索引
                val status = cursor.getInt(
                    cursor
                        .getColumnIndex(DownloadManager.COLUMN_STATUS)
                )
                tv_download!!.text = ("下载中...(" + formatKMGByBytes(currentBytes)
                        + "/" + formatKMGByBytes(totalBytes) + ")")
                // 将当前下载的字节数转化为进度位置
                val progress = (currentBytes * 1.0 / totalBytes * 100).toInt()
                progressBar_download!!.progress = progress
                Log.i(
                    "downloadUpdate: ", currentBytes.toString() + " " + totalBytes + " "
                            + status + " " + progress
                )
                if (DownloadManager.STATUS_SUCCESSFUL == status
                    && tv_download!!.visibility == View.VISIBLE
                ) {
                    tv_download!!.visibility = View.GONE
                    tv_download!!.performClick()
                    if (isLocalExist) {
                        tv_download!!.visibility = View.GONE
                        displayFile()
                    }
                }
            }
        } finally {
            cursor?.close()
        }
    }

    override fun onCallBackAction(
        integer: Int,
        o: Any,
        o1: Any,
    ) {
    }

    override fun onDestroy() {
        super.onDestroy()
        mTbsReaderView?.onStop()
        if (mDownloadObserver != null) {
            contentResolver.unregisterContentObserver(mDownloadObserver!!)
        }
    }

    @SuppressLint("Override")
    override fun onPointerCaptureChanged(hasCapture: Boolean) {
    }

    private inner class DownloadObserver(handler: Handler) :
        ContentObserver(handler) {
        override fun onChange(
            selfChange: Boolean,
            uri: Uri,
        ) {
            queryDownloadStatus()
        }
    }

    /**
     * 将字节数转换为KB、MB、GB
     *
     * @param size 字节大小
     * @return
     */
    private fun formatKMGByBytes(size: Long): String {
        val bytes = StringBuffer()
        val format = DecimalFormat("###.00")
        if (size >= 1024 * 1024 * 1024) {
            val i = size / (1024.0 * 1024.0 * 1024.0)
            bytes.append(format.format(i)).append("GB")
        } else if (size >= 1024 * 1024) {
            val i = size / (1024.0 * 1024.0)
            bytes.append(format.format(i)).append("MB")
        } else if (size >= 1024) {
            val i = size / 1024.0
            bytes.append(format.format(i)).append("KB")
        } else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B")
            } else {
                bytes.append(size.toInt()).append("B")
            }
        }
        return bytes.toString()
    }

    companion object {
        /**
         * 跳转页面
         *
         * @param context
         * @param fileUrl  文件url
         * @param fileName 文件名
         */
        fun actionStart(
            context: Context,
            fileUrl: String?,
            fileName: String?,
        ) {
            val intent = Intent(context, FileDisplayActivity::class.java)
            intent.putExtra("fileUrl", fileUrl)
            intent.putExtra("fileName", fileName)
            context.startActivity(intent)
        }
    }
}