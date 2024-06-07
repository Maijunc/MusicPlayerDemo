package com.androidcourse.mediaplayer.utils

import android.annotation.SuppressLint
import android.media.session.PlaybackState
import android.os.SystemClock
import com.androidcourse.mediaplayer.PlaybackManager
import java.util.concurrent.atomic.AtomicBoolean

class PlaybackSubThread(interval : Int, playbackManager: PlaybackManager)
    : Runnable {

    private var isRunning : AtomicBoolean = AtomicBoolean(false)

    private var isStopped : AtomicBoolean = AtomicBoolean(false)

    private val m_Interval : Int

    private val m_Worker : Thread

    private val m_PlaybackManager : PlaybackManager

    init {
        m_PlaybackManager = playbackManager
        m_Worker = Thread(this)
        m_Interval = interval
    }

    // It would've worked the other way too but now we making use of the 'isStopped' function

    override fun run() {

        this.isRunning.set(true)
        this.isStopped.set(false)

        while(isRunning()) {
            if(isStopped()) {
                break
            }
            // Do Some UI Updates actually Notification Updates
            val actions : Long = this.m_PlaybackManager.getAvailableActions()
            val state : Int = this.m_PlaybackManager.getPlaybackState()
            val position : Int = this.m_PlaybackManager.getPlaybackPosition()

            if(this.m_PlaybackManager.isPlayingOrPaused()) {
                @SuppressLint("WrongConstant")
                val builder : PlaybackState.Builder = PlaybackState.Builder()
                    .setActions(this.m_PlaybackManager.getAvailableActions())
                    .setState(state, position.toLong(), 1.0F, SystemClock.elapsedRealtime())

                this.m_PlaybackManager.getPlaybackCallback().onPlaybackStateChange(builder.build())
                if(state != PlaybackState.STATE_PLAYING) {
                    this.onStop()
                }
            }

            try {
                Thread.sleep(this.m_Interval.toLong())
            }
            catch (ignore : InterruptedException) {
                this.interrupt()
            }
        }

        isStopped.set(false)
        isRunning.set(false)
    }

    private fun interrupt() {
        this.isStopped.set(true)
        this.m_Worker.interrupt()
    }

    fun getWorker() : Thread { return this.m_Worker }

    fun isRunning() : Boolean { return this.isRunning.get() }

    fun isStopped() : Boolean { return this.isStopped.get() }

    fun onStart() { this.m_Worker.start() }

    fun onStop() { this.interrupt() }
}