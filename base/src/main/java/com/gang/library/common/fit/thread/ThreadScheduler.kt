package com.gang.library.common.fit.thread

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * description：
 * ===============================
 * creator：anjihang
 * create time：2017/8/31 15:15
 * ===============================
 * reasons for modification：
 * Modifier：
 * Modify time：
 */
class ThreadScheduler private constructor() {

    private val mWorkThreadExecuter: ExecutorService
    private val mUIThreadHandler: Handler

    fun runOnWorkThread(runnable: Runnable?) {
        if (null != runnable && !mWorkThreadExecuter.isShutdown) {
            mWorkThreadExecuter.execute(runnable)
        }
    }

    fun runOnUiThread(runnable: Runnable?) {
        if (null != runnable) {
            mUIThreadHandler.post(runnable)
        }
    }

    companion object {
        @Volatile
        private var sScheduler: ThreadScheduler? = null
        fun get(): ThreadScheduler? {
            if (sScheduler == null) {
                synchronized(ThreadScheduler::class.java) {
                    if (sScheduler == null) {
                        sScheduler = ThreadScheduler()
                    }
                }
            }
            return sScheduler
        }
    }

    init {
        mWorkThreadExecuter = Executors.newFixedThreadPool(3)
        mUIThreadHandler = Handler(Looper.getMainLooper())
    }
}