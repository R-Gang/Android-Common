package com.gang.library.common.view.ext

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.gang.library.base.BaseVBActivity
import com.gang.library.base.BaseVBFragment
import com.gang.library.base.BaseVMActivity
import com.gang.library.base.BaseVMFragment
import com.gang.tools.kotlin.utils.LogUtils
import java.lang.reflect.ParameterizedType

/**
 * ViewBinding 各种初始化情况的扩展
 *
 * Created on 2021/7/1.
 *
 * @author o.s
 */

/**
 * 初始化 [BaseVBActivity] 中的 [ViewBinding]
 * 适用于范型参数为 [ViewBinding] 的 [ComponentActivity]
 */
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> BaseVBActivity<VB>.initViewBinding(): VB? {
    var viewBinding: VB? = null
    val type = javaClass.genericSuperclass
    if (type is ParameterizedType) {
        val clazz = type.actualTypeArguments[0] as? Class<VB>
        val method = clazz?.getMethod("inflate", LayoutInflater::class.java)
        viewBinding = (method?.invoke(null, layoutInflater) as? VB)?.apply {
            setContentView(root)
            if (this is ViewDataBinding) {
                this.lifecycleOwner = this@initViewBinding
            }
        }
    }
    return viewBinding
}

/**
 * 初始化 [BaseVMActivity] 中的 [ViewBinding]
 * 适用于第二个范型参数为 [ViewBinding] 的 [ComponentActivity]
 */
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> BaseVMActivity<*, VB>.initViewBinding(): VB? {
    var viewBinding: VB? = null
    val type = javaClass.genericSuperclass
    if (type is ParameterizedType) {
        val clazz = type.actualTypeArguments[1] as? Class<VB>
        val method = clazz?.getMethod("inflate", LayoutInflater::class.java)
        viewBinding = (method?.invoke(null, layoutInflater) as? VB)?.apply {
            setContentView(root)
            if (this is ViewDataBinding) {
                this.lifecycleOwner = this@initViewBinding
            }
        }
    }
    return viewBinding
}

/**
 * 初始化 [BaseVBFragment] 中的 [ViewBinding]
 * 适用于范型参数为 [ViewBinding] 的 [Fragment]
 */
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> BaseVBFragment<VB>.initViewBinding(
    container: ViewGroup? = null,
    attachToRoot: Boolean = false,
): VB? {
    var viewBinding: VB? = null
    val type = javaClass.genericSuperclass
    if (type is ParameterizedType) {
        val clazz = type.actualTypeArguments[0] as? Class<VB>
        val method = clazz?.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        try {
            viewBinding =
                (method?.invoke(null, layoutInflater, container, attachToRoot) as? VB)?.apply {
                    if (this is ViewDataBinding) {
                        this.lifecycleOwner = this@initViewBinding
                    }
                }
        } catch (e: Throwable) {
            e.printStackTrace()
            LogUtils.e("clazz=$clazz", "e=${e.message}")
            viewBinding = null
        }
    }
    return viewBinding
}

/**
 * 初始化 [BaseVMFragment] 中的 [ViewBinding]
 * 适用于第二个范型参数为 [ViewBinding] 的 [Fragment]
 */
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> BaseVMFragment<*, VB>.initViewBinding(
    container: ViewGroup? = null,
    attachToRoot: Boolean = false,
): VB? {
    var viewBinding: VB? = null
    val type = javaClass.genericSuperclass
    if (type is ParameterizedType) {
        val clazz = type.actualTypeArguments[1] as? Class<VB>
        val method = clazz?.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        try {
            viewBinding =
                (method?.invoke(null, layoutInflater, container, attachToRoot) as? VB)?.apply {
                    if (this is ViewDataBinding) {
                        this.lifecycleOwner = this@initViewBinding
                    }
                }
        } catch (e: Throwable) {
            e.printStackTrace()
            LogUtils.e("clazz=$clazz", "e=${e.message}")
            viewBinding = null
        }
    }
    return viewBinding
}