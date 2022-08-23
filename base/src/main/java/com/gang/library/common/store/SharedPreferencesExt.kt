package com.gang.library.common.store

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Base64
import com.gang.tools.kotlin.utils.mToolsContext
import java.io.*
import java.net.URLDecoder
import java.net.URLEncoder

/**
 *
 * Created on 2020/4/21.
 *
 * @author o.s
 */

/**
 * Get the Editor instance and commit/apply
 * @param commit if true , use [SharedPreferences.Editor.commit], otherwise , use [SharedPreferences.Editor.commit]
 * @param action invoke the action after get the [SharedPreferences.Editor] instance
 */
inline fun SharedPreferences.edit(
    commit: Boolean = false,
    action: SharedPreferences.Editor.() -> Unit,
) {
    val editor = edit()
    action(editor)
    if (commit)
        editor.commit()
    else
        editor.apply()
}

/**
 * Return the SharedPreferences instance
 * @param name Desired preferences file. Default value is the packageName
 * @param mode Operating mode. Default value is [Context.MODE_PRIVATE]
 */
fun Context.sp(name: String = packageName, mode: Int = Context.MODE_PRIVATE): SharedPreferences =
    getSharedPreferences(name, mode)

fun Activity.sp(name: String = packageName, mode: Int = Context.MODE_PRIVATE): SharedPreferences =
    getSharedPreferences(name, mode)

/**
 * 删除指定的SP文件中某一条数据
 */
fun Context.removeKey(key: String, name: String = packageName) {
    sp(name).edit().remove(key).apply()
}

/**
 * 删除指定的整个SP文件
 */
fun Context.clearSp(name: String = packageName) {
    sp(name).edit().clear().apply()
}

/**
 * Set a [T] value in the preferences editor, to be written back once
 * commit() or apply() are called.
 *
 * @param key The name of the preference to modify
 * @param value The new value for the preference
 * @param name Desired preferences file. Default value is the packageName
 */
fun <T> Context.putSpValue(key: String, value: T, name: String = packageName) = sp(name).edit {
    when (value) {
        is Long -> putLong(key, value)
        is String -> putString(key, value)
        is Int -> putInt(key, value)
        is Boolean -> putBoolean(key, value)
        is Float -> putFloat(key, value)
        else -> putString(key, serialize(value))
    }
}

fun <T> Activity.putSpValue(key: String, value: T, name: String = packageName) = sp(name).edit {
    when (value) {
        is Long -> putLong(key, value)
        is String -> putString(key, value)
        is Int -> putInt(key, value)
        is Boolean -> putBoolean(key, value)
        is Float -> putFloat(key, value)
        else -> putString(key, serialize(value))
    }
}

/**
 * Retrieve a [T] value from the preferences
 *
 * @param key The name of the preference to retrieve.
 * @param default Value to return if this preference does not exist.
 * @param name Desired preferences file. Default value is the packageName
 */
fun <T> Context.getSpValue(key: String, default: T, name: String = packageName): T = sp(name).run {
    val result = when (default) {
        is Long -> getLong(key, default)
        is String -> getString(key, default)
        is Int -> getInt(key, default)
        is Boolean -> getBoolean(key, default)
        is Float -> getFloat(key, default)
        else -> deSerialization(getString(key, serialize(default)))
    }
    result as T
}

fun <T> Activity.getSpValue(key: String, default: T, name: String = packageName): T = sp(name).run {
    val result = when (default) {
        is Long -> getLong(key, default)
        is String -> getString(key, default)
        is Int -> getInt(key, default)
        is Boolean -> getBoolean(key, default)
        is Float -> getFloat(key, default)
        else -> deSerialization(getString(key, serialize(default)))
    }
    return result as T
}

/**
 * 获取指定SP文件的所有数据key
 */
fun Context.getSpAll(name: String): MutableMap<String, *> = sp(name).run {
    all
}

private fun <T> serialize(obj: T): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    val objectOutputStream = ObjectOutputStream(
        byteArrayOutputStream
    )
    objectOutputStream.writeObject(obj)
    var serStr = byteArrayOutputStream.toString("ISO-8859-1")
    serStr = URLEncoder.encode(serStr, "UTF-8")
    objectOutputStream.close()
    byteArrayOutputStream.close()
    return serStr
}

private fun <T> deSerialization(str: String?): T {
    val redStr = URLDecoder.decode(str, "UTF-8")
    val byteArrayInputStream = ByteArrayInputStream(
        redStr.toByteArray(charset("ISO-8859-1"))
    )
    val objectInputStream = ObjectInputStream(
        byteArrayInputStream
    )
    val obj = objectInputStream.readObject() as T
    objectInputStream.close()
    byteArrayInputStream.close()
    return obj
}


//----------  以下旧版封装SharedPreferences start  ----------
@Deprecated("旧版封装SharedPreferences，已废弃")
object SpExt {
    /**
     * SharePreference本地存储
     *
     * @param key
     * @param value
     */
    fun putSpValue(key: String?, value: Any?) {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(mToolsContext)
        val editor = sharedPreferences.edit()
        when (value) {
            is String -> editor.putString(key, value as String?)
            is Int -> editor.putInt(key, value)
            is Long -> editor.putLong(key, value)
            is Float -> editor.putFloat(key, value)
            is Boolean -> editor.putBoolean(key, value)
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
    fun getSpValue(key: String?, defaultObject: Any): Any? {
        val type = defaultObject.javaClass.simpleName
        val sp =
            PreferenceManager.getDefaultSharedPreferences(mToolsContext)
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
     * 存储集合
     */
    fun putValueHashMap(key: String?, hashmap: HashMap<String?, Any?>?) {
        val liststr = SceneList2String(hashmap)
        return putSpValue(key, liststr)
    }

    /**
     * 获取集合
     */
    fun getSpValueHashMap(key: String?): HashMap<String?, Any?>? {
        return String2SceneList(getSpValue(key, "") as String?)
    }

    @Throws(IOException::class)
    fun SceneList2String(hashmap: HashMap<String?, Any?>?): String? {
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        val byteArrayOutputStream = ByteArrayOutputStream()
        // 然后将得到的字符数据装载到ObjectOutputStream
        val objectOutputStream = ObjectOutputStream(
            byteArrayOutputStream)
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(hashmap)
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        val SceneListString = String(Base64.encode(
            byteArrayOutputStream.toByteArray(), Base64.DEFAULT))
        // 关闭objectOutputStream
        objectOutputStream.close()
        return SceneListString
    }

    @Throws(StreamCorruptedException::class, IOException::class, ClassNotFoundException::class)
    fun String2SceneList(SceneListString: String?): HashMap<String?, Any?> {
        val mobileBytes = Base64.decode(SceneListString?.toByteArray(), Base64.DEFAULT)
        val byteArrayInputStream = ByteArrayInputStream(
            mobileBytes)
        val objectInputStream = ObjectInputStream(
            byteArrayInputStream)
        val SceneList = objectInputStream
            .readObject() as HashMap<String?, Any?>
        objectInputStream.close()
        return SceneList
    }

    /**
     *  清空本地缓存
     */
    fun clearSpValues() {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(mToolsContext)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
//----------  以下旧版封装SharedPreferences end ----------
}