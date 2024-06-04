package com.androidcourse.musicplayerdemo.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.androidcourse.musicplayerdemo.ui.adapters.models.BaseRecyclerViewItem
import com.androidcourse.musicplayerdemo.ui.adapters.viewholders.BaseViewHolder

// 基础的Adapter
abstract class BaseRecyclerViewAdapter(items : MutableList<BaseRecyclerViewItem>)
    : RecyclerView.Adapter<BaseViewHolder>() {

    enum class ViewType {
        LIST, GRID
    }

    val m_Items : MutableList<BaseRecyclerViewItem>

    init {
        m_Items = items
    }

    private lateinit var m_LayoutViewType : ViewType

    // 绑定数据
    override fun onBindViewHolder(holder : BaseViewHolder, position : Int) {
        holder.onInitializeView(m_LayoutViewType)
        holder.onBindViewHolder(m_Items[position])
    }

    fun setAdapterViewType(viewType : ViewType) {
        m_LayoutViewType = viewType
    }

    // 获取当前item对应的itemCount
    override fun getItemCount(): Int {
        return m_Items.size
    }

    override fun getItemId(position: Int): Long {
        return this.m_Items[position].getHashCode().toLong()
    }

    // 获取当前item对应的枚举值
    override fun getItemViewType(position: Int): Int {
        // ordinal 返回此枚举常量的序数
        return m_Items[position].m_ItemType.ordinal
    }

    fun getViewType() : ViewType {
        return m_LayoutViewType
    }
}