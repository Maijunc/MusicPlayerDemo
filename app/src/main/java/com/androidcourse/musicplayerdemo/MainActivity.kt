package com.androidcourse.musicplayerdemo

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.androidcourse.musicplayerdemo.ui.UIThread
import com.androidcourse.musicplayerdemo.utils.PermissionManager


class MainActivity : AppCompatActivity() {

    private lateinit var m_Thread : UIThread
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        PermissionManager.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, 100)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            PermissionManager.requestPermission(
                this,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                100
            )
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", packageName, null)
                intent.setData(uri)
                startActivity(intent)
            }
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


        m_Thread = UIThread(this)

    }
}