package com.androidcourse.musicplayerdemo.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.lang.reflect.Constructor


class StateFragmentAdapter(fragmentManager : FragmentManager, lifecycle : Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle)
{
    // 这里做了一个映射
    // 为了可以在getFragment的时候可以通过Fragment的class类型直接找到对应的Fragment
    // 而不用通过序号来找，用序号来找十分麻烦而且可读性很差
    // 直接用拉链哈希表
    private val m_Items : LinkedHashMap<Class<*>, Fragment>

    private val m_FragmentManager : FragmentManager

    init {
        m_Items = LinkedHashMap()
        m_FragmentManager = fragmentManager
    }

    // 后面的params是构造fragment用的参数
    // 添加构造函数带参数的Fragment
    fun addFragment(fragmentType: Class<*>, vararg params: Any) {
        try {
            if (params.isNotEmpty()) {
                val classes = Array<Class<*>>(params.size) { i -> params[i]::class.java }
                val constructor = fragmentType.getDeclaredConstructor(*classes)
                val fragment = constructor.newInstance(*params) as Fragment
                m_Items[fragmentType] = fragment
            } else {
                addFragment(fragmentType)
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }


    fun addFragment(fragmentType : Class<*>) {
        try {
            val constructor : Constructor<*> = fragmentType.getDeclaredConstructor()
            val fragment: Fragment = constructor.newInstance() as Fragment
            m_Items[fragmentType] = fragment
        }
        catch (ex : Exception) {
            throw RuntimeException(ex)
        }
    }

    fun addFragment(fragment : Any) {
        try {
            if(fragment !is Fragment)
                throw RuntimeException("Not instance of Fragment")

            val clazz : Class<*> = Class.forName(fragment.javaClass.name)
            m_Items[clazz] = fragment
        }
        catch (ex : Exception) {
            throw RuntimeException(ex)
        }
    }

    fun getFragment(position: Int) : Fragment {
        return m_Items.values.toTypedArray()[position]
    }

    fun <T : Fragment> getFragment(fragmentType : Class<T>) : T {
        return m_Items.get(fragmentType) as T
    }

    override fun createFragment(position: Int): Fragment {
        return m_Items.values.toTypedArray()[position]
    }
    override fun getItemCount(): Int {
        return m_Items.size
    }
}