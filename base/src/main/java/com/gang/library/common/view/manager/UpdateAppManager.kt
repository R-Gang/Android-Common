package com.gang.library.common.view.manager

import com.vector.update_app.UpdateAppManager

/**
 *
 * @ProjectName:    Android-Common
 * @Package:        com.gang.library.common.user
 * @ClassName:      UpdateAppManager
 * @Description:     java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     8/20/21 7:59 PM
 * @UpdateUser:     更新者：
 * @UpdateDate:     8/20/21 7:59 PM
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class UpdateAppManager  {

    fun getInstance(): UpdateAppManager.Builder {
        return UpdateAppManager.Builder();
    }
}