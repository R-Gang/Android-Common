package com.gang.library.common.utils

import java.sql.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.library.common.utils
 * @ClassName:      DateUtils
 * @Description:     java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     2020/8/3 17:18
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/3 17:18
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
object DateUtils {
    //获取当前时间戳
    fun getCurTimeLong(): Long {
        return System.currentTimeMillis()
    }

    //将当前时间戳转换成规定格式
    fun getCurTimeLong(pattern: String?): String {
        val date = Date(getCurTimeLong())
        val format = SimpleDateFormat(pattern)
        return format.format(date)
    }

    //将时间戳转换成规定格式
    fun getDateToString(milSecond: Long, pattern: String?): String {
        var milSecond = milSecond
        if (milSecond < 100000000000L) {
            milSecond *= 1000
        }
        val date = Date(milSecond)
        val format = SimpleDateFormat(pattern)
        return format.format(date)
    }

    fun GetMonth(time: Long): String {
        val format = SimpleDateFormat("MM")
        val d1 = Date(time)
        return format.format(d1)
    }

    fun GetDay(time: Long): String {
        val format = SimpleDateFormat("dd")
        val d1 = Date(time)
        return format.format(d1)
    }

    // 时间
    fun Gethh(time: Long): String {
        //    结果为“0”是上午     结果为“1”是下午
        //    val ca = GregorianCalendar()
        //    println(ca.get(GregorianCalendar.AM_PM));
        val format = SimpleDateFormat("HH")
        val d1 = Date(time)
        return format.format(d1)
    }

    // 获取上午、下午
    //    结果为“0”是上午     结果为“1”是下午
    fun getAmPm(time: Long): String {
        val hh = Gethh(time).toInt()
        val ampm = if (hh > 12) "下午" else "上午"
        return ampm
    }

    fun GetFriendlyTime(time: String?): String {
        if (time == null) {
            return ""
        }
        var milSecond = time.toLong()
        if (milSecond < 100000000000L) {
            milSecond *= 1000
        }
        val date = Date(milSecond)
        return GetFriendlyTime(date)
    }

    fun GetFriendlyTime(time: Date): String { //获取time距离当前的秒数
        val ct = ((System.currentTimeMillis() - time.time) / 1000).toInt()
        if (ct == 0) {
            return "刚刚"
        }
        if (ct in 1..59) {
            return ct.toString() + "秒前"
        }
        if (ct in 60..3599) {
            return Math.max(ct / 60, 1).toString() + "分钟前"
        }
        return if (ct in 3600..86399) (ct / 3600).toString() + "小时前" else getDateToString(
            dateToTimestamp(time),
            "yyyy-MM-dd"
        )
    }

    //    3.2 Date -> Timestamp
//  父类不能直接向子类转化，可借助中间的String
    fun dateToTimestamp(date: Date): Long {
        val ts = Timestamp(date.time)
        return ts.time
    }

    /**
     * 获取日期 昨天-1 今天0 明天1 的日期
     *
     * @return
     */
    fun dateTiem(d: String?, day: Int, type: String?): String {
        val formatter = SimpleDateFormat(type)
        var date: Date? = null
        try {
            date = formatter.parse(d)
            val calendar: Calendar = GregorianCalendar()
            calendar.time = date
            calendar.add(Calendar.DATE, day) //1 把日期往后增加一天.整数往后推,负数往前移动
            date = calendar.time //这个时间就是日期往后推一天的结果
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return formatter.format(date)
    }

    /**
     * 获取n个月后的日期
     *
     * @param date  传入的日期
     * @param month 前一个月-1 这个月0 下个月1 的日期
     * @param type  传入的类型 例:yyyy-MM-dd
     * @return
     */
    fun getMonthAgo(date: Date?, month: Int, type: String?): String {
        val simpleDateFormat = SimpleDateFormat(type)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MONTH, month)
        return simpleDateFormat.format(calendar.time)
    }

    /**
     * 根据指定的日期字符串获取星期几
     *
     * @param strDate 指定的日期字符串(yyyy-MM-dd 或 yyyy/MM/dd)
     * @return week
     * 星期几(MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY)
     */
    fun getWeekByDateStr(strDate: String): String {
        val year = strDate.substring(0, 4).toInt()
        val month = strDate.substring(5, 7).toInt()
        val day = strDate.substring(8, 10).toInt()
        val c = Calendar.getInstance()
        c[Calendar.YEAR] = year
        c[Calendar.MONTH] = month - 1
        c[Calendar.DAY_OF_MONTH] = day
        var week = ""
        val weekIndex = c[Calendar.DAY_OF_WEEK]
        when (weekIndex) {
            1 -> week = "星期日"
            2 -> week = "星期一"
            3 -> week = "星期二"
            4 -> week = "星期三"
            5 -> week = "星期四"
            6 -> week = "星期五"
            7 -> week = "星期六"
        }
        return week
    }

    /**
     * 判断时间在时间段以内如（7:00-22:00）
     *
     * @param s
     * @param e
     * @return
     * @throws ParseException
     */
    @Throws(ParseException::class)
    fun isTimeRange(
        startHour: String?,
        s: String?,
        e: String?,
    ): Boolean {
        val df = SimpleDateFormat("HH:mm")
        val now = df.parse(startHour)
        val begin = df.parse(String.format("%s:00", s))
        val end = df.parse(String.format("%s:00", e))
        val nowTime = Calendar.getInstance()
        nowTime.time = now
        val beginTime = Calendar.getInstance()
        beginTime.time = begin
        val endTime = Calendar.getInstance()
        endTime.time = end
        return nowTime.before(endTime) && nowTime.after(beginTime)
    }

    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    fun IsToday(day: String?): Boolean {
        try {
            val pre = Calendar.getInstance()
            val predate = Date(System.currentTimeMillis())
            pre.time = predate
            val cal = Calendar.getInstance()
            val date = getDateFormat()!!.parse(day)
            cal.time = date
            if (cal[Calendar.YEAR] == pre[Calendar.YEAR]) {
                val diffDay = (cal[Calendar.DAY_OF_YEAR]
                        - pre[Calendar.DAY_OF_YEAR])
                if (diffDay == 0) {
                    return true
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 判断是否为昨天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    @Throws(ParseException::class)
    fun IsYesterday(day: String?): Boolean {
        val pre = Calendar.getInstance()
        val predate = Date(System.currentTimeMillis())
        pre.time = predate
        val cal = Calendar.getInstance()
        val date = getDateFormat()!!.parse(day)
        cal.time = date
        if (cal[Calendar.YEAR] == pre[Calendar.YEAR]) {
            val diffDay = (cal[Calendar.DAY_OF_YEAR]
                    - pre[Calendar.DAY_OF_YEAR])
            if (diffDay == -1) {
                return true
            }
        }
        return false
    }

    fun getDateFormat(): SimpleDateFormat? {
        if (null == DateLocal.get()) {
            DateLocal.set(
                SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.CHINA
                )
            )
        }
        return DateLocal.get()
    }

    private val DateLocal =
        ThreadLocal<SimpleDateFormat?>()

    /**
     * 将字符串转为时间戳
     *
     * @param time    2016-1-25
     * @param argType
     * @return
     */
    fun getStringToDate(time: String?, argType: String?): Long {
        var strType = argType
        if (argType == null) {
            strType = "yyyy-MM-dd"
        }
        val sdf = SimpleDateFormat(strType)
        var date = Date()
        try {
            date = sdf.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date.time / 1000
    }

    /**
     * 获取今天开始的 0点 0 分
     *
     * @return
     */
    fun getToday(): Long {
        val strToday = getCurTimeLong("yyyy-MM-dd日")
        return getStringToDate("$strToday 00:00:00", null)
    }

    /**
     * 获取今天 结束的 0点 0 分
     *
     * @return
     */
    fun getTomorrow(): Long {
        val today = getToday()
        return today + 24 * 60 * 60
    }

    /**
     * 获取后天开始的 0点 0 分
     *
     * @return
     */
    fun getAfterTomorrow(): Long {
        val today = getTomorrow()
        return today + 24 * 60 * 60
    }
}