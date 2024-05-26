package com.androidcourse.musicplayerdemo.glide.audiocover

// Audio的封面图像
class AudioFileCover(path : String) {
    val m_Path : String

    init {
        m_Path = path
    }

    override fun hashCode(): Int {
        return m_Path.hashCode()
    }

    // 通过路径来比较二者是否相等
    override fun equals(obj: Any?): Boolean {
        return if (obj !is AudioFileCover) false else obj.m_Path == this.m_Path
    }
}