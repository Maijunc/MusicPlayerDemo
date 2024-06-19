package com.androidcourse.musicplayerdemo.ui.adapters.viewholders

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.androidcourse.musicplayerdemo.R
import com.androidcourse.musicplayerdemo.glide.audiocover.AudioFileCover
import com.androidcourse.musicplayerdemo.ui.adapters.BaseRecyclerViewAdapter
import com.androidcourse.musicplayerdemo.ui.adapters.models.BaseRecyclerViewItem
import com.androidcourse.musicplayerdemo.ui.adapters.models.SongRecyclerViewItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


// itemView 对应 item_song_layout_sample.xml
class SongViewHolder(itemView : View)
    : BaseViewHolder(itemView) {
    private val m_RootView : LinearLayout

    private val m_ImageView_Parent : ConstraintLayout

    private val m_TestView_Title : TextView

    private val m_TextView_Artist : TextView
    private val m_ImageView_Art : ImageView


    init {
        m_RootView = findViewById(R.id.item_root_view)
        m_ImageView_Parent = findViewById(R.id.item_song_art_image_view_parent)

        m_TestView_Title = findViewById(R.id.item_song_title_text_view)
        m_TextView_Artist = findViewById(R.id.item_song_artist_text_view)

        m_ImageView_Art = findViewById(R.id.item_song_art_image_view)
    }
    override fun onInitializeView(viewType: BaseRecyclerViewAdapter.ViewType) {
        when (viewType) {
            BaseRecyclerViewAdapter.ViewType.LIST -> {
                m_RootView.orientation = LinearLayout.HORIZONTAL
                m_ImageView_Parent.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, itemView.resources.getDimensionPixelSize(R.dimen.item_library_song_art_size))
            }
            BaseRecyclerViewAdapter.ViewType.GRID -> {
                this.m_RootView.orientation = LinearLayout.VERTICAL
                this.m_ImageView_Parent.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
        }
    }

    override fun onBindViewHolder(viewItem: BaseRecyclerViewItem?) {
        val item : SongRecyclerViewItem = viewItem as SongRecyclerViewItem

        this.m_TestView_Title.text = viewItem.getTitle()
        this.m_TextView_Artist.text = viewItem.getArtistName()

        Log.i("SongViewHolder", "filepath:" + viewItem.getFilePath())

        // 从图片路径获取图像并加载到imageView中
        Glide.with(itemView.context)
            .load(AudioFileCover(item.getFilePath()))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(com.androidcourse.icons_pack.R.drawable.album_24px)
            .error(com.androidcourse.icons_pack.R.drawable.album_24px) // 设置错误占位图
            .into(this.m_ImageView_Art)
    }
}