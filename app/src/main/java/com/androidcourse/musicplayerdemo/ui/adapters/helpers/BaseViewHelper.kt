package com.androidcourse.musicplayerdemo.ui.adapters.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidcourse.musicplayerdemo.R
import com.androidcourse.musicplayerdemo.ui.adapters.viewholders.BaseViewHolder
import com.androidcourse.musicplayerdemo.ui.adapters.viewholders.SongViewHolder
import java.lang.reflect.Constructor


class BaseViewHelper {

    companion object {
        //
        private val m_LayoutIds: LinkedHashMap<Class<*>, Int> = LinkedHashMap()

        init {
            m_LayoutIds[SongViewHolder::class.java] = R.layout.item_library_song_view
        }

        private fun getLayoutId(viewHolder : Class<*>) : Int {
            return m_LayoutIds.getOrDefault(viewHolder, -1)
        }

        fun <T : BaseViewHolder> onCreateViewHolder(viewHolder: Class<*>, parent: ViewGroup) : T {
            try {
                val view : View =  LayoutInflater.from(parent.context).inflate(getLayoutId(viewHolder), parent, false)
                val constructor : Constructor<*> = viewHolder.getDeclaredConstructor(View::class.java)
                return constructor.newInstance(view) as T
            }
            catch (ex : Exception) {
                throw RuntimeException(ex)
            }
        }
    }

}