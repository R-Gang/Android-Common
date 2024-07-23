package com.gang.app.ui.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import com.alibaba.sdk.android.push.notification.CPushMessage
import com.alibaba.sdk.android.push.popup.PopupNotifyClick
import com.gang.app.R
import com.gang.app.common.user.ToUIEvent
import com.gang.library.common.fit.thread.ThreadPoolManager
import com.gang.library.base.BaseActivity
import com.gang.library.ui.widget.swipeback.helper.SwipeBackHelper
import com.gang.library.ui.widget.swipeback.helper.dispatchTouchEvent
import com.gang.tools.kotlin.dimension.checkNavigationBarShow
import com.gang.tools.kotlin.dimension.navigationBarHeight
import com.gang.tools.kotlin.dimension.screenArray
import com.gang.tools.kotlin.dimension.screenDpiArray
import com.gang.tools.kotlin.utils.LogUtils
import com.gang.tools.kotlin.utils.showToast
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask

/**
 * 1.Android 仿全面屏侧滑返回功能
 * 2.线程池使用示例
 */
class HttpApiActivity : BaseActivity() {

    private var countDownTimer: CountDownTimer? = null
    val phoneNum = "18510507183"

    var mSwipeBackHelper: SwipeBackHelper? = null

    override val layoutId: Int
        get() = R.layout.activity_http_api

    override fun initView(savedInstanceState: Bundle?) {

        if (mNotchScreen.not()) {
            // 初始化侧滑返回帮助类
            mSwipeBackHelper = SwipeBackHelper(this)
        }

        dark()


        // 线程池使用示例
        val executorService =
            Executors.newSingleThreadExecutor() //唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行(同步)
        for (i in 1..pageIndex) { //刷新到当前item
            val syncRunnable = Runnable {
                //RequestMomentList(i)
            }
            executorService.execute(syncRunnable)
        }
        executorService.shutdown()
        val thread: Thread = object : Thread() {
            @Synchronized
            override fun run() {
                super.run()
                while (true) { // 实时检测线程池是否上传完成
                    if (executorService.isTerminated) {
                        // mGrowthHistoryRecyclerView.scrollToPosition(position1 + 2)
                        // MoveToPosition(layoutManager, position1 + 2);//提交完跳转指定位置
                        break
                    }
                }
            }
        }
        ThreadPoolManager.instance?.execute(FutureTask(thread, null), null)

        LogUtils.i(this, "${screenArray[0]}===${screenArray[1]}")
        LogUtils.i(this, "${screenDpiArray[0]}===${screenDpiArray[1]}")
        LogUtils.i(this, "${checkNavigationBarShow()}===$navigationBarHeight")

    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        // 使用侧滑处理
        return mSwipeBackHelper.dispatchTouchEvent(ev) {
            super.dispatchTouchEvent(ev)
        }
    }

    //重写Activity该方法，当窗口焦点变化时自动隐藏system bar，这样可以排除在弹出dialog和menu时，system bar会重新显示的问题。
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    override fun initData() {

        //离线推送接受的消息进行处理
        PopupNotifyClick { title, summary, extMap ->
            LogUtils.d(
                "PopupNotifyClick",
                "收到一条离线推送接受的消息, title: $title, summary: $summary, extraMap: $extMap"
            )
            val cPushMessage = CPushMessage()
            cPushMessage.title = title
            cPushMessage.content = summary
//            NotificationUtil.buildNotification(this, cPushMessage);

            val jietuId = extMap.get("jietuId")
            val jietuType = extMap.get("jietuType")
//            toActivity<HttpApiActivity>(
//                TYPE_KEY to TYPE_SINGLE_TOPIC,
//                TYPE_VALUE_KEY to jietuId,
//                CommonCode.joinContentKey to jietuType
//            )

        }.onCreate(this, this.intent)

    }

    // 倒计时
    private fun countDown() {
//        phoneNum = etPhone.getText().toString()
        //        if (!phoneNum.matches("0?(13|14|15|17|18)[0-9]{9}")) {
        if (phoneNum.length != 11) {
            showToast("请输入正确的手机号码")
            return
        }
//        tvGetCode.setClickable(false)
        if (countDownTimer == null) countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(l: Long) {
//                tvGetCode.setText((l / 1000).toString() + "S")
            }

            override fun onFinish() {
//                tvGetCode.setClickable(true)
//                tvGetCode.setText("获取验证码")
//                tvGetCode.setOnClickListener(View.OnClickListener { view: View? -> getRandomCode() })
            }
        }
        countDownTimer!!.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }

    // eventbus 演示
    override fun onEvent(any: Any) {
        (any as ToUIEvent).apply {
            if (tag == ToUIEvent.MESSAGE_EVENT) {
                LogUtils.e("", "MainActivity===eventbus 演示")
                showToast(obj.toString())
            }
        }
    }

}