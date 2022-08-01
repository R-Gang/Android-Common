package com.gang.library.common.user

import android.app.Activity
import android.view.View
import com.gang.library.bean.UserEntity
import com.gang.library.common.store.getSpValue
import com.gang.library.ui.interfaces.Setter
import com.gang.library.common.store.putSpValue
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*

/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.app.common.user
 * @ClassName:      UserManager
 * @Description:    java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     2020/8/3 16:18
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/3 16:18
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
enum class UserManager {

    INSTANCE;

    var userData = UserEntity()

    fun save(user: UserEntity) {
        userData = user
        putSpValue("user_id", userData.user_id)
        putSpValue("user_token", userData.user_token)
    }

    fun isLogin(): Boolean {
        return (userData.user_id.isNotEmpty() && userData.user_token.isNotEmpty())
    }

    fun isLogin1(): Boolean {
        return (getSpValue("user_id", "").toString().isNotEmpty()
                && getSpValue("user_token", "").toString().isNotEmpty())
    }

    fun <T : View?, V> apply(
        list: List<T>,
        setter: Setter<in T, V>, value: V,
    ) {
        var i = 0
        val count = list.size
        while (i < count) {
            setter[list[i], value] = i
            i++
        }
    }

    init {
        EventBus.getDefault().register(this)
    }

    @Subscribe
    fun onEvent(objects: Objects) {
        objects.apply {

        }
    }

    private lateinit var mActivity: Activity

}