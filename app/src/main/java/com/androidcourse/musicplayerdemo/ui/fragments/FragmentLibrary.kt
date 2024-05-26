package com.androidcourse.musicplayerdemo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidcourse.mediaplayer.LibraryManager
import com.androidcourse.musicplayerdemo.R
import com.androidcourse.musicplayerdemo.ui.adapters.LibraryRecyclerViewAdapter
import com.androidcourse.musicplayerdemo.ui.adapters.models.BaseRecyclerViewItem
import com.androidcourse.musicplayerdemo.ui.adapters.models.SongRecyclerViewItem
import com.realgear.mediaplayer.model.Song


class FragmentLibrary() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View =  inflater.inflate(R.layout.fragment_library, container, false)

        val recyclerView : RecyclerView = view.findViewById(R.id.library_recyclerView)

        val panelheights : Int = resources.getDimensionPixelSize(R.dimen.navigation_bar_height) + resources.getDimensionPixelSize(R.dimen.mediaplayerbar_height)
        recyclerView.setPadding(0,0,0,panelheights)

        // requireContext() 封装了一下context 防止空值
        val songs : List<Song> = LibraryManager.getSongs(requireContext())
        val items : MutableList<BaseRecyclerViewItem> = ArrayList()

        for (song in songs) {
            items.add(SongRecyclerViewItem(song))
        }

        val adapter : LibraryRecyclerViewAdapter = LibraryRecyclerViewAdapter(items)

        // bind the adapter and set the layout to the recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        return view
    }

    // 加载Library中的内容
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}