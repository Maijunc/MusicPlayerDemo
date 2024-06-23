package com.androidcourse.mediaplayer

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.session.PlaybackState
import android.nfc.Tag
import android.os.Handler
import android.os.PowerManager
import android.os.SystemClock
import android.util.Log
import androidx.annotation.IntDef
import com.androidcourse.mediaplayer.interfaces.IPlaybackCallback
import com.androidcourse.mediaplayer.utils.PlaybackListener
import com.androidcourse.mediaplayer.utils.PlaybackSubThread
import com.realgear.mediaplayer.model.Song
import java.io.File
import java.util.TreeMap


// 广播接收后最后处理逻辑的一层
class PlaybackManager(context: Context, playbackCallback: IPlaybackCallback) {

    companion object {
        val TAG : String = PlaybackManager::class.java.simpleName

        // 定义注解 @IntDef注解用来定义一个整数类型的枚举，即只允许变量取枚举中定义的值
        @Retention(AnnotationRetention.SOURCE)
        @IntDef(REPEAT_TYPE_NONE, REPEAT_TYPE_ONE, REPEAT_TYPE_ALL)
        annotation class RepeatType {}

        // 不循环
        const val REPEAT_TYPE_NONE = 0
        // 单曲循环
        const val REPEAT_TYPE_ONE = 1
        // 列表循环
        const val REPEAT_TYPE_ALL = 2
    }

    // 限制 m_RepeatType 的值只能是上面三种参数的一种
    @RepeatType
    var m_RepeatType = REPEAT_TYPE_ONE

    val THREAD_UPDATE_INTERVAL : Int = 500 // 比较丝滑的更新间隔

    private var m_PlaybackState : Int = 0

    private var m_IsPrepared : Boolean = false
    private var m_PlayOnFocusGain : Boolean = false

    private var m_Callback : IPlaybackCallback
    private val m_AudioManager : AudioManager
    private val m_Listener : PlaybackListener
    private val m_UIHandler : Handler
    private val m_Context : Context

    private var m_AudioFocusRequest : AudioFocusRequest? = null
    private var m_MediaPlayer : MediaPlayer? = null

    private var m_CurrentQueueIndex : Int = 0
    private var m_CurrentSong : Song? = null

    private var m_Queue : MutableList<Int>
    private val m_Song : TreeMap<Int, Song>
    private val m_Threads : MutableList<PlaybackSubThread>

    init {
        this.m_Context = context
        this.m_Callback = playbackCallback

        this.m_Listener = PlaybackListener(this)
        this.m_UIHandler = android.os.Handler()
        this.m_AudioManager = context.getSystemService(AudioManager::class.java)

        this.m_Queue = ArrayList()
        this.m_Threads = ArrayList()

        this.m_Song = LibraryManager.getTreemapOfSongs(LibraryManager.getSongs(this.m_Context))
    }

    // 放弃音频焦点
    private fun onAbandonAudioFocus() {
        if(this.m_AudioFocusRequest != null) {
            this.m_AudioManager.abandonAudioFocusRequest(this.m_AudioFocusRequest!!)
        }
    }

    // 获取音频焦点
    private fun onGetAudioFocus() : Boolean {
        if(this.m_AudioFocusRequest == null) {
            val builder : AudioFocusRequest.Builder = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setFocusGain(AudioManager.AUDIOFOCUS_GAIN)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener(this.m_Listener)
                .setAudioAttributes(AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build())

            this.m_AudioFocusRequest = builder.build()
        }

        return this.m_AudioManager.requestAudioFocus(this.m_AudioFocusRequest!!) != AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }

    private fun onPlay() {
        this.m_MediaPlayer!!.start()
        this.m_PlaybackState = PlaybackState.STATE_PLAYING
        this.onUpdatePlaybackState()
    }

    private fun onRelease() {
        if(this.m_MediaPlayer != null) {
            this.m_MediaPlayer!!.reset()
            this.m_MediaPlayer!!.release()
            this.m_MediaPlayer = null
        }
    }

    private fun onStartMediaPlayer() {
        this.m_IsPrepared = true
        if(!this.m_PlayOnFocusGain)
            this.onPlay()
    }

    private fun onStoppingThreads() {
        if(this.m_Threads.size > 0)
            for (thread : PlaybackSubThread in this.m_Threads) {
                thread.onStop()
                this.m_Threads.remove(thread)
            }
    }

    // 刷新，重新生成一个thread
    private fun onUpdatePlaybackState() {
        //不需要做此判断
        /*
        if(this.m_Callback == null)
            return
         */

        this.onStoppingThreads()

        if(this.m_PlaybackState == PlaybackState.STATE_PLAYING) {
            val thread: PlaybackSubThread = PlaybackSubThread(THREAD_UPDATE_INTERVAL, this)
            thread.getWorker().name = "Playback Sub Thread"
            this.m_Threads.add(thread)
            thread.onStart()
        }
        else {
            @SuppressLint("WrongConstant")
            val builder : PlaybackState.Builder = PlaybackState.Builder()
                .setActions(this.getAvailableActions())
                .setState(this.getPlaybackState(), this.getPlaybackState().toLong(), 1.0F, SystemClock.elapsedRealtime())

            this.m_Callback.onPlaybackStateChange(builder.build())
        }
    }

    // Playback Actions (Notification uses)
    fun canPlayNext() : Boolean {
        return (this.m_CurrentQueueIndex + 1) < this.m_Queue.size
    }

    fun canPlayPrev() : Boolean {
        return (this.m_CurrentQueueIndex - 1) >= 0
    }

    fun getAvailableActions() : Long {
        var actions : Long = PlaybackState.ACTION_PLAY
        if(this.isPlaying()) {
            actions = actions or PlaybackState.ACTION_PAUSE
        }

        return actions or PlaybackState.ACTION_STOP or PlaybackState.ACTION_SKIP_TO_PREVIOUS or PlaybackState.ACTION_SKIP_TO_NEXT or PlaybackState.ACTION_SEEK_TO
    }

    fun getCurrentQueueIndex() : Int {
        return this.m_CurrentQueueIndex
    }

    fun getCurrentSong() : Song? {
        return this.m_CurrentSong
    }

    fun getPlaybackPosition() : Int {
        if(this.isPlaying()) {
            return this.m_MediaPlayer!!.currentPosition
        }

        return 0
    }

    fun getPlaybackState() : Int {
        return this.m_PlaybackState
    }

    fun getPlaybackCallback() : IPlaybackCallback {
        return this.m_Callback
    }

    fun isPlaying() : Boolean {
        return (!this.m_PlayOnFocusGain && this.m_MediaPlayer != null && this.m_PlaybackState == PlaybackState.STATE_PLAYING && this.m_MediaPlayer!!.isPlaying())
    }
    fun isPlayingOrPaused() : Boolean {
        return (this.isPlaying() || (this.m_MediaPlayer != null && this.m_PlaybackState == PlaybackState.STATE_PAUSED))
    }

    fun onAudioCompleted() {
        Log.i(TAG,"AudioCompleted, PlaybackState = " + m_RepeatType)

        if(this.isPlaying()) {
            this.onStop()
        }
        else {
            when (this.m_RepeatType) {
                REPEAT_TYPE_NONE -> {
                    this.m_PlaybackState = PlaybackState.STATE_PAUSED;
                    this.m_MediaPlayer!!.pause();
                    this.m_MediaPlayer!!.seekTo(0);
                    onUpdatePlaybackState();
                }
                REPEAT_TYPE_ONE -> {
                    onPlayIndex(this.m_CurrentQueueIndex)
                }
                REPEAT_TYPE_ALL -> {
                    if(canPlayNext())
                        this.onPlayNext()
                    else
                        this.onPlayIndex(0) //从头开始播放
                }
                else -> {
                    Log.i(TAG, "REPEAT_TYPE_NONE")
                }
            }
        }
    }

    // 音频焦点
    fun onAudioFocusChanged(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                if(this.m_PlayOnFocusGain) {
                    this.m_PlayOnFocusGain = false
                    this.onPlay()
                }
                return
            }
            // 在Android studio里面Duck Focus Gain是指在音频混合过程中降低（或"duck"）某个音频信号的音量
            // We should also add duck focus gain but we will add that later
            else -> {
                if (isPlaying()) {
                    onPause()
                    this.m_PlayOnFocusGain = true
                }
            }
        }
    }

    fun onPlayIndex(queueIndex : Int) {
        Log.i(TAG, "Previous Song Index: " + this.m_CurrentQueueIndex);

        val id : Int = this.m_Queue.get(queueIndex)

        val songToPlay : Song? = this.m_Song.get(id)

        // 判断是否和当前在播放的是同一首歌
        var isSameSong : Boolean = (this.m_CurrentSong != null && queueIndex == this.m_CurrentQueueIndex && this.m_CurrentSong!!.id.toInt() == id)

        if(this.m_MediaPlayer == null) {
            this.m_MediaPlayer = MediaPlayer()
            // 让MediaPlayer能在后台跑
            this.m_MediaPlayer!!.setWakeMode(this.m_Context, PowerManager.PARTIAL_WAKE_LOCK)
            this.m_MediaPlayer!!.setOnCompletionListener(this.m_Listener)
            this.m_MediaPlayer!!.setOnPreparedListener(this.m_Listener)
        }
        else if(!isSameSong) {
            this.m_PlaybackState = PlaybackState.STATE_NONE
            this.m_MediaPlayer!!.reset()
            this.m_IsPrepared = false
        }

        this.m_PlayOnFocusGain = this.onGetAudioFocus()
        if(!isSameSong) {
            this.m_Callback.onUpdateMetadata(songToPlay)
            this.m_CurrentSong = songToPlay
            this.m_CurrentQueueIndex = queueIndex

            if(songToPlay != null) { // check not really needed here
                val filePath = songToPlay.data
                val file = File(filePath)
                if (file.exists()) {
                    try {
                        this.m_MediaPlayer!!.setDataSource(filePath)
                        this.m_MediaPlayer!!.prepareAsync()
                    } catch (e: Exception) {
                        Log.e(TAG, "MediaPlayer setDataSource or prepareAsync failed", e)
                    }
                } else {
                    Log.e(TAG, "File not found: $filePath")
                    // if file not found
                }
            }
            else {
                this.onStartMediaPlayer()
            }
        }
        else {
            this.onStartMediaPlayer()
        }
        Log.i(TAG, "Playing Song Index: " + this.m_CurrentQueueIndex);
    }
    fun onPause() {
        if(this.m_MediaPlayer == null)
            return

        if(this.isPlaying())
            this.m_MediaPlayer!!.pause()

        m_PlaybackState = PlaybackState.STATE_PAUSED
        this.onUpdatePlaybackState()
    }
    fun onPlayPause() {
        Log.i("OnPlayPause", "Click")

        if(this.m_MediaPlayer == null)
            return

        if(this.m_MediaPlayer!!.isPlaying())
            this.onPause()
        else
            this.onPlayIndex(this.m_CurrentQueueIndex)
    }
    fun onPlayNext() {
        if(this.canPlayNext())
            this.onPlayIndex(this.m_CurrentQueueIndex + 1)
    }
    // 这里应该是 --this.m_CurrentQueueIndex ?
    fun onPlayPrev() {
        if(this.canPlayPrev())
            this.onPlayIndex(this.m_CurrentQueueIndex - 1)
    }

    fun onSeekTo(position : Long) {
        if(this.m_MediaPlayer != null && isPlayingOrPaused()) {
            this.m_MediaPlayer!!.seekTo(position.toInt())
            onUpdatePlaybackState() //Update the playback state
        }
    }
    fun onSetQueue(queue : MutableList<Int>) {
        this.m_Queue = queue
    }
    // 设置歌曲循环状态
    fun onSetRepeatState(@RepeatType repeatState : Int) {
        this.m_RepeatType = repeatState
    }

    // 保证传入的MediaPlayer是当前Playback对应的MediaPlayer
    fun onStartMediaPlayer(mediaPlayer: MediaPlayer?) {
        if(this.m_MediaPlayer != mediaPlayer)
            return

        this.onStartMediaPlayer()
    }

    fun onStop() {
        if(this.m_MediaPlayer != null) {
            this.m_PlaybackState = PlaybackState.STATE_STOPPED
            onUpdatePlaybackState()
            onAbandonAudioFocus()
            onRelease()
        }
    }

    fun onUpdateIndex(index : Int) {
        this.m_CurrentQueueIndex = index
    }
}