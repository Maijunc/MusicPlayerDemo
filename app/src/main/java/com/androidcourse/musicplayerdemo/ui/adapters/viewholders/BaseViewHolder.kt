package com.androidcourse.musicplayerdemo.ui.adapters.viewholders

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.androidcourse.musicplayerdemo.ui.adapters.BaseRecyclerViewAdapter
import com.androidcourse.musicplayerdemo.ui.adapters.models.BaseRecyclerViewItem

// 基础的一个ViewHolder
abstract class BaseViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    abstract fun onInitializeView(viewType: BaseRecyclerViewAdapter.ViewType)
    abstract fun onBindViewHolder(viewItem: BaseRecyclerViewItem?)

    // 封装一下，这样就不用itemView.findViewById了
    fun <T : View?> findViewById(@IdRes id: Int): T {
        return this.itemView.findViewById(id)
    }
}