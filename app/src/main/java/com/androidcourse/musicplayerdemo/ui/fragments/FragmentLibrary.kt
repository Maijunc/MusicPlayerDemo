package com.androidcourse.musicplayerdemo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.androidcourse.mediaplayer.LibraryManager
import com.androidcourse.musicplayerdemo.R
import com.androidcourse.musicplayerdemo.ui.adapters.BaseRecyclerViewAdapter
import com.androidcourse.musicplayerdemo.ui.adapters.LibraryRecyclerViewAdapter
import com.androidcourse.musicplayerdemo.ui.adapters.models.BaseRecyclerViewItem
import com.androidcourse.musicplayerdemo.ui.adapters.models.SongRecyclerViewItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.realgear.extensions.gridlayout.GridLayoutManagerExtended
import com.realgear.mediaplayer.model.Song


class FragmentLibrary() : Fragment() {

    private lateinit var m_RootView : View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.m_RootView = inflater.inflate(R.layout.fragment_library, container, false)



        return this.m_RootView
    }

    private lateinit var m_LibraryRecyclerView : RecyclerView

    private lateinit var m_LibraryAdapter : BaseRecyclerViewAdapter

    private var m_GridLayout : GridLayoutManagerExtended? = null

    // 加载Library中的内容
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.m_LibraryRecyclerView = findViewById(R.id.library_recyclerView)

        val panelheights : Int = resources.getDimensionPixelSize(R.dimen.navigation_bar_height) + resources.getDimensionPixelSize(R.dimen.mediaplayerbar_height)
        m_LibraryRecyclerView.setPadding(0,0,0,panelheights)

// requireContext() 封装了一下context 防止空值
        val songs : MutableList<Song> = LibraryManager.getSongs(requireContext()).toMutableList()
        val items : MutableList<BaseRecyclerViewItem> = ArrayList()

        for (song in songs) {
            items.add(SongRecyclerViewItem(song))
        }

        this.m_LibraryAdapter = LibraryRecyclerViewAdapter(items)
        this.m_LibraryAdapter.setHasStableIds(true)
        setAdapterViewType(BaseRecyclerViewAdapter.ViewType.GRID)
        // bind the adapter and set the layout to the recyclerView
        this.m_LibraryRecyclerView.adapter = this.m_LibraryAdapter

        val btn : FloatingActionButton = findViewById(R.id.btn_test_layout)
        btn.setOnClickListener {
            setAdapterViewType(
                if(this.m_LibraryAdapter.getViewType() == BaseRecyclerViewAdapter.ViewType.GRID)
                BaseRecyclerViewAdapter.ViewType.LIST
                else BaseRecyclerViewAdapter.ViewType.GRID)

                val songsNew : List<Song> = LibraryManager.getSongs(requireContext())
                var cnt = 0
                val pos = songs.size - 1
                if(songsNew.size != items.size) {
                    items.clear()
                    for (song in songsNew)
                        items.add(SongRecyclerViewItem(song))
                    m_LibraryAdapter.notifyDataSetChanged()
                }
        }
    }

    private fun setAdapterViewType(viewType : BaseRecyclerViewAdapter.ViewType) {
        this.m_LibraryAdapter.setAdapterViewType(viewType)

        var rowCount = if(viewType == BaseRecyclerViewAdapter.ViewType.LIST)1 else 3

        if(m_GridLayout == null) {
            m_GridLayout = GridLayoutManagerExtended(requireContext(), rowCount)
            m_LibraryRecyclerView.layoutManager = this.m_GridLayout
        } else {
            this.m_GridLayout!!.spanCount = rowCount
            this.m_LibraryAdapter.notifyDataSetChanged()
        }
    }

    fun <T : View> findViewById(@IdRes id : Int) : T {
        return this.m_RootView.findViewById(id)
    }
}