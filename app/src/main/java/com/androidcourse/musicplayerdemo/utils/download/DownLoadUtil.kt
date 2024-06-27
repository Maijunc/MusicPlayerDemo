package com.androidcourse.musicplayerdemo.utils.download

import android.app.DownloadManager
import android.app.DownloadManager.Request
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Network
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast

class DownLoadUtil(private val context: Context){
    private var downloadId: Long = -1

    fun downloadFile(fileName: String, audioUrl: String):Long {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        // 创建下载请求
        val request = DownloadManager.Request(Uri.parse(audioUrl))
            .setTitle(fileName) // 设置下载文件的标题
            .setDescription("$fileName Downloading") // 设置下载描述
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) // 设置下载完成后是否显示通知
            .setAllowedOverMetered(true) // 允许在移动网络下下载
            .setAllowedOverRoaming(true) // 允许在漫游时下载
            // 设置下载文件保存的位置
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        // 注册下载完成的广播接收器
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    Log.i(TAG, "downloadStatus: successful")
                    // 下载完成，可以在这里处理下载完成的逻辑
                    // 例如：通知用户下载完成、播放下载的音频等
                    Toast.makeText(context,"$fileName 下载完成",Toast.LENGTH_SHORT).show()
                }
            }
        }
        context.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            Context.RECEIVER_NOT_EXPORTED)

        // 将下载请求加入到系统的下载队列，并返回一个下载任务的唯一标识 ID
        downloadId = downloadManager.enqueue(request)

        // 保存 downloadId，以便后续查询下载状态或者取消下载
        return downloadId
    }


    companion object {
        private const val TAG = "DownloadUtil"
    }
}