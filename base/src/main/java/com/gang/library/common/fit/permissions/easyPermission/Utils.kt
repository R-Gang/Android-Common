package com.gang.library.common.fit.permissions.easyPermission

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import java.util.*

/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.app.common.utils.permissions.easyPermission
 * @ClassName:      Utils
 * @Description:    java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     2020/8/3 16:32
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/3 16:32
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
internal object Utils {
    fun isOverMarshmallow(): Boolean {
        return Build.VERSION.SDK_INT >= 23
    }

    fun findDeniedPermissions(
        activity: Activity?,
        vararg permission: String
    ): List<String>? {
        if (activity == null) {
            return null
        }
        val denyPermissions: MutableList<String> =
            ArrayList()
        if (Build.VERSION.SDK_INT >= 23) {
            for (value in permission) {
                if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                    denyPermissions.add(value)
                }
            }
        }
        return denyPermissions
    }

    fun shouldShowRequestPermissionRationale(
        `object`: Any?,
        perm: String?
    ): Boolean {
        return when (`object`) {
            is Activity -> {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    (`object` as Activity?)!!,
                    perm!!
                )
            }
            is Fragment -> {
                `object`.shouldShowRequestPermissionRationale(
                    perm!!
                )
            }
            else -> {
                (`object` is android.app.Fragment
                        && Build.VERSION.SDK_INT >= 23 && `object`.shouldShowRequestPermissionRationale(
                    perm!!
                ))
            }
        }
    }

    fun getActivity(`object`: Any?): Activity? {
        return when (`object`) {
            is Activity -> {
                `object`
            }
            is Fragment -> {
                `object`.activity
            }
            is android.app.Fragment -> {
                `object`.activity
            }
            else -> {
                null
            }
        }
    }

    fun isEmpty(list: List<*>?): Boolean {
        return list == null || list.isEmpty()
    }
}