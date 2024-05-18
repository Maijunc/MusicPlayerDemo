package com.androidcourse.musicplayerdemo

import android.os.Bundle
import android.widget.Adapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.androidcourse.musicplayerdemo.views.panels.RootMediaPlayerPanel
import com.androidcourse.musicplayerdemo.views.panels.RootNavigationBarPanel
import com.realgear.multislidinguppanel.MultiSlidingPanelAdapter
import com.realgear.multislidinguppanel.MultiSlidingUpPanelLayout
import com.realgear.multislidinguppanel.PanelStateListener


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val panelLayout : MultiSlidingUpPanelLayout = findViewById(R.id.root_sliding_up_panel)

        val items: MutableList<Class<*>> = ArrayList()

        // You add your panels here it can be different classes with different layouts
        // but they all should extend the BasePanelView class with same constructors
        // You can add 1 or more then 1 panels

        items.add(RootMediaPlayerPanel::class.java)
        items.add(RootNavigationBarPanel::class.java)

        // This is to listen on all the panels you can add methods or extend the class

        // This is to listen on all the panels you can add methods or extend the class
        panelLayout.setPanelStateListener(object : PanelStateListener(panelLayout) {})
        // The adapter handles the items you can also extend it but I don't recommend for
        // beginners

        // The adapter handles the items you can also extend it but I don't recommend for
        // beginners
        panelLayout.setAdapter(object : MultiSlidingPanelAdapter(this as AppCompatActivity, items) {})

    }
}