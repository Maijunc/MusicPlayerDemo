package com.androidcourse.musicplayerdemo.views.panels

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.androidcourse.musicplayerdemo.R
import com.realgear.extensions.bottomsheet.CustomBottomSheetBehavior
import com.realgear.multislidinguppanel.BasePanelView
import com.realgear.multislidinguppanel.MultiSlidingUpPanelLayout


class RootMediaPlayerPanel(context : Context, panelLayout : MultiSlidingUpPanelLayout)
    : BasePanelView(context, panelLayout) {

    init {
        // Context 应该是个 Singleton
        getContext().setTheme(R.style.Theme_MusicPlayerDemo)
        LayoutInflater.from(getContext()).inflate(R.layout.layout_root_mediaplayer, this, true)
    }

    override fun onCreateView() {
        // The panel will be collapsed(折叠) on start of application
        this.panelState = MultiSlidingUpPanelLayout.COLLAPSED

        // The panel will slide up and down
        this.mSlideDirection = MultiSlidingUpPanelLayout.SLIDE_VERTICAL

        // Sets the panels peak height
        this.peakHeight = resources.getDimensionPixelSize(R.dimen.mediaplayerbar_height)
    }

    override fun onBindView() {
        val layout : FrameLayout = findViewById(R.id.media_player_bottom_sheet_behavior)

        val bottomSheetBehavior : CustomBottomSheetBehavior<FrameLayout> = CustomBottomSheetBehavior.from(layout)
        bottomSheetBehavior.skipAnchored = false
        bottomSheetBehavior.allowUserDragging = true

        val dm : DisplayMetrics = resources.displayMetrics

        bottomSheetBehavior.setAnchorOffset((dm.heightPixels * 0.75F).toInt())
        bottomSheetBehavior.state = CustomBottomSheetBehavior.STATE_COLLAPSED

        bottomSheetBehavior.addBottomSheetCallback(object :
            CustomBottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, oldState: Int, newState: Int) {
                when (newState) {
                    CustomBottomSheetBehavior.STATE_COLLAPSED -> {

                    }

                    CustomBottomSheetBehavior.STATE_ANCHORED -> {

                    }

                    CustomBottomSheetBehavior.STATE_EXPANDED -> {

                    }

                    CustomBottomSheetBehavior.STATE_DRAGGING -> multiSlidingUpPanel.isSlidingEnabled = false
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    override fun onPanelStateChanged(p0: Int) {
    }
}