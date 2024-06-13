package com.androidcourse.mediaplayer.statics

class ClassManager private constructor(activity : Class<*>) {

    companion object {
        private var _instance : ClassManager? = null

        fun getActivity() : Class<*> {
            if(_instance == null)
                throw RuntimeException("Instance was not created")

            return _instance!!.m_Activity
        }

        fun init(activity : Class<*>) {
            // _instance 或 m_Activity 为空都不行
            if(_instance?.m_Activity == null) {
                _instance = ClassManager(activity)
            }
        }
    }

    private var m_Activity : Class<*>

    init {
        m_Activity = activity
    }
}