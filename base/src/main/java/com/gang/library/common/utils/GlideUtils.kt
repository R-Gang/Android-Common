package com.gang.library.common.utils

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.ViewPropertyTransition


/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.app.common.utils
 * @ClassName:      GlideUtils
 * @Description:    媒体管理和图像加载
 * @Author:         haoruigang
 * @CreateDate:     2020/8/3 17:25
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/3 17:25
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
object GlideUtils {

    // Property Animation 属性动画
    var animationObject =
        ViewPropertyTransition.Animator { view: View ->
            view.alpha = 0f
            val fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
            fadeAnim.duration = 233
            fadeAnim.start()
        }

    // Glide 配置
    @SuppressLint("CheckResult")
    private fun getPhotoImageOption(
        context: Context,
        defaultImage: Int,
    ): RequestOptions { //通过RequestOptions扩展功能
        val options = RequestOptions()
        options.placeholder(defaultImage) //预览图片
            .error(defaultImage) // 加载失败时显示的图片
        return options
    }

    // 网络图片
    fun setGlideImg(
        context: Context,
        url: String?,
        defaultImage: Int,
        imageView: ImageView,
    ) {
        Glide.with(context).load(url).apply(getPhotoImageOption(context, defaultImage))
            .into(imageView)
    }

    // 圆角
    fun setGlideRadiusBitmap(
        context: Context,
        url: String?,
        defaultImage: Int,
        imageView: ImageView,
        cornerRadius: Float,
    ) { //  获取Bitmap
        Glide.with(context).asBitmap().load(url)
            .apply(getPhotoImageOption(context, defaultImage))
            .into(object : BitmapImageViewTarget(imageView) {
                override fun setResource(resource: Bitmap?) {
                    super.setResource(resource)
                    val circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.resources, resource)
                    circularBitmapDrawable.cornerRadius =
                        if (cornerRadius == 0f) 20f else cornerRadius //设置圆角弧度
                    imageView.setImageDrawable(circularBitmapDrawable)
                }
            })
    }

    // 获取bitmap图片
    fun getGlideBitmap(
        context: Context,
        url: String?,
        defaultImage: Int,
        imageView: ImageView,
        callBitmap: CallBitmap,
    ) { //  获取Bitmap
        Glide.with(context).asBitmap().load(url)
            .apply(getPhotoImageOption(context, defaultImage))
            .into(object : BitmapImageViewTarget(imageView) {
                override fun setResource(resource: Bitmap?) {
                    super.setResource(resource)
                    callBitmap.bitmapRes(resource)
                }
            })
    }

    interface CallBitmap {
        fun bitmapRes(resource: Bitmap?)
    }

    // 圓角
    fun getRoundedCornerBitmap(bitmap: Bitmap, cornerRadius: Float = 50f): Bitmap? {
        val roundBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(roundBitmap)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        val roundPx = cornerRadius
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return roundBitmap
    }

}