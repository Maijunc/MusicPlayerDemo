package com.androidcourse.musicplayerdemo.ui.adapters

import android.view.ViewGroup
import com.androidcourse.musicplayerdemo.ui.adapters.helpers.BaseViewHelper
import com.androidcourse.musicplayerdemo.ui.adapters.models.BaseRecyclerViewItem
import com.androidcourse.musicplayerdemo.ui.adapters.viewholders.BaseViewHolder
import com.androidcourse.musicplayerdemo.ui.adapters.viewholders.SongViewHolder




class LibraryRecyclerViewAdapter(items : MutableList<BaseRecyclerViewItem>)
    : BaseRecyclerViewAdapter(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        // Enum.values() 返回一个包含所有枚举常量的数组
        val itemType : BaseRecyclerViewItem.ItemType = BaseRecyclerViewItem.ItemType.values()[viewType]

        // 创建一个SongViewHolder，通过LayoutInflater指定布局文件item_song_layout_sample.xml, 对应的ViewType为LIST
        return when (itemType) {
            BaseRecyclerViewItem.ItemType.SONG -> BaseViewHelper.onCreateViewHolder(SongViewHolder::class.java, parent)

            else -> error("Unsupported item type: $itemType")
        }
    }
}