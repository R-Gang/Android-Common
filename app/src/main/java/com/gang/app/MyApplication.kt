package com.gang.app

import com.gang.app.data.ProvinceData
import com.gang.library.BaseApplication
import com.gang.library.common.user.Config


class MyApplication : BaseApplication() {

    override fun onCreate() {
        // 是否显示日志
        Config.isShowLog = true

        // 阿里云配置参数
        Config.accessKeyId = ""
        Config.accessKeySecret = ""
        val ossBucket = ""
        Config.ENDPOINT = "http://oss-cn-beijing.aliyuncs.com/"
        Config.OSS_URL = "https://$ossBucket.oss-cn-beijing.aliyuncs.com/"

        super.onCreate()

        init()
    }

    private fun init() {
        Thread {
            // 子线程中解析省市区数据
            ProvinceData.initJsonData()
        }.start()
    }

    companion object {

    }
}