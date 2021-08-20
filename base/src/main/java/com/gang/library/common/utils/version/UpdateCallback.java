package com.gang.library.common.utils.version;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.utils.AppUpdateUtils;

import org.json.JSONObject;

/**
 * 新版本版本检测回调
 */
public class UpdateCallback extends com.vector.update_app.UpdateCallback {

    private final Activity mActivity;

    public UpdateCallback(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 解析json,自定义协议
     *
     * @param json 服务器返回的json
     * @return UpdateAppBean
     */
    protected UpdateAppBean parseJson(String json) {
        UpdateBean updateAppBean = new UpdateBean();
        try {
            JSONObject jsonData = new JSONObject(json);
            JSONObject jsonObject = jsonData.getJSONObject("data");
            updateAppBean.setNewVersionCode(jsonObject.optString("appVersionCode"));
            updateAppBean.setUpdate(jsonObject.optString("ifShow").equals("1") ? "Yes" : "No")
                    .setNewVersion(jsonObject.optString("appVersion"))
                    .setApkFileUrl(jsonObject.optString("apkFileUrl"))
                    .setUpdateLog(jsonObject.optString("description"))
                    .setConstraint(jsonObject.optBoolean("constraint") && jsonObject.optString("ifUp").equals("1"));

//            updateAppBean.setNewVersionCode(jsonObject.optString("new_version_code"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateAppBean;
    }

    /**
     * 有新版本
     *
     * @param updateApp        新版本信息
     * @param updateAppManager app更新管理器
     */
    @Override
    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
        int clientVersionCode = AppUpdateUtils.getVersionCode(mActivity);
        int serverVersionCode = Integer.parseInt(((UpdateBean) updateApp).getNewVersionCode());
        String clientVersionName = AppUpdateUtils.getVersionName(mActivity);
        String serverVersionName = updateApp.getNewVersion();
        Log.d("UpdateCallback11111", clientVersionCode + ":" + serverVersionCode + ":" + clientVersionName + ":" + serverVersionName);
        //有新版本
        if (!TextUtils.isEmpty(clientVersionName) && !TextUtils.isEmpty(serverVersionName)
                && clientVersionCode < serverVersionCode && !clientVersionName.equals(serverVersionName)) {
            Log.d("UpdateCallback*2****", clientVersionCode + ":" + serverVersionCode + ":" + clientVersionName + ":" + serverVersionName);
            updateAppManager.showDialogFragment();
        }
    }

}
