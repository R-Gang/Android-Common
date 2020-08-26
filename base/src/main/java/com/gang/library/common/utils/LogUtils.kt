package com.gang.library.common.utils

import com.apkfuns.logutils.Log2FileConfig
import com.apkfuns.logutils.LogConfig
import com.apkfuns.logutils.LogUtils

/**
 *
 * @ProjectName:    Android-Common
 * @Package:        com.gang.library.common.utils
 * @ClassName:      LogUtils
 * @Description:    日志管理器
 * @Author:         haoruigang
 * @CreateDate:     2020/8/25 18:50
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/25 18:50
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
object LogUtils {

    /**
     * 选项配置
     *
     * @return LogConfig
     */
    fun getLogConfig(): LogConfig {
        return LogUtils.getLogConfig()
    }

    /**
     * 日志写入文件相关配置
     * @return LogConfig
     */
    fun getLog2FileConfig(): Log2FileConfig? {
        return LogUtils.getLog2FileConfig()
    }

    /**
     * verbose输出
     *
     * @param msg
     * @param args
     */
    fun v(msg: String?, vararg args: Any?) {
        LogUtils.v(msg, *args)
    }

    fun v(`object`: Any?) {
        LogUtils.v(`object`)
    }


    /**
     * debug输出
     *
     * @param msg
     * @param args
     */
    fun d(msg: String?, vararg args: Any?) {
        LogUtils.d(msg, *args)
    }

    fun d(`object`: Any?) {
        LogUtils.d(`object`)
    }

    /**
     * info输出
     *
     * @param msg
     * @param args
     */
    fun i(msg: String?, vararg args: Any?) {
        LogUtils.i(msg, *args)
    }

    fun i(`object`: Any?) {
        LogUtils.i(`object`)
    }

    /**
     * warn输出
     *
     * @param msg
     * @param args
     */
    fun w(msg: String?, vararg args: Any?) {
        LogUtils.w(msg, *args)
    }

    fun w(`object`: Any?) {
        LogUtils.w(`object`)
    }

    /**
     * error输出
     *
     * @param msg
     * @param args
     */
    fun e(msg: String?, vararg args: Any?) {
        LogUtils.e(msg, *args)
    }

    fun e(`object`: Any?) {
        LogUtils.e(`object`)
    }

    /**
     * assert输出
     *
     * @param msg
     * @param args
     */
    fun wtf(msg: String?, vararg args: Any?) {
        LogUtils.wtf(msg, *args)
    }

    fun wtf(`object`: Any?) {
        LogUtils.wtf(`object`)
    }

    /**
     * 打印json
     *
     * @param json
     */
    fun json(json: String?) {
        LogUtils.json(json)
    }

    /**
     * 输出xml
     * @param xml
     */
    fun xml(xml: String?) {
        LogUtils.xml(xml)
    }

}