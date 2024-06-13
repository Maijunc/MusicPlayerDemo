package com.androidcourse.musicplayerdemo.ui

import android.media.session.MediaController
import com.androidcourse.mediaplayer.CorePlayer
import com.androidcourse.mediaplayer.interfaces.IPlayerCallback
import com.androidcourse.mediaplayer.statics.ClassManager
import com.androidcourse.musicplayerdemo.MainActivity

class MediaPlayerManager(activity: MainActivity, callback: MediaController.Callback) {

    companion object {
        private var instance : MediaPlayerManager? = null

        fun getInstance() : MediaPlayerManager? { return instance }
    }

    private val m_CorePlayer : CorePlayer

    private val m_Callback : IPlayerCallback?

    init {
        ClassManager.init(MainActivity::class.java)

        this.m_CorePlayer = CorePlayer(activity,callback)
        this.m_Callback = this.m_CorePlayer.callback

        setInstance(this)
    }

    private fun setInstance(instance : MediaPlayerManager) {
        if(MediaPlayerManager.instance == null) {
            MediaPlayerManager.instance = instance
        }
    }

    fun getCallback() : IPlayerCallback? { return this.m_Callback }

    fun onStart() { this.m_CorePlayer.onStart() }

    fun onDestroy() { this.m_CorePlayer.onDestroy() }
}