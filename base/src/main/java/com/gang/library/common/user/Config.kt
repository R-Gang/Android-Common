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

    // CALL_PHONE 权限
    const val REQUEST_CALL_PHONE = 100
    // READ_WRITE 权限
    const val REQUEST_CODE_READ_WRITE = 101
    // CAMERA 权限
    const val REQUEST_CAMERA = 102

    /**
     * startActivityForResult requestCode
     */
    const val toActivityRequestCode=0x000001

    /**
     * 扫描跳转Activity RequestCode
     */
    const val REQUEST_CODE_CAMERE =0x000002

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

    // 设置全局字体("HYXinRenWenSongW.ttf")
    var typefaceAll = "HYXinRenWenSongW.ttf"

    // 是否开启设置页面(默认开启)
    var setContentView = true
    // 是否开启全局页面管理(默认开启)
    var activityEnabled = true
    // 是否开启沉浸式状态栏(默认开启)
    var statusBarEnabled = true
    // 是否开启EventBus(默认关闭)
    var eventBusEnabled = false
    // 使用腾讯浏览器X5内核
    var isOpenTBSX5QbSdk = false
}