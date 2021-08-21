package com.gang.library.common.utils.version

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import com.vector.update_app.UpdateAppBean
import com.vector.update_app.UpdateAppManager
import com.vector.update_app.UpdateCallback
import com.vector.update_app.utils.AppUpdateUtils
import org.json.JSONObject

/**
 * 新版本版本检测回调
 */
class UpdateCallback(private val mActivity: Activity) : UpdateCallback() {
    /**
     * 解析json,自定义协议
     *
     * @param json 服务器返回的json
     * @return UpdateAppBean
     */
    override fun parseJson(json: String): UpdateAppBean {
        val updateAppBean = UpdateBean()
        try {
            val jsonData = JSONObject(json)
            val jsonObject = jsonData.getJSONObject("data")
            updateAppBean.newVersionCode = jsonObject.optString("appVersionCode")
            updateAppBean.setUpdate(if (jsonObject.optString("ifShow") == "1") "Yes" else "No")
                .setNewVersion(jsonObject.optString("appVersion"))
                .setApkFileUrl(jsonObject.optString("apkFileUrl"))
                .setUpdateLog(jsonObject.optString("description")).isConstraint =
                jsonObject.optBoolean("constraint") && jsonObject.optString("ifUp") == "1"

//            updateAppBean.setNewVersionCode(jsonObject.optString("new_version_code"));
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return updateAppBean
    }

    /**
     * 有新版本
     *
     * @param updateApp        新版本信息
     * @param updateAppManager app更新管理器
     */
    override fun hasNewApp(updateApp: UpdateAppBean, updateAppManager: UpdateAppManager) {
        val clientVersionCode = AppUpdateUtils.getVersionCode(mActivity)
        val serverVersionCode = (updateApp as UpdateBean).newVersionCode!!.toInt()
        val clientVersionName = AppUpdateUtils.getVersionName(mActivity)
        val serverVersionName = updateApp.getNewVersion()
        Log.d(
            "UpdateCallback11111",
            "$clientVersionCode:$serverVersionCode:$clientVersionName:$serverVersionName"
        )
        //有新版本
        if (!TextUtils.isEmpty(clientVersionName) && !TextUtils.isEmpty(serverVersionName)
            && clientVersionCode < serverVersionCode && clientVersionName != serverVersionName
        ) {
            Log.d(
                "UpdateCallback*2****",
                "$clientVersionCode:$serverVersionCode:$clientVersionName:$serverVersionName"
            )
            updateAppManager.showDialogFragment()
        }
    }
}