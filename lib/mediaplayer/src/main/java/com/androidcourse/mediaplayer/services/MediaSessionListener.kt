package com.androidcourse.mediaplayer.services

import android.media.session.MediaSession

class MediaSessionListener(service : MediaPlayerService)
    : MediaSession.Callback(){

    private val m_Service : MediaPlayerService

    init{
        m_Service = service
    }

    override fun onPlay() {
        this.m_Service.onPlay()
    }

    override fun onPause() {
        this.m_Service.onPause()
    }

    override fun onSkipToNext() {
        this.m_Service.onPlayNext()
    }

    override fun onSkipToPrevious() {
        this.m_Service.onPlayPrev()
    }

    override fun onSeekTo(pos: Long) {
        this.m_Service.setSeekbarPosition(pos.toInt())
    }

    override fun onStop() {
        this.m_Service.stopSelf()
    }


}