package com.gang.library.common.user

import android.util.Log
import java.util.*
import java.util.concurrent.*

/**
 * Created by haoruigang on 2018/4/13 9:55.
 * 实现高性能，高并发，可延时线程池管理
 */
class ThreadPoolManager private constructor() {
    var threadPoolExecutor: ThreadPoolExecutor? = null
    private val service = LinkedBlockingQueue<Future<*>>()
    private val runnable = Runnable {
        while (true) {
            var futureTask: FutureTask<*>? = null
            try {
                Log.e("myThreadPook", "service size " + service.size)
                futureTask = service.take() as FutureTask<*>
                Log.e("myThreadPook", "池  " + threadPoolExecutor?.poolSize)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            if (futureTask != null) {
                threadPoolExecutor?.execute(futureTask)
            }
        }
    }

    fun <T> execute(futureTask: FutureTask<T>?, delayed: Any?) {
        if (futureTask != null) {
            try {
                if (delayed != null) {
                    val timer = Timer()
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            try {
                                service.put(futureTask)
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                            }
                        }
                    }, delayed as Long)
                } else {
                    service.put(futureTask)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    private val handler =
        RejectedExecutionHandler { runnable: Runnable?, threadPoolExecutor: ThreadPoolExecutor? ->
            try {
                service.put(FutureTask<Any?>(runnable, null)) //
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

    fun <T> removeThread() {
        service.poll()
    }

    companion object {
        //假设：3个线程－－－并发线程
        //场景：同时调用getInstace方法
        //线程一：
        //在服务器，或者多线程访问
        //服务器并发
        @get:Synchronized
        var instance: ThreadPoolManager? = null
            get() {
                //假设：3个线程－－－并发线程
                //场景：同时调用getInstace方法
                //线程一：
                if (field == null) {
                    field = ThreadPoolManager()
                }
                return field
            }
            private set
    }

    init {
        threadPoolExecutor = ThreadPoolExecutor(4, 10, 10, TimeUnit.SECONDS,
            ArrayBlockingQueue(4), handler)
        threadPoolExecutor?.execute(runnable)
    }
}