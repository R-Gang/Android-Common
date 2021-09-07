package com.gang.library.common.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.preference.PreferenceManager
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.*
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.gang.library.BaseApplication
import com.gang.library.common.user.Config
import java.io.UnsupportedEncodingException
import java.lang.reflect.Array
import java.nio.charset.Charset
import java.security.MessageDigest
import java.util.regex.Pattern
import kotlin.experimental.and

/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.app.common.utils
 * @ClassName:      U
 * @Description:     java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     2020/8/3 17:30
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/3 17:30
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
object U {

    private var toast: Toast? = null

    /**
     * Toast
     *
     * @param msg
     */
    fun showToast(msg: String?) {
        showToast(msg, Toast.LENGTH_SHORT)
    }

    /**
     * Toast
     *
     * @param msg
     * @param duration Toast.LENGTH_LONG
     */
    @SuppressLint("ShowToast")
    fun showToast(msg: String?, duration: Int) {
        if (BaseApplication.appContext != null) {
            if (toast == null) {
                toast = Toast.makeText(BaseApplication.appContext, msg, duration)
                toast?.setGravity(Gravity.CENTER, 0, 0)
            } else {
                toast?.setText(msg)
            }
            toast?.show()
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param argEditText
     */
    fun hideSoftKeyboard(argEditText: EditText) {
        val imm =
            (BaseApplication.appContext!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        imm.hideSoftInputFromWindow(argEditText.windowToken, 0)
    }

    /***
     * 隐藏软键盘
     */
    fun hideKeyboard(activity: Activity) {
        val inputManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity.currentFocus
        if (view != null) {
            inputManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    /**
     * map转为字符串
     *
     * @param map
     * @return
     */
    fun transMap2String(map: Map<*, *>): String {
        var entry: Map.Entry<*, *>
        val sb = StringBuilder()
        val iterator: Iterator<*> = map.entries.iterator()
        while (iterator.hasNext()) {
            entry = iterator.next() as Map.Entry<*, *>
            sb.append(entry.key.toString()).append("=")
                .append(if (null == entry.value) "" else entry.value.toString())
                .append(if (iterator.hasNext()) "&" else "")
        }
        return sb.toString()
    }

    /**
     * 获取版本号信息
     */
    fun getVersionName(): String? {
        return try {
            val packageInfo: PackageInfo =
                BaseApplication.appContext!!.packageManager.getPackageInfo(
                    BaseApplication.appContext!!.packageName,
                    PackageManager.GET_CONFIGURATIONS
                )
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "unknown version"
        }
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    fun isNetConnected(context: Context): Boolean {
        val connectivity = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (null != connectivity) {
            val info = connectivity.activeNetworkInfo
            if (null != info && info.isConnected) {
                return info.state == NetworkInfo.State.CONNECTED
            }
        }
        return false
    }

    /**
     * 判断是否是wifi连接
     */
    fun isWifiConnected(context: Context): Boolean {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * 打开网络设置界面
     */
    fun openNetSetting(activity: Activity) {
        val intent = Intent("/")
        val cm =
            ComponentName("com.android.settings", "com.android.settings.WirelessSettings")
        intent.component = cm
        intent.action = "android.intent.action.VIEW"
        activity.startActivityForResult(intent, 0)
    }

    /**
     * SharePreference本地存储
     *
     * @param key
     * @param value
     */
    fun savePreferences(key: String?, value: Any?) {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(BaseApplication.appContext)
        val editor = sharedPreferences.edit()
        when (value) {
            is String -> editor.putString(key, value as String?)
            is Int -> editor.putInt(key, (value as Int?)!!)
            is Long -> editor.putLong(key, (value as Long?)!!)
            is Float -> editor.putFloat(key, (value as Float?)!!)
            is Boolean -> editor.putBoolean(key, (value as Boolean?)!!)
        }
        editor.apply()
    }

    /**
     * 根据key和默认值的数据类型，获取SharePreference中所存值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    fun getPreferences(key: String?, defaultObject: Any): Any? {
        val type = defaultObject.javaClass.simpleName
        val sp =
            PreferenceManager.getDefaultSharedPreferences(BaseApplication.appContext)
        if ("String" == type) {
            return sp.getString(key, defaultObject as String)
        } else if ("Integer" == type) {
            return sp.getInt(key, (defaultObject as Int))
        } else if ("Boolean" == type) {
            return sp.getBoolean(key, (defaultObject as Boolean))
        } else if ("Float" == type) {
            return sp.getFloat(key, (defaultObject as Float))
        } else if ("Long" == type) {
            return sp.getLong(key, (defaultObject as Long))
        }
        return null
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    fun stringFilter(str: String): String {
        var str = str
        str = str.replace("【".toRegex(), "[").replace("】".toRegex(), "]")
            .replace("！".toRegex(), "!").replace("：".toRegex(), ":") // 替换中文标号
        val regEx = "[『』]" // 清除掉特殊字符
        val p = Pattern.compile(regEx)
        val m = p.matcher(str)
        return m.replaceAll("").trim { it <= ' ' }
    }

    /**
     * 将字符串进行md5加密
     *
     * @param argString 明文
     * @return d5加密后的密文
     */
    fun MD5(argString: String): String? {
        val hexDigits = charArrayOf(
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'a',
            'b',
            'c',
            'd',
            'e',
            'f'
        )
        return try {
            val btInput = argString.toByteArray()
            // 获得MD5摘要算法的 MessageDigest 对象
            val mdInst = MessageDigest.getInstance("MD5")
            // 使用指定的字节更新摘要
            mdInst.update(btInput)
            // 获得密文
            val md = mdInst.digest()
            // 把密文转换成十六进制的字符串形式
            val j = md.size
            val str = CharArray(j * 2)
            var k = 0
            for (byte0 in md) {
                str[k++] = hexDigits[byte0.toInt().ushr(4) and 0xf]
                str[k++] = hexDigits[(byte0 and 0xf).toInt()]
            }
            String(str)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun MD5_16(argString: String): String? {
        val hexDigits = charArrayOf(
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'a',
            'b',
            'c',
            'd',
            'e',
            'f'
        )
        return try {
            val btInput = argString.toByteArray()
            // 获得MD5摘要算法的 MessageDigest 对象
            val mdInst = MessageDigest.getInstance("MD5")
            // 使用指定的字节更新摘要
            mdInst.update(btInput)
            // 获得密文
            val md = mdInst.digest()
            // 把密文转换成十六进制的字符串形式
            val j = md.size
            val str = CharArray(j * 2)
            var k = 0
            for (byte0 in md) {
                str[k++] = hexDigits[byte0.toInt().ushr(4) and 0xf]
                str[k++] = hexDigits[(byte0 and 0xf).toInt()]
            }
            val strResult = String(str)
            strResult.substring(8, 24)
            //return str.toString();
        } catch (e: Exception) {
            Log.i("----", e.toString())
            e.printStackTrace()
            null
        }
    }

    /**
     * 判断字符串格式是否为手机号码
     *
     * @param phoneNumber
     * @return
     */
    fun isPhoneNum(phoneNumber: String): Boolean {
        return !TextUtils.isEmpty(phoneNumber) && phoneNumber.matches("0?(13|14|15|16|17|18|19)[0-9]{9}" as Regex)
    }

    /**
     * 判断字符串格式是否为邮箱
     *
     * @param email
     * @return
     */
    fun isEMail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && email.matches("^[a-zA-Z0-9_-_.]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$" as Regex)
    }

    /**
     * 描述：将null转化为“”.
     *
     * @param str 指定的字符串
     * @return 字符串的String类型
     */
    fun parseEmpty(str: String?): String {
        var str = str
        if (str == null || "null" == str.trim { it <= ' ' }) {
            str = ""
        }
        return str.trim { it <= ' ' }
    }

    /**
     * 描述：判断一个字符串是否为null或空值.
     *
     * @param str 指定的字符串
     * @return true or false
     */
    fun isEmpty(str: String?): Boolean {
        return str == null || str.trim { it <= ' ' }.isEmpty()
    }

    fun isNotEmpty(str: String?): Boolean {
        return !isEmpty(str)
    }

    /**
     * 描述：判断一个集合是否为null或空值.
     *
     * @param str 指定的集合
     * @return true or false
     */
    fun isEmpty(list: List<*>?): Boolean {
        return list == null || list.isEmpty()
    }

    fun isNotEmpty(list: List<*>?): Boolean {
        return !isEmpty(list)
    }

    /**
     * 描述：是否只是字母和数字.
     *
     * @param str 指定的字符串
     * @return 是否只是字母和数字:是为true，否则false
     */
    fun isNumberLetter(str: String): Boolean {
        var isNoLetter = false
        val expr = "^[A-Za-z0-9]+$" as Regex
        if (str.matches(expr)) {
            isNoLetter = true
        }
        return isNoLetter
    }

    /**
     * 描述：是否只是数字.
     *
     * @param str 指定的字符串
     * @return 是否只是数字:是为true，否则false
     */
    fun isNumber(str: String): Boolean {
        var isNumber = false
        val expr = "^[0-9]+$" as Regex
        if (str.matches(expr)) {
            isNumber = true
        }
        return isNumber
    }

    /**
     * 描述：是否是中文.
     *
     * @param str 指定的字符串
     * @return 是否是中文:是为true，否则false
     */
    fun isChinese(str: String): Boolean {
        var isChinese = true
        val chinese = "[\u0391-\uFFE5]" as Regex
        if (!isEmpty(str)) { //获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
            for (i in str.indices) { //获取一个字符
                val temp = str.substring(i, i + 1)
                //判断是否为中文字符
                if (temp.matches(chinese)) {
                } else {
                    isChinese = false
                }
            }
        }
        return isChinese
    }

    /**
     * 描述：是否包含中文.
     *
     * @param str 指定的字符串
     * @return 是否包含中文:是为true，否则false
     */
    fun isContainChinese(str: String): Boolean {
        var isChinese = false
        val chinese = "[\u0391-\uFFE5]" as Regex
        if (!isEmpty(str)) { //获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
            for (i in str.indices) { //获取一个字符
                val temp = str.substring(i, i + 1)
                //判断是否为中文字符
                if (temp.matches(chinese)) {
                    isChinese = true
                } else {
                }
            }
        }
        return isChinese
    }

    /**
     * 判断对象是否为空
     *
     * @param obj 对象
     * @return `true`: 为空<br></br>`false`: 不为空
     */
    fun isEmpty(obj: Any?): Boolean {
        if (obj == null) {
            return true
        }
        if (obj is String && obj.toString().isEmpty()) {
            return true
        }
        if (obj is Editable && obj.toString().isEmpty()) {
            return true
        }
        if (obj is CharSequence && obj.toString().isEmpty()) {
            return true
        }
        if (obj.javaClass.isArray && Array.getLength(obj) == 0) {
            return true
        }
        if (obj is Collection<*> && obj.isEmpty()) {
            return true
        }
        if (obj is Map<*, *> && obj.isEmpty()) {
            return true
        }
        if (obj is SparseArray<*> && obj.size() == 0) {
            return true
        }
        if (obj is SparseBooleanArray && obj.size() == 0) {
            return true
        }
        if (obj is SparseIntArray && obj.size() == 0) {
            return true
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (obj is SparseLongArray && obj.size() == 0) {
                return true
            }
        }
        return false
    }

    /**
     * 判断对象是否非空
     *
     * @param obj 对象
     * @return `true`: 非空<br></br>`false`: 空
     */
    fun isNotEmpty(obj: Any?): Boolean {
        return !isEmpty(obj)
    }

    /**
     * 对比时间
     *
     * @param time 对象
     * @return `true`: 非空<br></br>`false`: 空
     */
    fun timeEqual(time: String?): String {
        val type = "0" //0 时间相等  1传入的时间大于本地时间  -1传入的时间小于本地时间
        val curTime = System.currentTimeMillis() / 1000
        return if (curTime < java.lang.Long.valueOf(time!!)) { //比较
            "1"
        } else if (curTime > java.lang.Long.valueOf(time)) {
            "-1"
        } else {
            "0"
        }
    }

    /**
     * 替换空字符串
     *
     * @param s
     * @return
     */
    fun getReplaceTrim(s: String): String {
        return s.trim { it <= ' ' }.replace(" ", "")
    }

    /**
     * dp转px
     */
    fun dip2px(context: Context, dpValue: Int): Int {
        return dip2px(context, dpValue.toFloat())
    }

    /**
     * dp转px
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * px转dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * px转sp
     */
    fun px2sp(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue / fontScale + 0.5f).toInt()
    }

    /**
     * sp转px
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 字符串（含中文）转16进制
     *
     * @param str
     * @return
     */
    fun SendS(str: String): ByteArray {
        var ok = ByteArray(0)
        try {
            ok = str.toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return ok
    }

    /**
     * 16进制转字符串（含中文）
     *
     * @param bytes
     * @return
     */
    fun getString(bytes: ByteArray?): String {
        return String(bytes!!, Charset.forName("UTF-8"))
    }

    /**
     * 将系统表情转化为字符串
     *
     * @param s
     * @return
     */
    fun getString(s: String): String {
        val length = s.length
        var context = ""
        //循环遍历字符串，将字符串拆分为一个一个字符
        for (i in 0 until length) {
            val codePoint = s[i]
            //判断字符是否是emoji表情的字符
            if (isEmojiCharacter(codePoint)) { //如果是将以大括号括起来
                val emoji =
                    "{" + Integer.toHexString(codePoint.toInt()) + "}"
                context += emoji
                continue
            }
            context += codePoint
        }
        return context
    }

    /**
     * 是否包含表情
     *
     * @param codePoint
     * @return 如果不包含 返回false,包含 则返回true
     */
    private fun isEmojiCharacter(codePoint: Char): Boolean {
        return !(codePoint.toInt() == 0x0 || codePoint.toInt() == 0x9 || codePoint.toInt() == 0xA
                || codePoint.toInt() == 0xD
                || codePoint.toInt() in 0x20..0xD7FF
                || codePoint.toInt() in 0xE000..0xFFFD || codePoint.toInt() in 0x10000..0x10FFFF)
    }

    /**
     * 将表情描述转换成表情
     *
     * @param str
     * @return
     */
    fun getEmoji(str: String): String {
        var string = str
        val rep = "\\{(.*?)\\}"
        val p = Pattern.compile(rep)
        val m = p.matcher(string)
        while (m.find()) {
            val s1 = m.group().toString()
            val s2 = s1.substring(1, s1.length - 1)
            var s3: String
            try {
                s3 = (s2.toInt(16) as Char).toString()
                string = string.replace(s1, s3)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return string
    }

    /**
     * TextView实现部分文字可点击
     *
     * @param textView
     * @param start
     * @param end
     */
    fun setSpannable(textView: TextView, start: Int, end: Int, click: ClickableSpans) {
        val spanUser = SpannableStringBuilder(textView.text)
        spanUser.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                click.clickable(widget)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                click.updateDrawState(ds)
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = spanUser
    }

    /**
     * 检查图片类型
     */
    fun pictureToVideo(pictureType: String): Int {
        if (!TextUtils.isEmpty(pictureType)) {
            if (pictureType.startsWith("video") ||
                pictureType.startsWith("mp4")
            ) {
                return Config.TYPE_VIDEO
            } else if (pictureType.startsWith("audio")) {
                return Config.TYPE_AUDIO
            } else if (pictureType.contains("pdf")) {
                return Config.TYPE_PDF
            } else if (pictureType.contains("xls")) {
                return Config.TYPE_XLS
            } else if (pictureType.contains("doc")) {
                return Config.TYPE_DOC
            } else if (pictureType.contains("zip")) {
                return Config.TYPE_ZIP
            } else if (pictureType.contains("txt")) {
                return Config.TYPE_TXT
            } else {
                return Config.TYPE_FILE
            }
        }
        return Config.TYPE_IMAGE
    }

    interface ClickableSpans {
        fun clickable(widget: View)

        fun updateDrawState(ds: TextPaint){
            //删除下划线
            ds.isUnderlineText = false
        }
    }
}
