package com.androidcourse.mediaplayer.interfaces

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

interface ICustomBroadcastReceiver {

    fun onReceive(context : Context, intent: Intent)

    fun build(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                this@ICustomBroadcastReceiver.onReceive(context, intent)
            }
        }
    }
}

/*
自定义广播接收器接口
用于构建一个标准的 BroadcastReceiver 实例
并将接收到的广播转发到 onReceive 方法
 */