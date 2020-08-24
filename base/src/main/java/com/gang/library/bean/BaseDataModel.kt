package com.gang.library.bean

import java.util.*

/**
 * Created by haoruigang on 2017/11/7.
 */
class BaseDataModel<T> : BaseBeanModel() {

    var data: ArrayList<T>? = null
}