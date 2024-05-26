package com.androidcourse.musicplayerdemo.ui.adapters.models

import com.realgear.mediaplayer.model.Song

class SongRecyclerViewItem(song : Song)
    : BaseRecyclerViewItem(song.title, ItemType.SONG) {

    private val m_Item : Song

    init {
        m_Item = song
    }

    fun getFilePath() : String {
        return m_Item.data
    }
}