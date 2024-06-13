package com.androidcourse.mediaplayer

import android.app.Activity
import android.content.Intent
import android.media.session.MediaController
import com.androidcourse.mediaplayer.statics.IntentFields
import com.androidcourse.mediaplayer.interfaces.IPlayerCallback


class CorePlayer(private val m_vActivity: Activity, callback: MediaController.Callback?) {
    private var m_vCallback: IPlayerCallback?
    private val m_vMediaPlayerController: MediaPlayerController

    init {
        m_vCallback = this.callback
        m_vMediaPlayerController = MediaPlayerController(m_vActivity, callback!!)
    }

    val callback: IPlayerCallback?
        get() {
            m_vCallback = if (m_vCallback == null) object : IPlayerCallback {
                override fun onClickPlay(queueIndex: Int, queue: List<Int>) {
                    val new_queue = ArrayList<Int>()
                    for (i in queue) {
                        new_queue.add(i)
                    }
                    val intent = Intent(IntentFields.INTENT_PLAY)
                    intent.putIntegerArrayListExtra(IntentFields.EXTRA_TRACKS_QUEUE, new_queue)
                    intent.putExtra(IntentFields.EXTRA_TRACK_INDEX, queueIndex)
                    m_vActivity.sendBroadcast(intent)
                }

                override fun onClickPlayIndex(index: Int) {}

                override fun onClickPlayPause() {
                    val intent : Intent = Intent(IntentFields.INTENT_PLAY_PAUSE)
                    m_vActivity.sendBroadcast(intent)
                }

                override fun onClickPlayNext() {}
                override fun onClickPlayPrev() {}
                override fun onClickPause() {
                    val intent = Intent(IntentFields.INTENT_PLAY_PAUSE)
                    m_vActivity.sendBroadcast(intent)
                }

                override fun onSetSeekbar(position: Int) {}
                override fun onUpdateQueue(queue : List<Int>, queueIndex : Int) {}
                override fun onDestroy() {}
            } else m_vCallback
            return m_vCallback
        }

    fun onDestroy() {
        m_vMediaPlayerController.onDestroy()
    }

    fun onStart() {
        m_vMediaPlayerController.onStart()
    }
}