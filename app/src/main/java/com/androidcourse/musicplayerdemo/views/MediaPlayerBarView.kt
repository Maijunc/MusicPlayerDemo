package com.androidcourse.musicplayerdemo.views

import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.media.MediaMetadata
import android.media.session.PlaybackState
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.androidcourse.musicplayerdemo.R
import com.androidcourse.musicplayerdemo.ui.MediaPlayerThread
import com.google.android.material.progressindicator.LinearProgressIndicator
import org.w3c.dom.Text

class MediaPlayerBarView (rootView : View){

    companion object {
        const val STATE_NORMAL: Int = 0

        const val STATE_PARTIAL : Int = 1
    }


    private val m_RootView : View

    private var m_State : Int = 0

    // 背景板
    private val m_BackgroundView : FrameLayout
    // 进度条
    private val m_ProgressIndicator : LinearProgressIndicator
    // 控制盒
    private val m_ControlsContainer : ConstraintLayout

    private val m_ImageView_Art : ImageView
    private val m_TextView_SongTitle : TextView
    private val m_TextView_SongArtist : TextView
    private val m_ImageBtn_PlayPause : ImageButton

    private var m_PrevState : PlaybackState? = null

    init {
        this.m_RootView = rootView

        this.m_BackgroundView = findViewById(R.id.media_player_bar_bg)
        this.m_ControlsContainer = findViewById(R.id.media_player_bar_controls_container)
        this.m_ProgressIndicator = findViewById(R.id.media_player_bar_progress_indicator)

        // 相关的操作
        this.m_ImageView_Art = findViewById(R.id.image_view_album_art)
        this.m_TextView_SongTitle = findViewById(R.id.text_view_song_title)
        this.m_TextView_SongArtist = findViewById(R.id.text_view_song_artist)
        this.m_ImageBtn_PlayPause = findViewById(R.id.btn_play_pause)

        this.m_RootView.alpha = 1.0F

        this.onInit()
    }

    private fun onInit() {
        this.m_ImageBtn_PlayPause.setOnClickListener{
            MediaPlayerThread.getInstance()?.getCallback()?.onClickPlayPause()
        }
    }

    // slideOffset 滑动偏移量 alpha 是透明度的设置 1F 表示完全不透明 0表示完全透明
    fun onSliding(slideOffset : Float, state : Int) {
        val fadeStart : Float = 0.25F
        val alpha : Float = (slideOffset - fadeStart)

        //正常状态就显示
        if(state == STATE_NORMAL) {
            m_RootView.alpha = 1F - alpha
            m_BackgroundView.alpha = 1F
            m_ProgressIndicator.alpha = 1F
            m_ControlsContainer.alpha = 1F
        }
        else { //STATE_PARTIAL 部分显示
            m_RootView.alpha = alpha
            m_BackgroundView.alpha = 0F
            m_ProgressIndicator.alpha = 0F
            m_ControlsContainer.alpha = 1F
        }

        m_State = state
    }

    //为何这里要做一个转换呢 大概因为AppCompatActivity本质也是个实现了findViewById的view
    fun <T : View> findViewById(@IdRes id : Int) : T {
        return m_RootView.findViewById(id)
    }

    fun onPlaybackStateChanged(state : PlaybackState?) {
        if(state == null)
            return

        this.m_ProgressIndicator.progress = state.position.toInt()

        // 修改图标
        if(this.m_PrevState == null || this.m_PrevState!!.state != state.state)
            this.m_ImageBtn_PlayPause.setImageIcon(Icon.createWithResource(this.m_RootView.context,
                if(state.state == PlaybackState.STATE_PLAYING) com.androidcourse.icons_pack.R.drawable.pause_24px
                else com.androidcourse.icons_pack.R.drawable.play_arrow_24px ))
    }

    fun onMetadataChanged(metadata : MediaMetadata?) {
        if(metadata == null)
            return

        this.m_TextView_SongTitle.text = metadata.getText(MediaMetadata.METADATA_KEY_TITLE)
        this.m_TextView_SongArtist.text = metadata.getText(MediaMetadata.METADATA_KEY_ARTIST)

        val album_art : Bitmap? = metadata.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART)
        if(album_art != null) {
            this.m_ImageView_Art.setImageBitmap(album_art)
        } else {
            this.m_ImageView_Art.setImageDrawable(ResourcesCompat.getDrawable(this.m_RootView.resources, com.androidcourse.icons_pack.R.drawable.library_music_24px, this.m_RootView.context.theme))
        }

        this.m_ProgressIndicator.max = metadata.getLong(MediaMetadata.METADATA_KEY_DURATION).toInt()
    }

    fun onPanelStateChanged(panelState : Int) {

    }
}