package com.androidcourse.musicplayerdemo.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.androidcourse.musicplayerdemo.ui.adapters.models.BaseRecyclerViewItem
import com.androidcourse.musicplayerdemo.ui.adapters.viewholders.BaseViewHolder

// 基础的Adapter
abstract class BaseRecyclerViewAdapter(items : MutableList<BaseRecyclerViewItem>)
    : RecyclerView.Adapter<BaseViewHolder>() {

    val m_Items : MutableList<BaseRecyclerViewItem>

    init {
        m_Items = items
    }

    override fun onBindViewHolder(holder : BaseViewHolder, position : Int) {
        holder.onBindViewHolder(m_Items.get(position))
    }

    // 获取当前item对应的itemCount
    override fun getItemCount(): Int {
        return m_Items.size
    }

    // 获取当前item对应的枚举值
    override fun getItemViewType(position: Int): Int {
        return m_Items[position].m_ItemType.ordinal
    }
}