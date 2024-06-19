package com.androidcourse.mediaplayer

import android.app.Notification
import android.app.Notification.MediaStyle
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.graphics.drawable.Icon
import android.media.MediaMetadata
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.nfc.Tag
import android.os.Build
import android.util.Log
import com.androidcourse.mediaplayer.services.MediaPlayerService
import com.androidcourse.mediaplayer.statics.IntentFields

class MediaNotificationManager(service: MediaPlayerService) : BroadcastReceiver() {
    val m_vNotificationManager: NotificationManager
    private val m_vPlayAction: Notification.Action
    private val m_vPauseAction: Notification.Action
    private val m_vPlayNextAction: Notification.Action
    private val m_vPlayPrevAction: Notification.Action
    private val m_vService: MediaPlayerService
    private var m_vStarted: Boolean
    private var isRegistered: Boolean

    init {
        m_vService = service
        m_vStarted = false
        isRegistered = false
        val pkgName: String = m_vService.getPackageName()
        val playActionIntent = PendingIntent.getBroadcast(
            m_vService as Context,
            100,
            Intent(IntentFields.ACTION_PLAY).setPackage(pkgName),
            PendingIntent.FLAG_IMMUTABLE
        )
        val pauseActionIntent = PendingIntent.getBroadcast(
            m_vService as Context,
            100,
            Intent(IntentFields.ACTION_PAUSE).setPackage(pkgName),
            PendingIntent.FLAG_IMMUTABLE
        )
        val playPrevActionIntent = PendingIntent.getBroadcast(
            m_vService as Context,
            100,
            Intent(IntentFields.ACTION_PREV).setPackage(pkgName),
            PendingIntent.FLAG_IMMUTABLE
        )
        val playNextActionIntent = PendingIntent.getBroadcast(
            m_vService as Context,
            100,
            Intent(IntentFields.ACTION_NEXT).setPackage(pkgName),
            PendingIntent.FLAG_IMMUTABLE
        )
        m_vPlayAction = Notification.Action.Builder(
            Icon.createWithResource(
                m_vService as Context, com.androidcourse.icons_pack.R.drawable.play_arrow_24px
            ), "Play", playActionIntent
        ).build()
        m_vPauseAction = Notification.Action.Builder(
            Icon.createWithResource(
                m_vService as Context, com.androidcourse.icons_pack.R.drawable.pause_24px
            ), "Pause", pauseActionIntent
        ).build()
        m_vPlayNextAction = Notification.Action.Builder(
            Icon.createWithResource(
                m_vService as Context, com.androidcourse.icons_pack.R.drawable.skip_next_24px
            ), "Play Next", playNextActionIntent
        ).build()
        m_vPlayPrevAction = Notification.Action.Builder(
            Icon.createWithResource(
                m_vService as Context, com.androidcourse.icons_pack.R.drawable.skip_previous_24px
            ), "Play Previous", playPrevActionIntent
        ).build()
        val intentFilter = IntentFilter()
        intentFilter.addAction(IntentFields.ACTION_PLAY)
        intentFilter.addAction(IntentFields.ACTION_PAUSE)
        intentFilter.addAction(IntentFields.ACTION_PREV)
        intentFilter.addAction(IntentFields.ACTION_NEXT)
        m_vService.registerReceiver(this, intentFilter, Context.RECEIVER_EXPORTED)
        isRegistered = true
        m_vNotificationManager = m_vService.getSystemService(NotificationManager::class.java)
        m_vNotificationManager.cancelAll()
        val channel =
            NotificationChannel(pkgName, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_NONE)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        m_vNotificationManager.createNotificationChannel(channel)
    }

    fun onStop() {
        if (m_vStarted) {
            m_vStarted = false
            try {
                m_vNotificationManager.cancel(NOTIFICATION_ID)
                m_vService.unregisterReceiver(this)
            } catch (ignore: Exception) {
            }
            m_vService.stopForeground(true)
        } else if (isRegistered) {
            try {
                m_vService.unregisterReceiver(this)
            } catch (ignore: Exception) {
            }
        }
    }

    private val contentIntent: PendingIntent
        private get() {
            val intent = Intent("com.example.softmusic_beta.MainActivity")
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            return PendingIntent.getActivity(
                m_vService as Context,
                100,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }

    // Receive the broadcast
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            IntentFields.ACTION_PLAY -> m_vService.onPlay()
            IntentFields.ACTION_PAUSE -> m_vService.onPause()
            IntentFields.ACTION_PREV -> m_vService.onPlayPrev()
            IntentFields.ACTION_NEXT -> m_vService.onPlayNext()
        }
    }

    // Update the Notification with the MediaMetadata
    fun onUpdateNotification(
        mediaMetadata: MediaMetadata,
        playbackState: PlaybackState?,
        sessionToken: MediaSession.Token?
    ) {
        if (playbackState == null || playbackState.state == PlaybackState.STATE_STOPPED || playbackState.state == PlaybackState.STATE_NONE) {
            m_vService.stopForeground(true)
            try {
                m_vService.unregisterReceiver(this)
            } catch (ignore: Exception) {
            }
            m_vService.stopSelf()
            return
        }
        val isPlaying = playbackState.state == PlaybackState.STATE_PLAYING
        val actions =
            if (m_vService.getPlaybackManager().canPlayPrev()) intArrayOf(0, 1) else IntArray(1)
        val mediaStyle = MediaStyle()
            .setMediaSession(sessionToken)
            .setShowActionsInCompactView(*actions)
        val mediaDescription = mediaMetadata.getDescription()
        val builder: Notification.Builder =
            Notification.Builder(m_vService as Context, IntentFields.CHANNEL_ID)
                .setCategory("service")
                .setStyle(mediaStyle as Notification.Style)
                .setContentIntent(contentIntent)
                .setSmallIcon(com.androidcourse.icons_pack.R.drawable.album_24px) //此处是展示图标
                .setContentTitle(mediaDescription.title)
                .setContentText(mediaDescription.subtitle)
                .setLargeIcon(mediaDescription.iconBitmap)
                .setOngoing(isPlaying)
        val action = if (isPlaying) m_vPauseAction else m_vPlayAction
        if (m_vService.getPlaybackManager().canPlayPrev()) {
            builder.addAction(m_vPlayPrevAction)
        }
        builder.addAction(action)
        if (m_vService.getPlaybackManager().canPlayNext()) {
            builder.addAction(m_vPlayNextAction)
        }
        val notification = builder.build()
        if (isPlaying && !m_vStarted) {
            val intent = Intent(
                m_vService as Context,
                MediaPlayerService::class.java
            )
            m_vService.startForegroundService(intent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                m_vService.startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                )
            } else {
                m_vService.startForeground(NOTIFICATION_ID, notification)
            }
            m_vStarted = true
        } else {
            if (!isPlaying && m_vStarted) {
                m_vService.stopForeground(false)
                m_vStarted = false
            }
            m_vNotificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    companion object {
        val TAG = MediaNotificationManager::class.java.getSimpleName()
        val NOTIFICATION_NAME: String = MediaPlayerService::class.java.getSimpleName()
        const val NOTIFICATION_ID = 101
    }
}
