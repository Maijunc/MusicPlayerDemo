package com.androidcourse.mediaplayer.services

import android.app.Service
import android.content.Intent
import android.media.browse.MediaBrowser
import android.media.session.PlaybackState
import android.os.Bundle
import android.service.media.MediaBrowserService
import com.androidcourse.mediaplayer.interfaces.IPlaybackCallback
import com.androidcourse.mediaplayer.interfaces.IPlayerCallback
import com.realgear.mediaplayer.model.Song

class MediaPlayerService
    : MediaBrowserService(), IPlaybackCallback {

    private val TAG : String = this.javaClass.simpleName

    private lateinit var m_BroadcastHelper : MediaPlayerBroadcastHelper

    private val m_Callback = MediaSessionListener(this)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        return MediaBrowserService.BrowserRoot("media_root_id", null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowser.MediaItem>>
    ) {
        result.detach()
    }

    override fun onPlaybackStateChange(playbackState: PlaybackState) {
        TODO("Not yet implemented")
    }

    override fun onUpdateMetadata(song: Song) {
        TODO("Not yet implemented")
    }

    fun onPlay() {

    }

    fun onPlayIndex(index : Int) {}

    fun onPlay(queue : List<Int>, index : Int) {}

    fun onPlayPause() {
        TODO("Not yet implemented")
    }

    fun onPause() {}

    fun onPlayNext() {}

    fun onPlayPrev() {}

    fun onUpdateQueue(queue : List<Int>, index : Int) {

    }

    fun setRepeatState() {}

    fun setSeekbarPosition(position : Int) {}


}