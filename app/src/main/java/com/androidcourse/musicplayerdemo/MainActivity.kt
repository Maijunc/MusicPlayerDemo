package com.androidcourse.musicplayerdemo

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.androidcourse.musicplayerdemo.ui.UIThread
import com.androidcourse.musicplayerdemo.utils.PermissionManager


class MainActivity : AppCompatActivity() {

    private lateinit var m_Thread : UIThread
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        PermissionManager.requestPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE, 100)
        PermissionManager.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, 100)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                val uri = Uri.fromParts("package", packageName, null)
                intent.setData(uri)
                startActivity(intent)
            } catch (e : Exception) {
                val intent = Intent()
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            }
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                100
            )
        }

        // -------------------
        // 请求应用程序管理外部存储的权限。
//        PermissionManager.requestPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE, 100)
        // 请求读取外部存储的权限
//        PermissionManager.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, 100)

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 检查是否具有外部存储管理权限 如果没有权限，则启动一个设置界面 让用户授予该权限
            if (!Environment.isExternalStorageManager()) {
                val intent : Intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                val uri : Uri = Uri.fromParts("package", getPackageName(), null)
                intent.setData(uri)
                startActivity(intent)
            }
        }*/

        // ----------------
//        PermissionManager.requestPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE, 100)
//        PermissionManager.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, 100)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (!Environment.isExternalStorageEmulated()) {
//                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
//                val uri = Uri.fromParts("package", packageName, null)
//                intent.setData(uri)
//                startActivity(intent)
//            }
//        }


        this.m_Thread = UIThread(this)

    }
}