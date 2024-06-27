package com.androidcourse.musicplayerdemo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.androidcourse.musicplayerdemo.R

class FragmentHome() : Fragment() {

    private lateinit var m_RootView : View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.m_RootView = inflater.inflate(R.layout.fragment_home, container, false)

        return this.m_RootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun <T : View> findViewById(@IdRes id : Int) : T {
        return this.m_RootView.findViewById(id)
    }
}

/*
    *** params ***
    * 关于Fragment
    Fragment不能独立存在，必须嵌入到Activity中
    Fragment具有自己的生命周期，接收它自己的事件，并可以在Activity运行时被添加或删除
    Fragment的生命周期直接受所在的Activity的影响。如：当Activity暂停时，它拥有的所有Fragment们都暂停
    * 关于LayoutInflater和findViewById的区别
    LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化
    findViewById()是找xml布局文件下的具体widget控件(如 Button、TextView等)
    ** 具体作用
    对于一个没有被载入或者想要动态载入的界面，都需要使用LayoutInflater.inflate()来载入
    对于一个已经载入的界面，就可以使用Activity.findViewById()方法来获得其中的界面元素。
*/