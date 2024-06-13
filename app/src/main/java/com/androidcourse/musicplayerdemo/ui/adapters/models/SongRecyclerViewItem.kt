package com.androidcourse.musicplayerdemo.ui.adapters.models

import com.realgear.mediaplayer.model.Song

class SongRecyclerViewItem(song : Song)
    : BaseRecyclerViewItem(song.title, ItemType.SONG, song.duration, song.artistName) {

    private val m_Item : Song

    init {
        m_Item = song
    }

    fun getSongId() : Long {
        return this.m_Item.id
    }

    fun getFilePath() : String {
        return m_Item.data
    }

    // 做切换动画效果用
    override fun getHashCode() : Int {
        var result : Int = this.m_Item.id.hashCode().toString().toInt()
        result = 31 * result * this.m_Item.title.hashCode()
        result = 31 * result * this.m_Item.data.hashCode()
        result = 31 * result * this.m_Item.data.hashCode()

        return result
    }
}