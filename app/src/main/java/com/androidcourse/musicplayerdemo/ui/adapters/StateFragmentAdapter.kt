package com.androidcourse.musicplayerdemo.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class StateFragmentAdapter(fragmentManager : FragmentManager, lifecycle : Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle)
{
    private val mItems : MutableList<Fragment>
    private val mFragmentManager : FragmentManager

    init {
        mItems = mutableListOf()
        mFragmentManager = fragmentManager
    }

    fun addFragment(fragment : Fragment) {
        mItems.add(fragment)
    }

    fun getFragment(position: Int) : Fragment {
        return mItems.get(position)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun createFragment(position: Int): Fragment {
        return mItems.get(position)
    }
}