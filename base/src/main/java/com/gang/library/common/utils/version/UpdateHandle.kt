package com.gang.library.common.utils.version

import com.vector.update_app.UpdateAppBean
import com.vector.update_app.UpdateAppManager

/**
 *
 * @ProjectName:    thepalacemuseumdaily
 * @Package:        com.lkgood.thepalacemuseumdaily.common.utils.version
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

}