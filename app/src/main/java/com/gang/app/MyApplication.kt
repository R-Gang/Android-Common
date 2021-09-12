package com.gang.app

import com.gang.app.common.user.Configs
import com.gang.library.BaseApplication
import com.gang.library.common.user.Config
import com.lzy.okgo.OkGo
import com.zhy.http.okhttp.OkHttpUtils


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

        struct()

        // 版本更新
        if (Configs.isOpenVersionUpdate) {
            // okhttp-utils
            OkHttpUtils.getInstance()
                .init(this)
                .debug(true, "okHttp")
                .timeout(20 * 1000)
            OkGo.getInstance().init(this)
        }
    }

    companion object {

    }
}