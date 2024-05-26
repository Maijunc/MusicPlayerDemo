package com.androidcourse.musicplayerdemo.glide.audiocover

import android.media.MediaMetadataRetriever
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream

// DataFetcher 用于定义如何获取图像数据
class AudioFileCoverFetcher(private val model: AudioFileCover) : DataFetcher<InputStream> {

    private val m_Model : AudioFileCover

    // 输入流
    private var m_Stream : InputStream?

    init {
        m_Model = model
        m_Stream = null
    }

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        // 提取元数据
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(model.m_Path)
            val data = retriever.embeddedPicture
            if (data != null && data.size > 0)
                m_Stream = ByteArrayInputStream(data)
            callback.onDataReady(m_Stream)
        } catch (ex: Exception) {
            callback.onLoadFailed(ex)
        } finally {
            try {
                retriever.release()
            } catch (ignore: IOException) {}
        }
    }

    override fun cleanup() {
        if (m_Stream != null) {
            try {
                m_Stream!!.close()
            }
            catch (ignore : Exception) {}
        }
    }

    override fun cancel() {
        // Can't be cancel.
    }

    override fun getDataClass(): Class<InputStream> {
        return InputStream::class.java
    }

    override fun getDataSource(): DataSource {
        return DataSource.LOCAL
    }
}