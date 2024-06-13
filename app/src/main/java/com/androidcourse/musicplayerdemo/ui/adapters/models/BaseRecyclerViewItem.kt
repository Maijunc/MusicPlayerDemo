package com.androidcourse.musicplayerdemo.ui.adapters.models

// 基础的RecyclerViewItem
abstract class BaseRecyclerViewItem(title : String, itemType : ItemType, duration : Long, artist : String) {

    enum class ItemType {
        SONG
    }

    private val m_Title : String

    val m_ItemType : ItemType

    private val m_ArtistName : String

    private val m_Duration : Long

    init {
        m_Title = title
        m_ItemType = itemType
        m_ArtistName = artist
        m_Duration = duration
    }

    fun getTitle() : String {
        return this.m_Title
    }

    fun getArtistName() : String {
        return this.m_ArtistName
    }

    abstract fun getHashCode() : Int
}