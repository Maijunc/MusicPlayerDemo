package com.androidcourse.mediaplayer

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.media.MediaMetadata
import android.media.browse.MediaBrowser
import android.media.browse.MediaBrowser.SubscriptionCallback
import android.media.session.MediaController
import android.media.session.PlaybackState
import android.util.Log
import com.androidcourse.mediaplayer.services.MediaPlayerService

class MediaPlayerController(activity: Activity, callback: MediaController.Callback) {
    companion object {
        val TAG : String = MediaPlayerController::class.java.simpleName
    }

    private val m_Activity : Activity

    private var m_MediaBrowser : MediaBrowser? = null
    private val m_ConnectionCallback : MediaBrowser.ConnectionCallback
    private val m_MediaControllerCallback : MediaController.Callback

    private val m_SubscriptionCallback : SubscriptionCallback = object : SubscriptionCallback() {
        override fun onChildrenLoaded(
            parentId: String,
            children: MutableList<MediaBrowser.MediaItem>
        ) {
//            super.onChildrenLoaded(parentId, children)
        }
    }

    init {
        this.m_Activity = activity
        this.m_MediaControllerCallback = callback

        this.m_ConnectionCallback = object : MediaBrowser.ConnectionCallback() {
            override fun onConnected() {
                if(m_MediaBrowser == null)
                    throw RuntimeException("m_MediaBrowser is null")

                Log.i(TAG, "Connected successfully to the media controller")

                m_MediaBrowser!!.subscribe(
                    m_MediaBrowser!!.root,
                    m_SubscriptionCallback
                )

                val mediaController : MediaController = MediaController(
                    m_Activity as Context,
                    m_MediaBrowser!!.sessionToken
                )

                mediaController.registerCallback(m_MediaControllerCallback)
                m_Activity.mediaController = mediaController

                if(mediaController.playbackState == null) {
                    // We haven't played music yet
                    return
                }

                val playbackState : PlaybackState = mediaController.playbackState!!

                if(playbackState.state == PlaybackState.STATE_PLAYING || playbackState.state == PlaybackState.STATE_PAUSED) {
                    // service is running in the background and playing music
                    // need to update the Ui
                    val mediaMetadata : MediaMetadata? = mediaController.metadata

                    //
                    m_MediaControllerCallback.onMetadataChanged(mediaMetadata)
                    m_MediaControllerCallback.onPlaybackStateChanged(playbackState)
                }
            }

            override fun onConnectionFailed() {
                super.onConnectionFailed()

                Log.e(TAG, "FAiled to connect to media controller")
            }
        }
    }

    fun onStart() {
        if (this.m_MediaBrowser != null && this.m_MediaBrowser!!.isConnected) {
            this.onDestroy()
        }

        //
        this.m_MediaBrowser = MediaBrowser(
            this.m_Activity,
            ComponentName(this.m_Activity, MediaPlayerService::class.java),
            this.m_ConnectionCallback,
            null
        )

        this.m_MediaBrowser!!.connect()
    }

    fun onDestroy() {
        if(this.m_MediaBrowser == null)
            return

        try {
            this.m_Activity.mediaController.unregisterCallback(this.m_MediaControllerCallback)
        }
        catch (ignore : Exception) {}

        if(this.m_MediaBrowser!!.isConnected) {
            this.m_MediaBrowser!!.disconnect()
        }
    }
}