package com.gang.library.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gang.library.R;
import com.gang.library.common.utils.U;
import com.tencent.smtt.sdk.TbsReaderView;
import com.tencent.smtt.sdk.TbsReaderView.ReaderCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.File;
import java.text.DecimalFormat;

public class FileDisplayActivity extends Activity implements ReaderCallback {

    private TextView tv_title;
    private TbsReaderView mTbsReaderView;
    private TextView tv_download;
    private RelativeLayout rl_tbsView;    //rl_tbsView为装载TbsReaderView的视图
    private ProgressBar progressBar_download;
    private DownloadManager mDownloadManager;
    private long mRequestId;
    private DownloadObserver mDownloadObserver;
    private String mFileUrl = "", mFileName, fileName;//文件url 由文件url截取的文件名 上个页面传过来用于显示的文件名

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_file_display);
        findViewById();
        getFileUrlByIntent();
        mTbsReaderView = new TbsReaderView(this, this);
        rl_tbsView.addView(mTbsReaderView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        if ((mFileUrl == null) || (mFileUrl.length() <= 0)) {
            Toast.makeText(FileDisplayActivity.this, "获取文件url出错了",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mFileName = parseName(mFileUrl);

        mWebView = findViewById(R.id.web);
        WebSettings settings = mWebView.getSettings();
        settings.setSavePassword(false);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setSupportZoom(true);
        //设置是否可缩放，会出现缩放工具（若为true则上面的设值也默认为true）
        settings.setBuiltInZoomControls(true);
        //是否隐藏缩放工具
        settings.setDisplayZoomControls(true);
        settings.setAppCacheEnabled(false);//禁止缓存
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient());
        if (mFileUrl.contains(".mp4")) {
            mWebView.loadUrl(mFileUrl);
            mWebView.setVisibility(View.VISIBLE);
            rl_tbsView.setVisibility(View.GONE);
        } else if (isLocalExist()) {
            tv_download.setText("打开文件");
            tv_download.setVisibility(View.GONE);
            displayFile();
        } else {
            if (!mFileUrl.contains("http")) {
                new AlertDialog.Builder(FileDisplayActivity.this)
                        .setTitle("温馨提示:")
                        .setMessage("文件的url地址不合法哟，无法进行下载")
                        .setCancelable(false)
                        .setPositiveButton("确定", (arg0, arg1) -> {
                            return;
                        }).create().show();
            }
            startDownload();
        }
    }

    /**
     * 将url进行encode，解决部分手机无法下载含有中文url的文件的问题（如OPPO R9）
     *
     * @param url
     * @return
     * @author xch
     */
    private String toUtf8String(String url) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < url.length(); i++) {
            char c = url.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = String.valueOf(c).getBytes("utf-8");
                } catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0)
                        k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }

    private void findViewById() {
        tv_download = findViewById(R.id.tv_download);
        RelativeLayout back_icon = findViewById(R.id.rl_back_button);
        tv_title = findViewById(R.id.tv_title);
        progressBar_download = findViewById(R.id.progressBar_download);
        rl_tbsView = findViewById(R.id.rl_tbsView);
        back_icon.setOnClickListener(v -> finish());
    }

    /**
     * 获取传过来的文件url和文件名
     */
    private void getFileUrlByIntent() {
        Intent intent = getIntent();
        mFileUrl = intent.getStringExtra("fileUrl");
        fileName = intent.getStringExtra("fileName");
        tv_title.setText(fileName);
    }

    /**
     * 跳转页面
     *
     * @param context
     * @param fileUrl  文件url
     * @param fileName 文件名
     */
    public static void actionStart(Context context, String fileUrl, String fileName) {
        Intent intent = new Intent(context, FileDisplayActivity.class);
        intent.putExtra("fileUrl", fileUrl);
        intent.putExtra("fileName", fileName);
        context.startActivity(intent);
    }

    /**
     * 加载显示文件内容
     */
    private void displayFile() {
        Bundle bundle = new Bundle();
        bundle.putString("filePath", getLocalFile().getPath());
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
        String parseFormat = parseFormat(mFileName);
        boolean result = mTbsReaderView.preOpen(parseFormat, false);
        if (result) {
            mTbsReaderView.setVisibility(View.VISIBLE);
            mTbsReaderView.openFile(bundle);
        } else {
            U.INSTANCE.showToast("暂不支持");
        }
    }


    private String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 利用文件url转换出文件名
     *
     * @param url
     * @return
     */
    private String parseName(String url) {
        String fileName = null;
        try {
            fileName = url.substring(url.lastIndexOf("/") + 1);
        } finally {
            if (TextUtils.isEmpty(fileName)) {
                fileName = String.valueOf(System.currentTimeMillis());
            }
        }
        return fileName;
    }

    private boolean isLocalExist() {
        return getLocalFile().exists();
    }

    private File getLocalFile() {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mFileName);
    }

    /**
     * 下载文件
     */
    @SuppressLint("NewApi")
    private void startDownload() {
        mDownloadObserver = new DownloadObserver(new Handler());
        getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true, mDownloadObserver);

        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        //将含有中文的url进行encode
        String fileUrl = toUtf8String(mFileUrl);
        try {

            DownloadManager.Request request = new DownloadManager.Request(
                    Uri.parse(fileUrl));
            request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS, mFileName);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            mRequestId = mDownloadManager.enqueue(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void queryDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query()
                .setFilterById(mRequestId);
        Cursor cursor = null;
        try {
            cursor = mDownloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                // 已经下载的字节数
                long currentBytes = cursor
                        .getLong(cursor
                                .getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                // 总需下载的字节数
                long totalBytes = cursor
                        .getLong(cursor
                                .getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                // 状态所在的列索引
                int status = cursor.getInt(cursor
                        .getColumnIndex(DownloadManager.COLUMN_STATUS));
                tv_download.setText("下载中...(" + formatKMGByBytes(currentBytes)
                        + "/" + formatKMGByBytes(totalBytes) + ")");
                // 将当前下载的字节数转化为进度位置
                int progress = (int) ((currentBytes * 1.0) / totalBytes * 100);
                progressBar_download.setProgress(progress);

                Log.i("downloadUpdate: ", currentBytes + " " + totalBytes + " "
                        + status + " " + progress);
                if (DownloadManager.STATUS_SUCCESSFUL == status
                        && tv_download.getVisibility() == View.VISIBLE) {
                    tv_download.setVisibility(View.GONE);
                    tv_download.performClick();
                    if (isLocalExist()) {
                        tv_download.setVisibility(View.GONE);
                        displayFile();
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTbsReaderView.onStop();
        if (mDownloadObserver != null) {
            getContentResolver().unregisterContentObserver(mDownloadObserver);
        }
    }

    @SuppressLint("Override")
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private class DownloadObserver extends ContentObserver {

        private DownloadObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            queryDownloadStatus();
        }
    }


    /**
     * 将字节数转换为KB、MB、GB
     *
     * @param size 字节大小
     * @return
     */
    private String formatKMGByBytes(long size) {
        StringBuffer bytes = new StringBuffer();
        DecimalFormat format = new DecimalFormat("###.00");
        if (size >= 1024 * 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB");
        } else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB");
        } else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB");
        } else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            } else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }
}
