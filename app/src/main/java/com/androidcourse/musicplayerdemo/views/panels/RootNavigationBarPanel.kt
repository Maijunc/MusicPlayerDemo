package com.androidcourse.musicplayerdemo.views.panels

import android.content.Context
import android.view.LayoutInflater
import com.androidcourse.musicplayerdemo.R
import com.realgear.multislidinguppanel.BasePanelView
import com.realgear.multislidinguppanel.MultiSlidingUpPanelLayout

class RootNavigationBarPanel (context : Context, panelLayout : MultiSlidingUpPanelLayout)
    : BasePanelView(context, panelLayout) {

    init {
        // Context 应该是个 Singleton
        getContext().setTheme(R.style.Theme_MusicPlayerDemo)
        LayoutInflater.from(getContext()).inflate(R.layout.layout_root_navigation_bar, this, true)
    }

    override fun onCreateView() {
        // The panel will be collapsed(折叠) on start of application
        this.panelState = MultiSlidingUpPanelLayout.COLLAPSED

        // The panel will slide up and down
        this.mSlideDirection = MultiSlidingUpPanelLayout.SLIDE_VERTICAL

        // Sets the panels peak height
        this.peakHeight = resources.getDimensionPixelSize(R.dimen.navigation_bar_height)
    }

    override fun onBindView() {
    }

    override fun onPanelStateChanged(p0: Int) {
    }

}