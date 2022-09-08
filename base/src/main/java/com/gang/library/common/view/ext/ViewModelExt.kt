package com.gang.library.common.view.ext

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.gang.library.base.BaseVMActivity
import com.gang.library.base.BaseVMFragment
import com.gang.library.base.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * ViewModel初始化扩展类
 * */

private fun <VM : BaseViewModel> createViewModel(
    viewModelClass: Class<VM>,
    storeProducer: () -> ViewModelStore,
    factoryProducer: () -> ViewModelProvider.Factory,
): VM {
    return ViewModelProvider(storeProducer(), factoryProducer())[viewModelClass]
}

/**
 * 初始化 [BaseVMActivity] 中的 [ViewModel]
 * 适用于第一个范型参数为 [ViewModel] 的 [ComponentActivity]
 */
@Suppress("UNCHECKED_CAST")
fun <VM : BaseViewModel> BaseVMActivity<VM, *>.initViewModel(
    factoryProducer: (() -> ViewModelProvider.Factory)? = null,
): VM? {
    var viewModel: VM? = null
    val type = javaClass.genericSuperclass
    if (type is ParameterizedType) {
        val clazz = type.actualTypeArguments[0] as? Class<VM>
        clazz?.let {
            val factoryPromise = factoryProducer ?: {
                defaultViewModelProviderFactory
            }
            viewModel = createViewModel(it, { viewModelStore }, factoryPromise)
        }
    }
    return viewModel
}

/**
 * 初始化 [BaseVMFragment] 中的 [ViewModel]
 * 适用于第一个范型参数为 [ViewModel] 的 [ComponentActivity]
 */
@Suppress("UNCHECKED_CAST")
fun <VM : BaseViewModel> BaseVMFragment<VM, *>.initViewModel(
    ownerProducer: () -> ViewModelStoreOwner = { this },
    factoryProducer: (() -> ViewModelProvider.Factory)? = null,
): VM? {
    var viewModel: VM? = null
    val type = javaClass.genericSuperclass
    if (type is ParameterizedType) {
        val clazz = type.actualTypeArguments[0] as? Class<VM>
        clazz?.let {
            val factoryPromise = factoryProducer ?: {
                defaultViewModelProviderFactory
            }
            viewModel = createViewModel(it, { ownerProducer().viewModelStore }, factoryPromise)
        }
    }
    return viewModel
}