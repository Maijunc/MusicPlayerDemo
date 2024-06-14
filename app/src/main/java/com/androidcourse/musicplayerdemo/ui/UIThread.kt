package com.androidcourse.musicplayerdemo.ui

import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.PlaybackState
import android.view.View
import androidx.annotation.IdRes
import com.androidcourse.musicplayerdemo.MainActivity
import com.androidcourse.musicplayerdemo.R
import com.androidcourse.musicplayerdemo.views.panels.RootMediaPlayerPanel
import com.androidcourse.musicplayerdemo.views.panels.RootNavigationBarPanel
import com.realgear.multislidinguppanel.Adapter
import com.realgear.multislidinguppanel.MultiSlidingUpPanelLayout
import com.realgear.multislidinguppanel.PanelStateListener


class UIThread(activity : MainActivity) {
    companion object {
        private lateinit var instance : UIThread

        fun getInstance() : UIThread {
            return instance
        }
    }

    // 这不是一个子线程，只是把main中的ui载入挪到了这里，也就是一层封装
    private val m_MainActivity : MainActivity

    private lateinit var m_MultiSlidingPanel : MultiSlidingUpPanelLayout

    private val m_MediaPlayerThread : MediaPlayerThread


    init {
        instance = this

        m_MainActivity = activity
        onCreate()

        this.m_MediaPlayerThread = MediaPlayerThread(this.m_MainActivity, getCallback())
        this.m_MediaPlayerThread.onStart()
    }
    fun getCallback() : MediaController.Callback {
        return object : MediaController.Callback() {
            override fun onPlaybackStateChanged(state: PlaybackState?) {
                m_MultiSlidingPanel.adapter.getItem(RootMediaPlayerPanel::class.java).onPlaybackStateChanged(state)
            }

            override fun onMetadataChanged(metadata: MediaMetadata?) {
                m_MultiSlidingPanel.adapter.getItem(RootMediaPlayerPanel::class.java).onMetadataChanged(metadata)
            }
        }
    }

    fun onCreate() {
        // MultiSlidingUpPanelLayout 多个可滑动面板Layout
        m_MultiSlidingPanel = findViewById(R.id.root_sliding_up_panel)

        val items: MutableList<Class<*>> = ArrayList()

        items.add(RootMediaPlayerPanel::class.java)
        items.add(RootNavigationBarPanel::class.java)

        m_MultiSlidingPanel.setPanelStateListener(object : PanelStateListener(m_MultiSlidingPanel) {})

        m_MultiSlidingPanel.setAdapter(Adapter(this.m_MainActivity, items))
    }

    fun getMediaPlayerThread() : MediaPlayerThread {
        return this.m_MediaPlayerThread
    }

    fun <T : View?> findViewById(@IdRes id: Int): T {
        return this.m_MainActivity.findViewById(id)
    }
}