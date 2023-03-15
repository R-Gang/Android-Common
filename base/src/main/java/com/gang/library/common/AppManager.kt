package com.gang.library.common

import android.app.Activity
import android.content.Context
import android.os.Process
import java.util.*
import kotlin.system.exitProcess

/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.app.common
 * @ClassName:      AppManager
 * @Description:    Activity管理类：用于管理Activity和退出程序
 * @Author:         haoruigang
 * @CreateDate:     2020/8/3 17:50
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/3 17:50
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class AppManager private constructor() {
    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack?.add(activity)
    }

    /**
     *  获取上一个打开的Activity（堆栈中倒数第二个压入的）
     *  @param index 默认是上一个Activity
     */
    fun previousActivity(index: Int = activityStack?.size as Int - 2): Activity? {
        return activityStack?.elementAt(index)
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity? {
        return activityStack?.lastElement()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        val activity = activityStack?.lastElement()
        activity?.let { finishActivity(it) }
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity) {
        activityStack?.remove(activity)
        activity.finish()
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        activityStack?.apply {
            for (activity in this) {
                if (activity?.javaClass == cls) {
                    finishActivity(activity)
                    break
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        activityStack?.apply {
            for (i in indices) {
                if (null != this[i]) {
                    this[i]?.finish()
                }
            }
            activityStack?.clear()
        }
    }

    /**
     * 退出应用程序
     */
    fun AppExit(context: Context?) {
        try {
            finishAllActivity()
            //退出程序
            Process.killProcess(Process.myPid())
            exitProcess(1)
        } catch (e: Exception) {
        }
    }

    companion object {
        // Activity栈
        var activityStack: Stack<Activity>? = null

        // 单例模式
        private var instance: AppManager? = null

        /**
         * 单一实例
         */
        val appManager: AppManager?
            get() {
                if (instance == null) {
                    instance = AppManager()
                }
                return instance
            }
    }
}