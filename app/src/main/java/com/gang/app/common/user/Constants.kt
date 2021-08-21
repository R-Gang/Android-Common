package com.gang.app.common.user

/**
 *
 * @ProjectName:    Android-Common
 * @Package:        com.gang.app.common.user
 * @ClassName:      Constants
 * @Description:     java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     4/7/21 3:44 PM
 * @UpdateUser:     更新者：
 * @UpdateDate:     4/7/21 3:44 PM
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class Constants {

    companion object {

        val BASE_VERSION = "https://gitee.com/ganghan/version-update/raw/master/"
        var VERSION_PATH = BASE_VERSION + "mrgg_version_update.txt"

        val REQUEST_CODE_WRITE: Int=9090

        // 是否测试服
        var isTest = true

        //  https://newApiT.ilovegrowth.cn/(测试环境)  http://newapi.acz.1bu2bu.com/（开发环境） https://api.ilovegrowth.cn/（生产环境）
        private val BASE_API =
            if (isTest) "http://newapi.acz.1bu2bu.com/" else "https://newApiT.ilovegrowth.cn/"


        // 1.获取验证码
        var GET_RANDOM_CODE = BASE_API + "code"


    }

}