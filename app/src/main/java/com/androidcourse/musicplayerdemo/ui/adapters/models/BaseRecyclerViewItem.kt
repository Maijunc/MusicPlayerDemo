package com.androidcourse.musicplayerdemo.ui.adapters.models

// 基础的RecyclerViewItem
abstract class BaseRecyclerViewItem(title : String, itemType : ItemType) {

    enum class ItemType {
        SONG
    }

    val m_Title : String

    val m_ItemType : ItemType

    init {
        m_Title = title
        m_ItemType = itemType
    }

    abstract fun getHashCode() : Int
}