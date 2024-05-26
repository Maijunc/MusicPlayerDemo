package com.androidcourse.musicplayerdemo.ui.adapters.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.androidcourse.musicplayerdemo.ui.adapters.models.BaseRecyclerViewItem

// 基础的一个ViewHolder
abstract class BaseViewHolder(itemView: View, viewType : ViewType) :
    RecyclerView.ViewHolder(itemView) {
    enum class ViewType { // This viewtype will be used to decide which type of song layout was created list/grids
        LIST
    }

    private val m_ViewType : ViewType

    init {
        m_ViewType = viewType
    }

    abstract fun onBindViewHolder(viewItem: BaseRecyclerViewItem?)
}