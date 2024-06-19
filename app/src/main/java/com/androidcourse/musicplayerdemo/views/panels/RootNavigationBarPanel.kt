package com.androidcourse.musicplayerdemo.views.panels

import android.content.Context
import android.view.LayoutInflater
import androidx.viewpager2.widget.ViewPager2
import com.androidcourse.musicplayerdemo.R
import com.androidcourse.musicplayerdemo.ui.adapters.StateFragmentAdapter
import com.androidcourse.musicplayerdemo.ui.fragments.FragmentAI
import com.androidcourse.musicplayerdemo.ui.fragments.FragmentHome
import com.androidcourse.musicplayerdemo.ui.fragments.FragmentLibrary
import com.androidcourse.musicplayerdemo.ui.fragments.FragmentSettings
import com.realgear.multislidinguppanel.BasePanelView
import com.realgear.multislidinguppanel.MultiSlidingUpPanelLayout
import com.realgear.readable_bottom_bar.ReadableBottomBar

class RootNavigationBarPanel (context : Context, panelLayout : MultiSlidingUpPanelLayout)
    : BasePanelView(context, panelLayout) {

    //
    private lateinit var rootViewPager : ViewPager2

    // 底部导航栏
    private lateinit var rootNavigationBar : ReadableBottomBar

    init {
        // Context 应该是个 Singleton
        getContext().setTheme(R.style.Theme_MusicPlayerDemo)
        // 动态加载layout_root_navigation_bar.xml布局
        LayoutInflater.from(getContext()).inflate(R.layout.layout_root_navigation_bar, this, true)
    }

    override fun onCreateView() {
        // The panel will be collapsed(折叠) on start of application
        this.panelState = MultiSlidingUpPanelLayout.COLLAPSED

        // The panel will slide up and down(上下滑动）
        this.mSlideDirection = MultiSlidingUpPanelLayout.SLIDE_VERTICAL

        // Sets the panels peak height 设置峰值高度
        this.peakHeight = resources.getDimensionPixelSize(R.dimen.navigation_bar_height)

        rootViewPager = multiSlidingUpPanel.findViewById(R.id.root_view_pager)
        rootNavigationBar = findViewById(R.id.root_navigation_bar)
    }

    //列表项需要更新数据时，onBindView 方法会被调用
    override fun onBindView() {


        // 存储导航栏的Adapter
        val adapter = StateFragmentAdapter(supportFragmentManager, lifecycle)

        adapter.addFragment(FragmentHome::class.java)
        adapter.addFragment(FragmentLibrary::class.java)
        adapter.addFragment(FragmentAI::class.java)
        adapter.addFragment(FragmentSettings()::class.java)

        // 调用示范
        // adapter.addFragment(FragmentLibrary::class)
        // adapter.addFragment(FragmentLibrary::class.java, rootNavigationBar, rootViewPager)
        // val library : FragmentLibrary = adapter.getFragment(FragmentLibrary::class.java)

        rootViewPager.setAdapter(adapter)
        rootNavigationBar.setupWithViewPager2(rootViewPager)
    }

    override fun onPanelStateChanged(p0: Int) {
    }

}