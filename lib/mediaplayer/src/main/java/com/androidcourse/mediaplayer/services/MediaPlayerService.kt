package com.androidcourse.mediaplayer.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.browse.MediaBrowser
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.os.Bundle
import android.service.media.MediaBrowserService
import com.androidcourse.mediaplayer.LibraryManager
import com.androidcourse.mediaplayer.MediaNotificationManager
import com.androidcourse.mediaplayer.PlaybackManager
import com.androidcourse.mediaplayer.interfaces.IPlaybackCallback
import com.realgear.mediaplayer.model.Song

// 客户端的相关服务
class MediaPlayerService
    : MediaBrowserService(), IPlaybackCallback {

    private val TAG : String = this.javaClass.simpleName

    private lateinit var m_BroadcastHelper : MediaPlayerBroadcastHelper

    private val m_Callback = MediaSessionListener(this)

    private lateinit var m_Context : Context
    private var m_CurrentIndex : Int = 0
    private lateinit var m_MediaSession : MediaSession
    private lateinit var m_NotificationManager : MediaNotificationManager
    private lateinit var m_PlaybackManager : PlaybackManager

    private var m_PrevSong : Song? = null
    private var m_PrevState : PlaybackState? = null

    // 如果服务被系统杀死，则尝试重新创建服务。
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        this.m_Context = applicationContext

        this.m_MediaSession = MediaSession(this.m_Context, TAG)
        this.m_MediaSession.setCallback(this.m_Callback)

        this.sessionToken = this.m_MediaSession.sessionToken

        this.m_NotificationManager = MediaNotificationManager(this)
        this.m_PlaybackManager = PlaybackManager(this.m_Context, this)
        this.m_BroadcastHelper = MediaPlayerBroadcastHelper(this)

        this.m_BroadcastHelper.registerReceivers()
    }

    override fun onDestroy() {
        this.m_PlaybackManager.onStop()
        this.m_MediaSession.release()
        this.m_BroadcastHelper.unregisterReceivers()
        this.m_NotificationManager.onStop()

        super.onDestroy()
    }

    fun getNotificationManager() : MediaNotificationManager {
        return this.m_NotificationManager
    }
    fun getPlaybackManager() : PlaybackManager {
        return this.m_PlaybackManager
    }

    // 确定客户端连接到 MediaBrowserService 后，是否有权限浏览和访问服务中的媒体内容
    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        // 返回允许访问的根节点
        return MediaBrowserService.BrowserRoot("media_root_id", null)
    }

    // 加载指定父节点的子节点（媒体内容）
    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowser.MediaItem>>) {
        result.detach()
    }

    // we could update this function to take in 2 arguments,
    // Current Song & Playback State
    override fun onPlaybackStateChange(playbackState: PlaybackState) {
        this.m_PrevSong = if(this.m_PrevSong == null) this.m_PlaybackManager.getCurrentSong() else m_PrevSong

        /*
        if(this.m_PrevState == null)
            this.m_PrevState = playbackState
        else if(this.m_PrevState!!.state == playbackState.state
            && this.m_PrevSong!!.id == this.m_PlaybackManager.getCurrentSong()?.id) {
            this.m_MediaSession.setPlaybackState(playbackState)
            return
        }
        */

        this.m_MediaSession.setPlaybackState(playbackState)
        this.m_NotificationManager.onUpdateNotification(
            LibraryManager.getMediaMetadata(this.m_PlaybackManager.getCurrentSong())!!,
            playbackState,
            this.m_MediaSession.sessionToken)
        this.m_PrevSong = this.m_PlaybackManager.getCurrentSong()
        this.m_PrevState = playbackState
        this.m_CurrentIndex = this.m_PlaybackManager.getCurrentQueueIndex()
    }

    override fun onUpdateMetadata(song: Song?) {
        this.m_MediaSession.setMetadata(LibraryManager.getMediaMetadata(song))
    }

    fun onPlayPause() {
        this.m_PlaybackManager.onPlayPause()
    }

    fun onPlay() {
        this.m_PlaybackManager.onPlayIndex(this.m_CurrentIndex)
    }

    fun onPlayIndex(index : Int) {
        this.m_PlaybackManager.onPlayIndex(index)
    }

    /* 尚未实现
    fun onFavourite() {
        this.m_vPlaybackManager.onFavourite()
    }
    */

    fun onPlay(queue : MutableList<Int>, index : Int) {
        this.m_PlaybackManager.onSetQueue(queue)
        this.m_PlaybackManager.onPlayIndex(index)
        this.m_CurrentIndex = index
    }

    fun onPause() {
        this.m_PlaybackManager.onPause()
    }

    fun onPlayNext() {
        this.m_PlaybackManager.onPlayNext()
    }

    fun onPlayPrev() {
        this.m_PlaybackManager.onPlayPrev()
    }

    fun onUpdateQueue(queue : MutableList<Int>, index : Int) {
        this.m_PlaybackManager.onSetQueue(queue)
        this.m_CurrentIndex = index
    }

    fun setRepeatState(@PlaybackManager.Companion.RepeatType repeatState : Int) {
        this.m_PlaybackManager.onSetRepeatState(repeatState)
    }

    fun setSeekbarPosition(position : Int) {
        this.m_PlaybackManager.onSeekTo(position.toLong())
    }


}