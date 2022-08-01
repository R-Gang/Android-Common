package com.gang.library.common.ext.permissions.easyPermission

import android.R
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.app.AppOpsManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.gang.library.common.ext.permissions.BasePermissionActivity
import com.gang.library.common.ext.permissions.BasePermissionFragment
import java.util.*

/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.app.common.utils.permissions.easyPermission
 * @ClassName:      EasyPermission
 * @Description:    Utility to request and check System permissions for apps targeting Android M (API >= 23).
 * @Author:         haoruigang
 * @CreateDate:     2020/8/3 16:29
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/3 16:29
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class EasyPermission(`object`: Any) {
    interface PermissionCallback : OnRequestPermissionsResultCallback {
        fun onEasyPermissionGranted(
            requestCode: Int,
            vararg perms: String?
        )

        fun onEasyPermissionDenied(requestCode: Int, vararg perms: String?)
    }

    private val `object`: Any = `object`
    private lateinit var mPermissions: Array<String>
    private var mRationale: String? = null
    private var mRequestCode = 0
    @StringRes
    private var mPositiveButtonText = R.string.ok
    @StringRes
    private var mNegativeButtonText = R.string.cancel

    fun permissions(vararg permissions: String): EasyPermission {
        mPermissions = permissions as Array<String>
        return this
    }

    fun rationale(rationale: String?): EasyPermission {
        mRationale = rationale
        return this
    }

    fun addRequestCode(requestCode: Int): EasyPermission {
        mRequestCode = requestCode
        return this
    }

    fun positveButtonText(@StringRes positiveButtonText: Int): EasyPermission {
        mPositiveButtonText = positiveButtonText
        return this
    }

    fun nagativeButtonText(@StringRes negativeButtonText: Int): EasyPermission {
        mNegativeButtonText = negativeButtonText
        return this
    }

    fun request() {
        requestPermissions(
            `object`,
            mRationale,
            mPositiveButtonText,
            mNegativeButtonText,
            mRequestCode,
            *mPermissions
        )
    }

    companion object {
        const val SETTINGS_REQ_CODE = 16061
        fun with(activity: Activity): EasyPermission {
            return EasyPermission(activity)
        }

        fun with(fragment: Fragment): EasyPermission {
            return EasyPermission(fragment)
        }

        /**
         * Check if the calling context has a set of permissions.
         */
        fun hasPermissions(
            context: Context,
            vararg perms: String?
        ): Boolean { // Always return true for SDK < M, let the system deal with the permissions
            if (!Utils.isOverMarshmallow()) {
                return true
            }
            if (perms == null || perms.size == 0) {
                return true
            }
            for (perm in perms) {
                val hasPerm = ContextCompat.checkSelfPermission(
                    context,
                    perm!!
                ) == PackageManager.PERMISSION_GRANTED
                if (!hasPerm) {
                    return false
                }
            }
            return true
        }

        fun hasPermissionAfterSuccess(
            context: Context,
            vararg perms: String?
        ): Boolean {
            if (!Utils.isOverMarshmallow()) {
                return true
            }
            if (perms == null || perms.size == 0) {
                return true
            }
            for (perm in perms) {
                var result = ContextCompat.checkSelfPermission(context, perm!!)
                if (result == PackageManager.PERMISSION_DENIED) {
                    return false
                }
                val op = AppOpsManagerCompat.permissionToOp(perm)
                if (TextUtils.isEmpty(op)) {
                    continue
                }
                result = AppOpsManagerCompat.noteProxyOp(context, op!!, context.packageName)
                if (result != AppOpsManagerCompat.MODE_ALLOWED) {
                    return false
                }
            }
            return true
        }

        /**
         * Request a set of permissions, showing rationale if the system requests it.
         */
        fun requestPermissions(
            `object`: Any, rationale: String?, requestCode: Int,
            vararg perms: String?
        ) {
            requestPermissions(
                `object`,
                rationale,
                R.string.ok,
                R.string.cancel,
                requestCode,
                *perms
            )
        }

        /**
         * Request a set of permissions, showing rationale if the system requests it.
         */
        fun requestPermissions(
            `object`: Any, rationale: String?, @StringRes positiveButton: Int,
            @StringRes negativeButton: Int, requestCode: Int, vararg permissions: String?
        ) {
            checkCallingObjectSuitability(`object`)
            val mCallBack = `object` as PermissionCallback
            if (!Utils.isOverMarshmallow()) {
                mCallBack.onEasyPermissionGranted(requestCode, *permissions)
                return
            }
            val deniedPermissions =
                Utils.findDeniedPermissions(
                    Utils.getActivity(`object`),
                    *permissions as Array<out String>
                )
                    ?: return
            var shouldShowRationale = false
            for (perm in deniedPermissions) {
                shouldShowRationale =
                    shouldShowRationale || Utils.shouldShowRequestPermissionRationale(
                        `object`,
                        perm
                    )
            }
            if (Utils.isEmpty(deniedPermissions)) {
                mCallBack.onEasyPermissionGranted(requestCode, *permissions)
            } else {
                val deniedPermissionArray =
                    deniedPermissions.toTypedArray<String?>()
                if (shouldShowRationale) {
                    val activity = Utils.getActivity(`object`) ?: return
                    AlertDialog.Builder(activity).setMessage(rationale)
                        .setPositiveButton(
                            positiveButton
                        ) { dialog, which ->
                            executePermissionsRequest(
                                `object`,
                                deniedPermissionArray,
                                requestCode
                            )
                        }
                        .setNegativeButton(
                            negativeButton
                        ) { dialog, which ->
                            // act as if the permissions were denied
                            `object`.onEasyPermissionDenied(
                                requestCode,
                                *deniedPermissionArray
                            )
                        }
                        .create()
                        .show()
                } else {
                    executePermissionsRequest(
                        `object`,
                        deniedPermissionArray,
                        requestCode
                    )
                }
            }
        }

        @TargetApi(23)
        private fun executePermissionsRequest(
            `object`: Any,
            perms: Array<String?>,
            requestCode: Int
        ) {
            checkCallingObjectSuitability(`object`)
            if (`object` is Activity) {
                ActivityCompat.requestPermissions(`object`, perms, requestCode)
            } else if (`object` is Fragment) {
                `object`.requestPermissions(perms, requestCode)
            } else if (`object` is android.app.Fragment) {
                `object`.requestPermissions(perms, requestCode)
            }
        }

        /**
         * Handle the result of a permission request.
         */
        fun onRequestPermissionsResult(
            `object`: Any, requestCode: Int, permissions: Array<String?>,
            grantResults: IntArray
        ) {
            checkCallingObjectSuitability(`object`)
            val mCallBack = `object` as PermissionCallback
            val deniedPermissions: MutableList<String?> =
                ArrayList()
            for (i in grantResults.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permissions[i])
                }
            }
            if (Utils.isEmpty(deniedPermissions)) {
                mCallBack.onEasyPermissionGranted(requestCode, *permissions)
            } else {
                mCallBack.onEasyPermissionDenied(
                    requestCode,
                    *deniedPermissions.toTypedArray()
                )
            }
        }

        /**
         * 在OnActivityResult中接收判断是否已经授权
         * 使用[EasyPermission.hasPermissions]进行判断
         *
         *
         * If user denied permissions with the flag NEVER ASK AGAIN, open a dialog explaining the
         * permissions rationale again and directing the user to the app settings. After the user
         * returned to the app, [Activity.onActivityResult] or
         * [Fragment.onActivityResult] or
         * [android.app.Fragment.onActivityResult] will be called with
         * {@value #SETTINGS_REQ_CODE} as requestCode
         *
         *
         *
         *
         * NOTE: use of this method is optional, should be called from
         * [PermissionCallback.onEasyPermissionDenied]
         *
         * @return `true` if user denied at least one permission with the flag NEVER ASK AGAIN.
         */
        fun checkDeniedPermissionsNeverAskAgain(
            `object`: BasePermissionFragment,
            rationale: String,
            @StringRes positiveButton: Int,
            @StringRes negativeButton: Int,
            negativeButtonOnClickListener: DialogInterface.OnClickListener?,
            deniedPerms: Array<out String?>
        ): Boolean {
            var shouldShowRationale: Boolean
            for (perm in deniedPerms) {
                shouldShowRationale = Utils.shouldShowRequestPermissionRationale(`object`, perm)
                if (!shouldShowRationale) {
                    val activity = Utils.getActivity(`object`) ?: return true
                    AlertDialog.Builder(activity).setMessage(rationale)
                        .setPositiveButton(
                            positiveButton
                        ) { dialog, which ->
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts(
                                "package",
                                activity.packageName,
                                null
                            )
                            intent.data = uri
                            startAppSettingsScreen(
                                `object`,
                                intent
                            )
                        }
                        .setNegativeButton(negativeButton, negativeButtonOnClickListener)
                        .create()
                        .show()
                    return true
                }
            }
            return false
        }

        fun checkDeniedPermissionsNeverAskAgain(
            `object`: BasePermissionActivity,
            rationale: String,
            @StringRes positiveButton: Int,
            @StringRes negativeButton: Int,
            negativeButtonOnClickListener: DialogInterface.OnClickListener?,
            deniedPerms: Array<out String?>
        ): Boolean {
            var shouldShowRationale: Boolean
            for (perm in deniedPerms) {
                shouldShowRationale = Utils.shouldShowRequestPermissionRationale(`object`, perm)
                if (!shouldShowRationale) {
                    val activity = Utils.getActivity(`object`) ?: return true
                    AlertDialog.Builder(activity).setMessage(rationale)
                        .setPositiveButton(
                            positiveButton
                        ) { dialog, which ->
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts(
                                "package",
                                activity.packageName,
                                null
                            )
                            intent.data = uri
                            startAppSettingsScreen(
                                `object`,
                                intent
                            )
                        }
                        .setNegativeButton(negativeButton, negativeButtonOnClickListener)
                        .create()
                        .show()
                    return true
                }
            }
            return false
        }

        @TargetApi(11)
        private fun startAppSettingsScreen(`object`: Any?, intent: Intent) {
            if (`object` is Activity) {
                `object`.startActivityForResult(
                    intent,
                    SETTINGS_REQ_CODE
                )
            } else if (`object` is Fragment) {
                `object`.startActivityForResult(
                    intent,
                    SETTINGS_REQ_CODE
                )
            } else if (`object` is android.app.Fragment) {
                `object`.startActivityForResult(
                    intent,
                    SETTINGS_REQ_CODE
                )
            }
        }

        private fun checkCallingObjectSuitability(`object`: Any) {
            require(
                `object` is Fragment
                        || `object` is Activity
                        || `object` is android.app.Fragment
            ) { "Caller must be an Activity or a Fragment." }
            require(`object` is PermissionCallback) { "Caller must implement PermissionCallback." }
        }
    }

}
