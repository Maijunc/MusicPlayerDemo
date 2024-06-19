package com.androidcourse.mediaplayer.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.androidcourse.mediaplayer.interfaces.ICustomBroadcastReceiver
import com.androidcourse.mediaplayer.statics.IntentFields


class MediaPlayerBroadcastHelper(service: MediaPlayerService) {

    private val m_Service : MediaPlayerService

    init {
        m_Service = service
    }

    // 广播接收者：播放
    private val onPlay : BroadcastReceiver = object : ICustomBroadcastReceiver {
        override fun onReceive(context: Context, intent: Intent) {
            val queue: MutableList<Int>? = intent.getIntegerArrayListExtra(IntentFields.EXTRA_TRACKS_QUEUE)
            val index: Int = intent.getIntExtra(IntentFields.EXTRA_TRACK_INDEX, -1)

            if (index < 0 || queue.isNullOrEmpty()) return

            this@MediaPlayerBroadcastHelper.m_Service.onPlay(queue, index)
        }
    }.build()

    // 广播接收者：根据索引播放列表中的特定项目
    private val onPlayIndex : BroadcastReceiver = object : ICustomBroadcastReceiver {
        override fun onReceive(context: Context, intent: Intent) {
            val index: Int = intent.getIntExtra(IntentFields.EXTRA_TRACK_INDEX, -1)

            if (index < 0) return

            this@MediaPlayerBroadcastHelper.m_Service.onPlayIndex(index)
        }
    }.build()

    // 广播接收者：播放下一首
    private val onPlayNext : BroadcastReceiver = object : ICustomBroadcastReceiver {
        override fun onReceive(context: Context, intent: Intent) {
            this@MediaPlayerBroadcastHelper.m_Service.onPlayNext()
        }
    }.build()

    // 广播接收者：播放上一首
    private val onPlayPrev : BroadcastReceiver = object : ICustomBroadcastReceiver {
        override fun onReceive(context: Context, intent: Intent) {
            this@MediaPlayerBroadcastHelper.m_Service.onPlayPrev()
        }
    }.build()

    // 广播接收者：在播放和暂停之间切换
    private val onPlayPause : BroadcastReceiver = object : ICustomBroadcastReceiver {
        override fun onReceive(context: Context, intent: Intent) {
            this@MediaPlayerBroadcastHelper.m_Service.onPlayPause()
        }
    }.build()

    // 广播接收者：播放
    private val onUpdateQueue : BroadcastReceiver = object : ICustomBroadcastReceiver {
        override fun onReceive(context: Context, intent: Intent) {
            val queue: ArrayList<Int>? = intent.getIntegerArrayListExtra(IntentFields.EXTRA_TRACKS_QUEUE)
            val index: Int = intent.getIntExtra(IntentFields.EXTRA_TRACK_INDEX, -1)

            if (index < 0 || queue.isNullOrEmpty()) return

            this@MediaPlayerBroadcastHelper.m_Service.onUpdateQueue(queue, index)
        }
    }.build()

    // 广播接收者：设置进度条（SeekBar）的当前位置或进度
    private val onSetSeekbarPosition : BroadcastReceiver = object : ICustomBroadcastReceiver {
        override fun onReceive(context: Context, intent: Intent) {
            val position: Int = intent.getIntExtra(IntentFields.EXTRA_SEEK_BAR_POSITION, -1)

            if (position < 0) return

            this@MediaPlayerBroadcastHelper.m_Service.setSeekbarPosition(position)
        }
    }.build()

    // 广播接收者：设置循环播放的状态
    private val setRepeatState : BroadcastReceiver = object : ICustomBroadcastReceiver {
        override fun onReceive(context: Context, intent: Intent) {
            val state: Int = intent.getIntExtra(IntentFields.EXTRA_REPEAT_STATE, -1)

            this@MediaPlayerBroadcastHelper.m_Service.setRepeatState(state)
        }
    }.build()

    // 注册广播接收器
    fun registerReceivers() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.m_Service.registerReceiver(this.onPlay, IntentFilter(IntentFields.INTENT_PLAY), RECEIVER_EXPORTED)
            this.m_Service.registerReceiver(this.onPlayIndex, IntentFilter(IntentFields.INTENT_PLAY_INDEX), RECEIVER_EXPORTED)
            this.m_Service.registerReceiver(this.onPlayNext, IntentFilter(IntentFields.INTENT_PLAY_NEXT), RECEIVER_EXPORTED)
            this.m_Service.registerReceiver(this.onPlayPrev, IntentFilter(IntentFields.INTENT_PLAY_PREV), RECEIVER_EXPORTED)
            this.m_Service.registerReceiver(this.onPlayPause, IntentFilter(IntentFields.INTENT_PLAY_PAUSE), RECEIVER_EXPORTED)
            this.m_Service.registerReceiver(this.onUpdateQueue, IntentFilter(IntentFields.INTENT_UPDATE_QUEUE), RECEIVER_EXPORTED)
            this.m_Service.registerReceiver(this.onSetSeekbarPosition, IntentFilter(IntentFields.INTENT_SET_SEEKBAR), RECEIVER_EXPORTED)
            this.m_Service.registerReceiver(this.setRepeatState, IntentFilter(IntentFields.INTENT_CHANGE_REPEAT), RECEIVER_EXPORTED)
        }
        else {
            this.m_Service.registerReceiver(this.onPlay, IntentFilter(IntentFields.INTENT_PLAY))
            this.m_Service.registerReceiver(this.onPlayIndex, IntentFilter(IntentFields.INTENT_PLAY_INDEX))
            this.m_Service.registerReceiver(this.onPlayNext, IntentFilter(IntentFields.INTENT_PLAY_NEXT))
            this.m_Service.registerReceiver(this.onPlayPrev, IntentFilter(IntentFields.INTENT_PLAY_PREV))
            this.m_Service.registerReceiver(this.onPlayPause, IntentFilter(IntentFields.INTENT_PLAY_PAUSE))
            this.m_Service.registerReceiver(this.onUpdateQueue, IntentFilter(IntentFields.INTENT_UPDATE_QUEUE))
            this.m_Service.registerReceiver(this.onSetSeekbarPosition, IntentFilter(IntentFields.INTENT_SET_SEEKBAR))
            this.m_Service.registerReceiver(this.setRepeatState, IntentFilter(IntentFields.INTENT_CHANGE_REPEAT))
        }
    }

    fun unregisterReceivers() {
        this.m_Service.unregisterReceiver(this.onPlay)
        this.m_Service.unregisterReceiver(this.onPlayIndex)
        this.m_Service.unregisterReceiver(this.onPlayNext)
        this.m_Service.unregisterReceiver(this.onPlayPrev)
        this.m_Service.unregisterReceiver(this.onPlayPause)
        this.m_Service.unregisterReceiver(this.onUpdateQueue)
        this.m_Service.unregisterReceiver(this.setRepeatState)
        this.m_Service.unregisterReceiver(this.setRepeatState)
    }

}