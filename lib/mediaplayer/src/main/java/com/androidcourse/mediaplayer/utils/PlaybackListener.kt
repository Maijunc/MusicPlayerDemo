package com.androidcourse.mediaplayer.utils

import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import com.androidcourse.mediaplayer.PlaybackManager

// 音频播放监听
class PlaybackListener(playbackManager: PlaybackManager)
    : AudioManager.OnAudioFocusChangeListener,
    MediaPlayer.OnCompletionListener,
    OnPreparedListener {

    private val m_PlaybackManager : PlaybackManager

    init {
        m_PlaybackManager = playbackManager
    }

    override fun onAudioFocusChange(focusChange: Int) {
        this.m_PlaybackManager.onAudioFocusChanged(focusChange)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        this.m_PlaybackManager.onAudioCompleted()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        this.m_PlaybackManager.onStartMediaPlayer(mp)
    }
}