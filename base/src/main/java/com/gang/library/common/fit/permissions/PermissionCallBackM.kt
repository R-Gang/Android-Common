package com.gang.library.common.fit.permissions

/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.app.common.utils.permissions
 * @ClassName:      PermissionCallBackM
 * @Description:     java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     2020/8/3 16:36
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/3 16:36
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
open interface PermissionCallBackM {
    fun onPermissionGrantedM(requestCode: Int, vararg perms: String?)
    fun onPermissionDeniedM(requestCode: Int, vararg perms: String?) {}
}