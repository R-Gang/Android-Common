package com.gang.library.common.utils.version

import com.vector.update_app.UpdateAppBean
import com.vector.update_app.UpdateAppManager

/**
 * 新版本版本检测回调
 */
interface UpdateCallback {

    /**
     * 解析json,自定义协议
     *
     * @param json 服务器返回的json
     * @return UpdateAppBean
     */
    fun parseJson(json: String, updateAppBean: UpdateBean): UpdateAppBean

    /**
     * 有新版本
     *
     * @param updateApp        新版本信息
     * @param updateAppManager app更新管理器
     */
    fun hasNewApp(updateApp: UpdateAppBean, updateAppManager: UpdateAppManager)

}