package com.gang.library.ui.interfaces

import android.view.View
import androidx.annotation.UiThread

/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.app.ui.interfaces
 * @ClassName:      Setter
 * @Description:     java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     2020/8/4 18:30
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/4 18:30
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
/** A setter that can apply a value to a list of views.  */
interface Setter<T : View?, V> {
    /** Set the `value` on the `view` which is at `index` in the list.  */
    @UiThread
    operator fun set(view: T, value: V?, index: Int)
}