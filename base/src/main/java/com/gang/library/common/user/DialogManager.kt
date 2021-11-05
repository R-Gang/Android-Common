package com.gang.library.common.user

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.WindowManager
import com.gang.library.R

enum class DialogManager {

    DM;

    /**
     * dialog
     */
    fun init(
        layout: Int,
        context: Context,
        themeResId: Int = R.style.DialogNoTheme, // 自己定义样式：不显示标题背景
    ): AlertDialog? {
        val mBuilder = AlertDialog.Builder(context, themeResId)
        mBuilder.setView(layout)
        val mDialog = mBuilder.create()
        mDialog.show()
        mDialog.dismiss()
        return mDialog
    }

    /**
     * dialog
     */
    fun initWindow(
        layout: Int,
        context: Context,
        themeResId: Int = 0, // 自己定义样式：设0默认样式
    ): AlertDialog {
        val mBuilder = AlertDialog.Builder(context, themeResId)
        val mDialog = mBuilder.create()
        mDialog.show()
        mDialog.setContentView(layout)
        mDialog.window!!.setBackgroundDrawable(BitmapDrawable())
        //处理无法弹出输入法
        mDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        //全屏处理
        mDialog.window!!.decorView.setPadding(0, 0, 0, 0)
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        mDialog.window!!.decorView.systemUiVisibility = uiOptions
        val lp = mDialog.window!!.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        mDialog.window!!.attributes = lp

        mDialog.dismiss()
        return mDialog
    }
}