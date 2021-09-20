package com.gang.library.ui.widget

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.gang.library.R


/**
 *
 * @Description:     java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     2021/9/1 12:09
 * @UpdateUser:     更新者：
 * @UpdateDate:     2021/9/1 12:09
 * @UpdateRemark:   更新说明：
 * @Version:
 */
class ToastCustom(context: Context?) : Toast(context) {
    companion object {
        private var mToast: Toast? = null
        fun showToast(context: Context, content: String?) {

            //获取系统的LayoutInflater
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view: View = inflater.inflate(R.layout.toast_layout, null)
            val tv_content = view.findViewById<TextView>(R.id.tv_content)
            tv_content.text = content

            //实例化toast
            mToast = Toast(context)
            mToast?.view = view
            mToast?.duration = LENGTH_SHORT
            mToast?.setGravity(Gravity.CENTER, 0, 0)
            mToast?.show()
        }
    }
}