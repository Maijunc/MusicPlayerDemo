package com.androidcourse.musicplayerdemo.ui

import android.media.session.MediaController
import com.androidcourse.mediaplayer.CorePlayer
import com.androidcourse.mediaplayer.interfaces.IPlaybackCallback
import com.androidcourse.mediaplayer.interfaces.IPlayerCallback
import com.androidcourse.mediaplayer.statics.ClassManager
import com.androidcourse.musicplayerdemo.MainActivity

class MediaPlayerThread(activity : MainActivity, callback : MediaController.Callback) {
    companion object {
        val TAG : String = "MediaPlayerThread"

        private var instance : MediaPlayerThread? = null

        fun getInstance() : MediaPlayerThread? {
            return instance
        }
    }

    private val m_CorePlayer : CorePlayer

    private val m_Callback : IPlayerCallback?

    init {
        ClassManager.init(MainActivity::class.java)

        this.m_CorePlayer = CorePlayer(activity, callback)
        this.m_Callback = this.m_CorePlayer.callback

        instance = this
    }

    private fun setInstance(instance : MediaPlayerThread) {
        if(MediaPlayerThread.instance == null) {
            MediaPlayerThread.instance = instance
        }
    }

    fun getCallback() : IPlayerCallback? {
        return this.m_Callback
    }

    fun onStart() {
        this.m_CorePlayer.onStart()
    }

    fun onDestroy() {
        this.m_CorePlayer.onDestroy()
    }
}