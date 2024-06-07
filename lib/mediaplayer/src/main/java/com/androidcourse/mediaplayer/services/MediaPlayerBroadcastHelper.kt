package com.androidcourse.mediaplayer.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.androidcourse.mediaplayer.interfaces.ICustomBroadcastReceiver
import com.androidcourse.mediaplayer.statics.IntentFields


class MediaPlayerBroadcastHelper(service: MediaPlayerService) {

    private val m_Service : MediaPlayerService

    init {
        m_Service = service
    }

    private val onPlay = object : ICustomBroadcastReceiver {
        override fun onReceive(context: Context, intent: Intent) {
            val queue: ArrayList<Int>? = intent.getIntegerArrayListExtra(IntentFields.EXTRA_TRACKS_QUEUE)
            val index: Int = intent.getIntExtra(IntentFields.EXTRA_TRACK_INDEX, -1)

            if (index < 0 || queue.isNullOrEmpty()) return

            this@MediaPlayerBroadcastHelper.m_Service.onPlay(queue, index)
        }
    }.build()

    private val onPlayIndex = object : ICustomBroadcastReceiver {
        override fun onReceive(context: Context, intent: Intent) {
            val index: Int = intent.getIntExtra(IntentFields.EXTRA_TRACK_INDEX, -1)

            if (index < 0) return

            this@MediaPlayerBroadcastHelper.m_Service.onPlayIndex(index)
        }
    }.build()

    private val onPlayNext = object : ICustomBroadcastReceiver {
        override fun onReceive(context: Context, intent: Intent) {
            this@MediaPlayerBroadcastHelper.m_Service.onPlayNext()
        }
    }.build()

    private val onPlayPrev = object : ICustomBroadcastReceiver {
        override fun onReceive(context: Context, intent: Intent) {
            this@MediaPlayerBroadcastHelper.m_Service.onPlayPrev()
        }
    }.build()

    private val onPlayPause = object : ICustomBroadcastReceiver {
        override fun onReceive(context: Context, intent: Intent) {
            this@MediaPlayerBroadcastHelper.m_Service.onPlayPause()
        }
    }.build()

    private val onUpdateQueue = object : ICustomBroadcastReceiver {
        override fun onReceive(context: Context, intent: Intent) {
            val queue: ArrayList<Int>? = intent.getIntegerArrayListExtra(IntentFields.EXTRA_TRACKS_QUEUE)
            val index: Int = intent.getIntExtra(IntentFields.EXTRA_TRACK_INDEX, -1)

            if (index < 0 || queue.isNullOrEmpty()) return

            this@MediaPlayerBroadcastHelper.m_Service.onUpdateQueue(queue, index)
        }
    }.build()

    private val onSetSeekbarPosition = object : ICustomBroadcastReceiver {
        override fun onReceive(context: Context, intent: Intent) {
            val position: Int = intent.getIntExtra(IntentFields.EXTRA_SEEK_BAR_POSITION, -1)

            if (position < 0) return

            this@MediaPlayerBroadcastHelper.m_Service.setSeekbarPosition(position)
        }
    }.build()

    private val setRepeatState = object : ICustomBroadcastReceiver {
        override fun onReceive(context: Context, intent: Intent) {
            val state: Int = intent.getIntExtra(IntentFields.EXTRA_REPEAT_STATE, -1)
        }
    }.build()

    fun registerReceivers() {
        this.m_Service.registerReceiver(this.onPlay, IntentFilter(IntentFields.INTENT_PLAY))
        this.m_Service.registerReceiver(this.onPlayIndex, IntentFilter(IntentFields.INTENT_PLAY_INDEX))
        this.m_Service.registerReceiver(this.onPlayNext, IntentFilter(IntentFields.INTENT_PLAY_NEXT))
        this.m_Service.registerReceiver(this.onPlayPrev, IntentFilter(IntentFields.INTENT_PLAY_PREV))
        this.m_Service.registerReceiver(this.onPlayPause, IntentFilter(IntentFields.INTENT_PLAY_PAUSE))
        this.m_Service.registerReceiver(this.onUpdateQueue, IntentFilter(IntentFields.INTENT_UPDATE_QUEUE))
        this.m_Service.registerReceiver(this.setRepeatState, IntentFilter(IntentFields.INTENT_SET_SEEKBAR))
        this.m_Service.registerReceiver(this.setRepeatState, IntentFilter(IntentFields.INTENT_CHANGE_REPEAT))
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