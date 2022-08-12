package com.gang.app.common.utils

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import java.util.*

/**
 * @CreateDate:     2022/8/2 13:56
 * @ClassName:      BitmapExt
 * @Description:    类作用描述
 * @Author:         haoruigang
 */
/**
 * 用字符串生成二维码
 *
 * @param str
 * @return
 * @throws WriterException
 */
@Throws(WriterException::class)
fun create2DCode(str: String?): Bitmap {
    val hints: Hashtable<EncodeHintType, String> =
        Hashtable<EncodeHintType, String>()
    hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
    // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
    val matrix: BitMatrix =
        MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 480, 480, hints)
    val width: Int = matrix.getWidth()
    val height: Int = matrix.getHeight()
    // 二维矩阵转为一维像素数组,也就是一直横着排了
    val pixels = IntArray(width * height)
    for (y in 0 until height) {
        for (x in 0 until width) {
            if (matrix.get(x, y)) {
                pixels[y * width + x] = -0x1000000
            }
        }
    }
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    // 通过像素数组生成bitmap,具体参考api
    bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    return bitmap
}