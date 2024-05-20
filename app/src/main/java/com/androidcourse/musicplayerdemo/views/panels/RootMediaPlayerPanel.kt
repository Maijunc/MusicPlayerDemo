package com.androidcourse.musicplayerdemo.views.panels

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import com.androidcourse.musicplayerdemo.R
import com.androidcourse.musicplayerdemo.ui.adapters.StateFragmentAdapter
import com.androidcourse.musicplayerdemo.ui.fragments.FragmentHome
import com.androidcourse.musicplayerdemo.ui.fragments.FragmentLibrary
import com.androidcourse.musicplayerdemo.ui.fragments.FragmentSettings
import com.androidcourse.musicplayerdemo.views.MediaPlayerBarView
import com.androidcourse.musicplayerdemo.views.MediaPlayerView
import com.realgear.extensions.bottomsheet.CustomBottomSheetBehavior
import com.realgear.multislidinguppanel.BasePanelView
import com.realgear.multislidinguppanel.IPanel
import com.realgear.multislidinguppanel.MultiSlidingUpPanelLayout
import com.realgear.readable_bottom_bar.ReadableBottomBar


class RootMediaPlayerPanel(context : Context, panelLayout : MultiSlidingUpPanelLayout)
    : BasePanelView(context, panelLayout) {

    private lateinit var mMediaPlayerView : MediaPlayerView

    private lateinit var mMediaPlayerBarView : MediaPlayerBarView

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

    // 绑定数据用的
    override fun onBindView() {
        // 播放器视图 以及 播放器工具栏视图
        mMediaPlayerView = MediaPlayerView(findViewById(R.id.media_player_view))
        mMediaPlayerBarView = MediaPlayerBarView(findViewById(R.id.media_player_bar_view))

        // 播放器底部行为布局
        val layout : FrameLayout = findViewById(R.id.media_player_bottom_sheet_behavior)

        // 底部表单行为
        val bottomSheetBehavior : CustomBottomSheetBehavior<FrameLayout> = CustomBottomSheetBehavior.from(layout)
        bottomSheetBehavior.skipAnchored = false
        bottomSheetBehavior.allowUserDragging = true

        val dm : DisplayMetrics = resources.displayMetrics

        bottomSheetBehavior.anchorOffset = (dm.heightPixels * 0.75F).toInt()
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
                mMediaPlayerView.onSliding(slideOffset, MediaPlayerView.STATE_PARTIAL)
                mMediaPlayerBarView.onSliding(slideOffset, MediaPlayerBarView.STATE_PARTIAL)
            }
        })
    }

    override fun onPanelStateChanged(p0: Int) {
    }

    override fun onSliding(panel: IPanel<View>, top: Int, dy: Int, slidingOffset: Float) {
        super.onSliding(panel, top, dy, slidingOffset)

        mMediaPlayerView.onSliding(slidingOffset, MediaPlayerView.STATE_NORMAL)
        mMediaPlayerBarView.onSliding(slidingOffset, MediaPlayerBarView.STATE_NORMAL)
    }
}