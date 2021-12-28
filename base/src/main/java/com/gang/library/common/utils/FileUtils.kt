package com.gang.library.common.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.icu.math.BigDecimal
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.annotation.NonNull
import com.apkfuns.logutils.LogUtils
import com.gang.library.BaseApplication
import com.gang.library.common.user.Config
import java.io.*
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @ProjectName:    Android-Common
 * @Package:        com.gang.library.common.utils
 * @ClassName:      FileUtils
 * @Description:     java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     2020/8/25 13:46
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/25 13:46
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
object FileUtils {

    private const val DEFAULT_CACHE_DIR = "picture_cache"
    const val POSTFIX = ".JPEG"
    const val POST_VIDEO = ".mp4"
    const val APP_NAME = "PictureSelector"
    const val CAMERA_PATH = "/$APP_NAME/CameraImage/"
    const val CROP_PATH = "/$APP_NAME/CropImage/"

    /**
     * @param type
     * @param outputCameraPath
     * @param format
     * @return
     */
    fun createCameraFile(
        type: Int,
        outputCameraPath: String,
        format: String,
    ): File? {
        val path =
            if (!TextUtils.isEmpty(outputCameraPath)) outputCameraPath else CAMERA_PATH
        return createMediaFile(path, type, format)
    }

    /**
     * @param type
     * @param format
     * @return
     */
    fun createCropFile(
        type: Int,
        format: String,
    ): File? {
        return createMediaFile(
            CROP_PATH,
            type,
            format
        )
    }

    private fun createMediaFile(
        parentPath: String,
        type: Int,
        format: String,
    ): File? {
        val state = Environment.getExternalStorageState()
        val rootDir =
            if (state == Environment.MEDIA_MOUNTED) Environment.getExternalStorageDirectory() else BaseApplication.appContext.cacheDir
        val folderDir = File(rootDir.absolutePath + parentPath)
        if (!folderDir.exists() && folderDir.mkdirs()) {
        }
        val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
                .format(Date())
        val fileName = APP_NAME + "_" + timeStamp + ""
        var tmpFile: File? = null
        val suffixType: String
        when (type) {
            Config.TYPE_IMAGE -> {
                suffixType = if (TextUtils.isEmpty(format)) POSTFIX else format
                tmpFile = File(folderDir, fileName + suffixType)
            }
            Config.TYPE_VIDEO -> tmpFile =
                File(folderDir, fileName + POST_VIDEO)
        }
        return tmpFile
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
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.toString()
    }

    /**
     * 创建文件夹
     *
     * @param filename
     * @return
     */
    fun createDir(
        filename: String,
        directory_path: String,
    ): String {
        val state = Environment.getExternalStorageState()
        val rootDir =
            if (state == Environment.MEDIA_MOUNTED) Environment.getExternalStorageDirectory() else BaseApplication.appContext.cacheDir
        var path: File? = null
        path = if (!TextUtils.isEmpty(directory_path)) { // 自定义保存目录
            File(rootDir.absolutePath + directory_path)
        } else {
            File(rootDir.absolutePath + "/PictureSelector")
        }
        if (!path.exists()) // 若不存在，创建目录，可以在应用启动的时候创建
        {
            path.mkdirs()
        }
        return "$path/$filename"
    }

    /**
     * TAG for log messages.
     */
    const val TAG = "PictureFileUtils"

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     * @author paulburke
     */
    fun getContentColumn(
        uri: Uri?, column: String, selection: String?,
        selectionArgs: Array<String>?,
    ): String? {
        var cursor: Cursor? = null
        val projection = arrayOf(
            column
        )
        try {
            cursor = BaseApplication.appContext.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } catch (ex: IllegalArgumentException) {
            Log.i(
                TAG,
                String.format(
                    Locale.getDefault(),
                    "getDataColumn: _data - [%s]",
                    ex.message
                )
            )
        } finally {
            cursor?.close()
        }
        return null
    }


    /**
     * 根据Uri返回文件绝对路径-(默认通过_data查询-查询手机存储的图片）
     * 兼容了file:///开头的 和 content://开头的情况
     */
    fun getFilePathContents(
        context: Context,
        uri: Uri?,
        mediaStore: String = MediaStore.Images.ImageColumns.DATA,
    ): String? {
        if (null == uri) return null
        val scheme = uri.scheme
        var data: String? = null
        if (scheme == null) {
            data = uri.path
        } else if (ContentResolver.SCHEME_FILE.equals(scheme, ignoreCase = true)) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme, ignoreCase = true)) {
            val cursor =
                context.contentResolver.query(
                    uri,
                    arrayOf(mediaStore),
                    null,
                    null,
                    null
                )
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    //根据_data查找
                    val index = cursor.getColumnIndex(mediaStore)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
            // getContentColumn(uri, mediaStore, null, null)
        }
        return data
    }


    fun getPhotoCacheDir(file: File): File {
        val cacheDir = BaseApplication.appContext.cacheDir
        val file_name = file.name
        if (cacheDir != null) {
            val mCacheDir =
                File(cacheDir, DEFAULT_CACHE_DIR)
            return if (!mCacheDir.mkdirs() && (!mCacheDir.exists() || !mCacheDir.isDirectory)) {
                file
            } else {
                var fileName = ""
                fileName = if (file_name.endsWith(".webp")) {
                    System.currentTimeMillis().toString() + ".webp"
                } else {
                    System.currentTimeMillis().toString() + ".png"
                }
                File(mCacheDir, fileName)
            }
        }
        if (Log.isLoggable(TAG, Log.ERROR)) {
            Log.e(TAG, "default disk cache dir is null")
        }
        return file
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     * @author paulburke
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     * @author paulburke
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * @author paulburke
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param uri
     */
    @SuppressLint("ObsoleteSdkInt")
    fun getPath(uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(BaseApplication.appContext, uri)) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getContentColumn(contentUri, "", null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                return getContentColumn(
                    contentUri,
                    "",
                    selection,
                    selectionArgs
                )
            }
        } else if ("content".equals(
                uri.scheme,
                ignoreCase = true
            )
        ) { // Return the remote address
            return if (isGooglePhotosUri(uri)) {
                uri.lastPathSegment
            } else getContentColumn(uri, "", null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    /**
     * Copies one file into the other with the given paths.
     * In the event that the paths are the same, trying to copy one file to the other
     * will cause both files to become null.
     * Simply skipping this step if the paths are identical.
     */
    @Throws(IOException::class)
    fun copyFile(@NonNull pathFrom: String, @NonNull pathTo: String?) {
        if (pathFrom.equals(pathTo, ignoreCase = true)) {
            return
        }
        var outputChannel: FileChannel? = null
        var inputChannel: FileChannel? = null
        try {
            inputChannel = FileInputStream(File(pathFrom)).channel
            outputChannel = FileOutputStream(File(pathTo)).channel
            inputChannel.transferTo(0, inputChannel.size(), outputChannel)
            inputChannel.close()
        } finally {
            inputChannel?.close()
            outputChannel?.close()
        }
    }

    /**
     * Copies one file into the other with the given paths.
     * In the event that the paths are the same, trying to copy one file to the other
     * will cause both files to become null.
     * Simply skipping this step if the paths are identical.
     */
    @Throws(IOException::class)
    fun copyAudioFile(@NonNull pathFrom: String, @NonNull pathTo: String?): Boolean {
        if (pathFrom.equals(pathTo, ignoreCase = true)) {
            return false
        }
        var outputChannel: FileChannel? = null
        var inputChannel: FileChannel? = null
        try {
            inputChannel = FileInputStream(File(pathFrom)).channel
            outputChannel = FileOutputStream(File(pathTo)).channel
            inputChannel.transferTo(0, inputChannel.size(), outputChannel)
            inputChannel.close()
        } finally {
            inputChannel?.close()
            outputChannel?.close()
            return deleteFile(pathFrom)
        }
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    fun readPictureDegree(path: String?): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return degree
    }

    /*
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    fun rotaingImageView(angle: Int, bitmap: Bitmap): Bitmap { //旋转图片 动作
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        println("angle2=$angle")
        // 创建新的图片
        return Bitmap.createBitmap(
            bitmap, 0, 0,
            bitmap.width, bitmap.height, matrix, true
        )
    }

    /**
     * 保存图片到本地
     */
    fun saveBitmap(name: String, bm: Bitmap): Boolean {
        val fileName = createNewFilePath(name)
        val f = File(fileName, name)
        return saveBitmapFile(bm, f)
    }

    fun saveBitmapFile(bitmap: Bitmap, file: File?): Boolean {
        var isSave = false
        try {
            val bos =
                BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            bos.flush()
            bos.close()
            isSave = true
        } catch (e: IOException) {
            LogUtils.d(e.message)
            e.printStackTrace()
        }
        return isSave
    }

    /**
     * 将本地图片资源转成bitmap
     */
    fun getBitmapByLocalRes(path: String): Bitmap? {
        val fis = FileInputStream(path)
        val bitmap = BitmapFactory.decodeStream(fis)
        return if (bitmap == null) null
        else return bitmap
    }

    /**
     * image is Damage
     *
     * @param path
     * @return
     */
    fun isDamage(path: String?): Int {
        var options: BitmapFactory.Options? = null
        if (options == null) options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options) //filePath代表图片路径
        return if (options.mCancel || options.outWidth == -1 || options.outHeight == -1
        ) { //表示图片已损毁
            -1
        } else 0
    }

    /**
     * 获取某目录下所有文件路径
     *
     * @param dir
     */
    fun getDirFiles(dir: String?): List<String> {
        val scanner5Directory = File(dir)
        val list: MutableList<String> =
            ArrayList()
        if (scanner5Directory.isDirectory) {
            for (file in scanner5Directory.listFiles()) {
                val path = file.absolutePath
                if (path.endsWith(".jpg") || path.endsWith(".jpeg")
                    || path.endsWith(".png") || path.endsWith(".gif")
                    || path.endsWith(".webp")
                ) {
                    list.add(path)
                }
            }
        }
        return list
    }

    val dCIMCameraPath: String
        get() {
            val absolutePath: String
            absolutePath = try {
                "%" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath + "/Camera"
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
            return absolutePath
        }

    /**
     * cacheDir
     *
     */
    fun deleteCacheDirFile() {
        val cutDir = BaseApplication.appContext.cacheDir
        val compressDir =
            File(BaseApplication.appContext.cacheDir.toString() + "/picture_cache")
        val lubanDir =
            File(BaseApplication.appContext.cacheDir.toString() + "/luban_disk_cache")

        deleteDirFile(cutDir)
        deleteDirFile(compressDir)
        deleteDirFile(lubanDir)
    }

    /**
     * externalCacheDir
     */
    fun deleteExternalCacheDirFile() {

        val cutDir = BaseApplication.appContext.externalCacheDir
        val compressDir =
            File(BaseApplication.appContext.externalCacheDir.toString() + "/picture_cache")
        val lubanDir =
            File(BaseApplication.appContext.externalCacheDir.toString() + "/luban_disk_cache")

        deleteDirFile(cutDir)
        deleteDirFile(compressDir)
        deleteDirFile(lubanDir)
    }

    /**
     * 删除指定目录下所以文件
     */
    fun deleteDirFile(dirFile: File?) {
        if (dirFile != null) {
            val files = dirFile.listFiles()
            if (files != null) {
                for (file in files) {
                    if (file.isFile) {
                        file.delete()
                    }
                }
            }
        }
    }

    /**
     * delete file 删除文件
     *
     * @param path
     */
    fun deleteFile(path: String?): Boolean {
        try {
            if (!TextUtils.isEmpty(path)) {
                val file = File(path)
                if (file.exists()) {
                    return file.delete()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return false
    }

    /**
     * @return
     */
    fun getDiskCacheDir(): String? {
        var cachePath: String? = null
        cachePath =
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
                BaseApplication.appContext.externalCacheDir!!.path
            } else {
                BaseApplication.appContext.cacheDir.path
            }
        return cachePath
    }

    /**
     * 判断文件是否存在
     * @param strFile
     * @return
     */
    fun fileIsExists(strFile: String?): Boolean {
        try {
            val f = File(strFile)
            if (!f.exists()) {
                return false
            }
        } catch (e: Exception) {
            return false
        }
        return true
    }


    // 判断SD卡是否存在
    fun isExistSDCard(): Boolean {
        return Environment.getExternalStorageState() ==
                Environment.MEDIA_MOUNTED
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

    // 遍历文件夹下所有文件
    private fun loopRead(dir: File, sb: StringBuffer) {
        val files = dir.listFiles()
        if (files != null) for (file in files) {
            if (file.isDirectory) {
                loopRead(file, sb)
            } else {
                if (file.length() != 0L) {
                    sb.append(readFileToString(file))
                }
            }
        }
    }

    //读取文件里面的内容
    private fun readFileToString(file: File): String? {
        var br: BufferedReader? = null
        val sb = StringBuilder()
        try {
            br = BufferedReader(FileReader(file))
            var line: String? = null
            while (br.readLine().also { line = it } != null) {
                val s = line!!.trim { it <= ' ' }
                if (s.length == 0) {
                    continue
                }
                if (s.startsWith("/") || s.startsWith("*")) {
                    continue
                }
                sb.append(line).append("\n")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            try {
                br?.close()
            } catch (e2: java.lang.Exception) {
                e2.printStackTrace()
            }
        }
        return sb.toString()
    }

    //将读取的路径以及相应的内容写入指定的文件
    private fun write(str: String, writer: Writer?) {
        try {
            writer!!.write(str)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            try {
                writer?.close()
            } catch (e2: java.lang.Exception) {
                e2.printStackTrace()
            }
        }
    }

    /**
     * 文件转成byte数组
     */
    @JvmStatic
    fun file2Byte(file: File) = file.readBytes()

    /**
     *  申请软著代码复制删除注释和空行
     *
     *  PROJECT_URL : 扫描的源代码
     *  OUT_PATH : 文档输出路径
     */
    fun codeSource(PROJECT_URL: String, OUT_PATH: String) {
        //文件读取路径
        val dir = File(PROJECT_URL)
        //文件输出路径
        val target = File(OUT_PATH)
        val bw = BufferedWriter(FileWriter(target))

        val sb = StringBuffer()
        loopRead(dir, sb)
        write(sb.toString(), bw)
    }


    /**
     *
     * 获取当前缓存
     *
     * @return
     *
     * @throws Exception
     */
    @Throws(java.lang.Exception::class)
    fun getTotalCacheSize(): String {
        var cacheSize: Long = getFolderSize(BaseApplication.appContext.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            cacheSize += BaseApplication.appContext.externalCacheDir?.let { getFolderSize(it) }!!
        }
        return getFormatSize(cacheSize)
    }

    /**
     *
     * 清空缓存
     *
     */
    fun clearAllCache() {
        deleteDir(BaseApplication.appContext.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            deleteDir(BaseApplication.appContext.externalCacheDir)
        }
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir!!.delete()
    }

    fun getFolderSize(file: File): Long {
        var size = 0L;
        try {
            var fileList = file.listFiles()
            fileList.forEachIndexed { index, file ->
                // 如果下面还有文件
                if (file.isDirectory) {
                    size += getFolderSize(fileList[index]);
                } else {
                    size += fileList[index].length();
                }
            }
        } catch (e: Exception) {
            e.printStackTrace();
        }
        return size
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    fun getFormatSize(size: Long): String {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return size.toString() + "Byte"
        }
        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(kiloByte.toString())
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "KB"
        }
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(megaByte.toString())
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "MB"
        }
        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(gigaByte.toString())
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "TB"
    }

}