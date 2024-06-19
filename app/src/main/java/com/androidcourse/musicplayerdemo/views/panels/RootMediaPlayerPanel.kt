package com.androidcourse.musicplayerdemo.views.panels

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadata
import android.media.session.PlaybackState
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.androidcourse.musicplayerdemo.R
import com.androidcourse.musicplayerdemo.views.MediaPlayerBarView
import com.androidcourse.musicplayerdemo.views.MediaPlayerView
import com.realgear.extensions.bottomsheet.CustomBottomSheetBehavior
import com.realgear.multislidinguppanel.BasePanelView
import com.realgear.multislidinguppanel.IPanel
import com.realgear.multislidinguppanel.MultiSlidingUpPanelLayout


class RootMediaPlayerPanel(context : Context, panelLayout : MultiSlidingUpPanelLayout)
    : BasePanelView(context, panelLayout) {

    // 音乐播放器视图
    private var m_MediaPlayerView : MediaPlayerView? = null
    // 音乐播放器工具栏视图
    private var m_MediaPlayerBarView : MediaPlayerBarView? = null

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

        // 播放器视图 以及 播放器工具栏视图
        m_MediaPlayerView = MediaPlayerView(findViewById(R.id.media_player_view))
        m_MediaPlayerBarView = MediaPlayerBarView(findViewById(R.id.media_player_bar_view))
    }

    // 绑定数据用的 数据更新时调用
    override fun onBindView() {

        // 播放器底部行为布局
        val layout : FrameLayout = findViewById(R.id.media_player_bottom_sheet_behavior)

        // 底部表单行为
        val bottomSheetBehavior : CustomBottomSheetBehavior<FrameLayout> = CustomBottomSheetBehavior.from(layout)
        bottomSheetBehavior.skipAnchored = false
        bottomSheetBehavior.allowUserDragging = true

        val dm : DisplayMetrics = resources.displayMetrics

        bottomSheetBehavior.anchorOffset = (dm.heightPixels * 0.75F).toInt()
        bottomSheetBehavior.peekHeight = getPeakHeight()
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

                    CustomBottomSheetBehavior.STATE_DRAGGING -> {
                        multiSlidingUpPanel.isSlidingEnabled = false
                    }

                    else -> {
                        multiSlidingUpPanel.isSlidingEnabled = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if(m_MediaPlayerView != null)
                    m_MediaPlayerView!!.onSliding(slideOffset, MediaPlayerView.STATE_PARTIAL)
                if(m_MediaPlayerBarView != null)
                    m_MediaPlayerBarView!!.onSliding(slideOffset, MediaPlayerBarView.STATE_PARTIAL)
            }
        })
    }

    override fun onPanelStateChanged(panelState: Int) {
        if(this.m_MediaPlayerBarView != null)
            this.m_MediaPlayerBarView!!.onPanelStateChanged(panelState)
        if(this.m_MediaPlayerView != null)
            this.m_MediaPlayerView!!.onPanelStateChanged(panelState)
    }

    override fun onSliding(panel: IPanel<View>, top: Int, dy: Int, slidingOffset: Float) {
        super.onSliding(panel, top, dy, slidingOffset)

        if(this.m_MediaPlayerView != null)
            this.m_MediaPlayerView!!.onSliding(slidingOffset, MediaPlayerView.STATE_NORMAL)
        if(this.m_MediaPlayerBarView != null)
            this.m_MediaPlayerBarView!!.onSliding(slidingOffset, MediaPlayerBarView.STATE_NORMAL)
    }

    fun onMetadataChanged(metadata: MediaMetadata?) {
        if(this.m_MediaPlayerBarView != null)
            this.m_MediaPlayerBarView!!.onMetadataChanged(metadata)
        if(this.m_MediaPlayerView != null)
            this.m_MediaPlayerView!!.onMetadataChanged(metadata)

        val bitmap : Bitmap? = metadata?.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART)
    }

    fun onPlaybackStateChanged(state: PlaybackState?) {
        if(state?.state == PlaybackState.STATE_PLAYING || state?.state == PlaybackState.STATE_PAUSED) {
            if(this.panelState == MultiSlidingUpPanelLayout.HIDDEN)
                this.collapsePanel()
        }

        if(this.m_MediaPlayerBarView != null)
            this.m_MediaPlayerBarView!!.onPlaybackStateChanged(state)
        if(this.m_MediaPlayerView != null)
            this.m_MediaPlayerView!!.onPlaybackStateChanged(state)
    }
}