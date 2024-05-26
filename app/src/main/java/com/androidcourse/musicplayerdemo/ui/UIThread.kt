package com.androidcourse.musicplayerdemo.ui

import android.view.View
import androidx.annotation.IdRes
import com.androidcourse.musicplayerdemo.MainActivity
import com.androidcourse.musicplayerdemo.R
import com.androidcourse.musicplayerdemo.views.panels.RootMediaPlayerPanel
import com.androidcourse.musicplayerdemo.views.panels.RootNavigationBarPanel
import com.realgear.multislidinguppanel.MultiSlidingUpPanelLayout
import com.realgear.multislidinguppanel.PanelStateListener
import com.realgear.multislidinguppanel.Adapter


class UIThread(activity : MainActivity) {
    // 这不是一个子线程，只是把main中的ui生成挪到了这里
    private val m_MainActivity : MainActivity

    private lateinit var m_MultiSlidingPanel : MultiSlidingUpPanelLayout

    init {
        m_MainActivity = activity

        onCreate()
    }

    fun onCreate() {
        // MultiSlidingUpPanelLayout 多个可滑动面板Layout
        val panelLayout: MultiSlidingUpPanelLayout = findViewById(R.id.root_sliding_up_panel)

        val items: MutableList<Class<*>> = ArrayList()

        items.add(RootMediaPlayerPanel::class.java)
        items.add(RootNavigationBarPanel::class.java)

        panelLayout.setPanelStateListener(object : PanelStateListener(panelLayout) {})

        panelLayout.setAdapter(Adapter(this.m_MainActivity, items))
    }

    fun <T : View?> findViewById(@IdRes id: Int): T {
        return this.m_MainActivity.findViewById(id)
    }
}