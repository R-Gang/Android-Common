package com.gang.library.common.user

import com.gang.library.BuildConfig

/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.app.common.user
 * @ClassName:      Config
 * @Description:    项目配置参数
 * @Author:         haoruigang
 * @CreateDate:     2020/8/10 17:26
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/10 17:26
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
object Config {

    //CALL_PHONE权限
    const val RC_CALL_PHONE = 100
    const val REQUEST_CODE_WRITE = 101

    // 文件类型
    const val TYPE_ALL = 0
    const val TYPE_IMAGE = 1
    const val TYPE_VIDEO = 2
    const val TYPE_AUDIO = 3
    const val TYPE_PDF = 4
    const val TYPE_XLS = 5
    const val TYPE_DOC = 6
    const val TYPE_TXT = 7
    const val TYPE_ZIP = 8
    const val TYPE_FILE = 9

    // 是否显示日志
    var isShowLog = BuildConfig.DEBUG

    // 阿里云配置参数
    var accessKeyId = ""
    var accessKeySecret = ""
    var ossBucket = ""
    var ENDPOINT = "http://oss-cn-beijing.aliyuncs.com/"
    var OSS_URL = "https://$ossBucket.oss-cn-beijing.aliyuncs.com/"
}