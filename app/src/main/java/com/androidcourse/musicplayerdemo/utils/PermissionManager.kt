package com.androidcourse.musicplayerdemo.utils

import android.app.Activity
import android.content.pm.PackageManager

// 管理权限的一个工具类
class PermissionManager {

    companion object {
        // 传入一个权限，检查该权限是否已授权
        fun isPermissionGranted(activity : Activity, permission: String) : Boolean {
            return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        }

        // 请求权限
        fun requestPermission(activity: Activity, permission: String, requestCode: Int) {
            activity.requestPermissions(arrayOf(permission), requestCode)
        }
    }
}