package com.androidcourse.musicplayerdemo.utils.download

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_NOT_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.util.Log

class DownloadUtils {
    fun download() {
        if (context == null) return
        //下载完成时，系统会自动发送一个 ACTION_DOWNLOAD_COMPLETE
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        val request = DownloadManager.Request(Uri.parse(url))

        // 指定下载路径及文件名
        request.setDestinationInExternalFilesDir(context, FILE_URI, fileName)

        // 获取下载管理器
        val downloadManager = context!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        // 配置
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        val id = downloadManager.enqueue(request)
        Log.i("DownloadUtils", "Start download, id = " + id)
        val receiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val myDownloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                Log.i("DownloadUtils", "Finish download, myDownloadID = " + myDownloadID)
                if (myDownloadID == id) {
                    val uri = downloadManager.getUriForDownloadedFile(myDownloadID)
                    lister?.success(uri, this)
                }
            }
        }

        if (context is Activity) {
            Log.i("DownloadUtils", "context is Activity")
            val activity = context as Activity
            activity.registerReceiver(receiver, filter, RECEIVER_NOT_EXPORTED)
        }
    }

    private var url = "https://taira-komori.jpn.org/sound_os2/daily01/knock_metal_door1.mp3"
    private val FILE_URI = "Download"
    private var lister: IDownloadListener? = null
    private var fileName = "knock_metal_door1.mp3"
    private var context: Context? = null

    fun setUrl(url: String): DownloadUtils {
        this.url = url
        return this
    }

    fun setLister(lister: IDownloadListener?): DownloadUtils {
        this.lister = lister
        return this
    }

    fun setFileName(fileName: String): DownloadUtils {
        this.fileName = fileName
        return this
    }

    fun setContext(context: Context?): DownloadUtils {
        this.context = context
        return this
    }

    companion object {
        fun builder(): DownloadUtils {
            return DownloadUtils()
        }
    }
}