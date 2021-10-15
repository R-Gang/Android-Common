package com.gang.library.common.utils

import android.annotation.TargetApi
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.apkfuns.logutils.LogUtils
import com.gang.library.BaseApplication
import com.gang.library.common.user.Config
import com.gang.library.common.utils.FileUtils.getDataColumn
import com.gang.library.common.utils.FileUtils.isDownloadsDocument
import com.gang.library.common.utils.FileUtils.isExternalStorageDocument
import com.gang.library.common.utils.FileUtils.isGooglePhotosUri
import com.gang.library.common.utils.FileUtils.isMediaDocument
import java.io.*

/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.app.common.utils
 * @ClassName:      ResourcesUtils
 * @Description:    获取资源
 * @Author:         haoruigang
 * @CreateDate:     2020/8/3 17:28
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/3 17:28
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
/**
 * 获取字符串
 *
 * @param id
 * @param obj
 * @return
 */
fun getString(@StringRes id: Int, obj: Array<Any?>): String {
    val string: String = BaseApplication.appContext.resources.getString(id)
    return if (obj.isNotEmpty()) String.format(string, *obj) else string
}

fun getStrings(@ArrayRes id: Int): Array<String> {
    return BaseApplication.appContext.resources.getStringArray(id)
}

/**
 * 文字中添加图片
 *
 * @param textView
 * @param imgResId
 * @param index
 * @param padding
 */
fun setTvaddDrawable(
    textView: TextView, @DrawableRes imgResId: Int,
    index: Int,
    padding: Int,
) {
    if (imgResId == -1) {
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
    } else {
        textView.setCompoundDrawablesWithIntrinsicBounds(
            if (index == 1) imgResId else 0,
            if (index == 2) imgResId else 0,
            if (index == 3) imgResId else 0,
            if (index == 4) imgResId else 0
        )
        textView.compoundDrawablePadding = padding
    }
}

/**
 * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
 *
 * @param context
 * @param imageUri
 */
@TargetApi(19)
fun getImageAbsolutePath(
    context: Context?,
    imageUri: Uri?,
): String? {
    if (context == null || imageUri == null) return null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(
            context,
            imageUri
        )
    ) {
        if (isExternalStorageDocument(imageUri)) {
            val docId = DocumentsContract.getDocumentId(imageUri)
            val split = docId.split(":").toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }
        } else if (isDownloadsDocument(imageUri)) {
            val id = DocumentsContract.getDocumentId(imageUri)
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),
                java.lang.Long.valueOf(id)
            )
            return getDataColumn(contentUri, null, null)
        } else if (isMediaDocument(imageUri)) {
            val docId = DocumentsContract.getDocumentId(imageUri)
            val split = docId.split(":").toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            when (type) {
                "image" -> {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
                "video" -> {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }
                "audio" -> {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
            }
            val selection = MediaStore.Images.Media._ID + "=?"
            val selectionArgs =
                arrayOf(split[1])
            return getDataColumn(contentUri, selection, selectionArgs)
        }
    } // MediaStore (and general)
    else if ("content".equals(
            imageUri.scheme,
            ignoreCase = true
        )
    ) { // Return the remote address
        return if (isGooglePhotosUri(imageUri)) imageUri.lastPathSegment else getDataColumn(
            imageUri,
            null,
            null
        )
    } else if ("file".equals(imageUri.scheme, ignoreCase = true)) {
        return imageUri.path
    }
    return null
}

/**
 * 读取assets中的txt文件
 *
 * @return
 */
fun readAssetsText(context: Context, fileName: String?): String {
    val sb = StringBuffer("")
    try {
        val inputStream = context.assets.open(fileName!!)
        var inputStreamReader: InputStreamReader? = null
        try {
            inputStreamReader = InputStreamReader(inputStream, "UTF-8")
        } catch (e1: UnsupportedEncodingException) {
            LogUtils.tag("readAssetsText=$e1")
        }
        val reader = BufferedReader(inputStreamReader)
        var line: String?
        try {
            while (reader.readLine().also { line = it } != null) {
                sb.append(line)
                sb.append("\n")
            }
        } catch (e: IOException) {
            LogUtils.tag("readAssetsText=$e")
        }
    } catch (e1: IOException) {
    }
    return sb.toString()
}

/**
 * 创建文件夹
 *
 * @param fileName 文件夹名字
 * @return 文件夹路径
 */
fun createNewFilePath(fileName: String): String {
    val file =
        File(Environment.getExternalStorageDirectory().toString() + File.separator + fileName)
    return file.toString()
}

/**
 * @param fileName
 * @return
 */
fun createNewFile(fileName: String): String {
    val file =
        File(Environment.getExternalStorageDirectory().toString() + File.separator + fileName)
    if (!file.exists()) {
        file.mkdirs()
    }
    return file.toString()
}

private const val MAIN_SOFT_FOLDER_NAME = "MAIN_SOFT0"
private const val CACHE_FOLDER_NAME = "CACHE0"
fun getImageCachePath(): String //给图片一个存储路径
{
    if (!isExistSDCard()) {
        return ""
    }
    val sdRoot =
        Environment.getExternalStorageDirectory().absolutePath
    val result = sdRoot +
            "/" + MAIN_SOFT_FOLDER_NAME + "/" + CACHE_FOLDER_NAME
    return if (File(result).exists() && File(result).isDirectory) {
        result
    } else {
        sdRoot
    }
}

// 判断SD卡是否存在
fun isExistSDCard(): Boolean {
    return Environment.getExternalStorageState() ==
            Environment.MEDIA_MOUNTED
}

/**
 * 根据Uri返回文件绝对路径
 * 兼容了file:///开头的 和 content://开头的情况
 */
fun getRealFilePathFromUri(
    context: Context,
    uri: Uri?,
): String? {
    if (null == uri) return null
    val scheme = uri.scheme
    var data: String? = null
    if (scheme == null) {
        data = uri.path
    } else if (ContentResolver.SCHEME_FILE.equals(scheme, ignoreCase = true)) {
        data = uri.path
    } else if (ContentResolver.SCHEME_CONTENT.equals(scheme, ignoreCase = true)) {
        val cursor = context.contentResolver.query(
            uri, arrayOf(
                MediaStore.Images.ImageColumns.DATA
            ), null, null, null
        )
        if (null != cursor) {
            if (cursor.moveToFirst()) { //根据_data查找
                val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                if (index > -1) {
                    data = cursor.getString(index)
                }
            }
            cursor.close()
        }
    }
    return data
}

//全局字体
val typefaceAll: Typeface by lazy { Typeface.createFromAsset(BaseApplication.appContext.assets, Config.typefaceAll) }