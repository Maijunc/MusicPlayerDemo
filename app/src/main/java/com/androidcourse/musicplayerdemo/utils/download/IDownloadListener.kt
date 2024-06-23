package com.androidcourse.musicplayerdemo.utils.download

import android.content.BroadcastReceiver
import android.net.Uri

interface IDownloadListener {
    fun success(uri : Uri, receiver: BroadcastReceiver)
}