package com.androidcourse.musicplayerdemo.ui.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import com.androidcourse.musicplayerdemo.R
import com.androidcourse.musicplayerdemo.glide.audiocover.AudioFileCover
import com.androidcourse.musicplayerdemo.ui.adapters.models.BaseRecyclerViewItem
import com.androidcourse.musicplayerdemo.ui.adapters.models.SongRecyclerViewItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

// itemView 对应 item_song_layout_sample.xml
class SongViewHolder(itemView : View, viewType: ViewType)
    : BaseViewHolder(itemView, viewType) {

    override fun onBindViewHolder(viewItem: BaseRecyclerViewItem?) {
        val item : SongRecyclerViewItem = viewItem as SongRecyclerViewItem

        val title : TextView = findViewById(R.id.item_song_title)
        val imageView : ImageView = findViewById(R.id.item_song_art)

        title.setText(viewItem.m_Title)
        Glide.with(itemView.context)
            .load(AudioFileCover(item.getFilePath()))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imageView)
    }

    fun <T : View?> findViewById(@IdRes id: Int): T {
        return this.itemView.findViewById(id)
    }
}