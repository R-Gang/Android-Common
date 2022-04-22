package com.gang.library.common.utils.version

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.graphics.Typeface
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.gang.library.R
import com.gang.library.common.utils.typefaceAll
import com.vector.update_app.UpdateAppBean
import com.vector.update_app.UpdateAppManager
import com.vector.update_app.utils.AppUpdateUtils
import java.io.File

/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.library.common.utils.version
 * @ClassName:      UpdateHandle
 * @Description:    新版本版本检测回调
 * @Author:         haoruigang
 * @CreateDate:     2021/11/5 11:43
 * @UpdateUser:     更新者：
 * @UpdateDate:     2021/11/5 11:43
 * @UpdateRemark:   更新说明：
 * @Version:
 */
class UpdateHandle(updateCallback: UpdateCallback) :
    com.vector.update_app.UpdateCallback() {

    private val updateCallback = updateCallback

    /**
     * 解析json,自定义协议
     *
     * @param json 服务器返回的json
     * @return UpdateAppBean
     */
    override fun parseJson(json: String): UpdateAppBean {
        val updateAppBean = UpdateBean()
        return updateCallback.parseJson(json, updateAppBean)
    }

    /**
     * 有新版本
     *
     * @param updateApp        新版本信息
     * @param updateAppManager app更新管理器
     */
    override fun hasNewApp(updateApp: UpdateAppBean, updateAppManager: UpdateAppManager) {
        updateCallback.hasNewApp(updateApp, updateAppManager)
    }


    companion object {
        /**
         * 跳转到更新页面
         *
         * @param updateApp
         * @param updateAppManager
         */
        fun showDialogFragment(
            mActivity: Activity,
            updateApp: UpdateAppBean,
            updateAppManager: UpdateAppManager,
        ) : AlertDialog?{
            val targetSize = updateApp.targetSize
            val updateLog = updateApp.updateLog
            var msg: String? = ""
            if (!TextUtils.isEmpty(targetSize)) {
                msg = "新版本大小：$targetSize\n\n"
            }
            if (!TextUtils.isEmpty(updateLog)) {
                msg += updateLog
            }
            val dialog = AlertDialog.Builder(mActivity, R.style.DialogNoTheme).create()
            val dialogView = View.inflate(mActivity, R.layout.update_app_dialog, null)
            dialog.setView(dialogView)
            dialog.setCancelable(false)
            dialog.show()
            val face: Typeface = typefaceAll
            val tvTitle = dialogView.findViewById<TextView>(R.id.tv_title)
            tvTitle.setTypeface(face)
            val tvSubTitle = dialogView.findViewById<TextView>(R.id.tv_sub_title)
            tvSubTitle.setTypeface(face)
            tvSubTitle.text = String.format("版本号%s", updateApp.newVersion)
            val tvUpdateInfo = dialogView.findViewById<TextView>(R.id.tv_update_info)
            tvUpdateInfo.setTypeface(face)
            tvUpdateInfo.text = msg
            val btnClose = dialogView.findViewById<Button>(R.id.btn_close)
            val btnOk = dialogView.findViewById<Button>(R.id.btn_ok)
            btnOk.setOnClickListener { v: View? ->
                //动态权限申请
                ActivityCompat.requestPermissions(mActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    9090)
            }
            btnClose.setOnClickListener { v: View? -> dialog.dismiss() }
            val llCancel = dialogView.findViewById<LinearLayout>(R.id.ll_cancel)
            llCancel.visibility = if (updateApp.isConstraint) View.GONE else View.VISIBLE
            val btnUpOk = dialogView.findViewById<Button>(R.id.btn_up_ok)
            btnUpOk.visibility = if (updateApp.isConstraint) View.VISIBLE else View.GONE
            btnUpOk.setOnClickListener { v: View? ->
                //不显示下载进度
//                updateAppManager.download()

                //显示下载进度
                updateAppManager.download(object : DownloadCallback() {

                    override fun onInstallAppAndAppOnForeground(file: File?): Boolean {
                        return run {
                            AppUpdateUtils.installApp(mActivity, file)
                            true
                        }
                    }
                })

                dialog.dismiss()
            }
            return dialog
        }
    }

}