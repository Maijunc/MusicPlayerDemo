package com.androidcourse.mediaplayer

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaDescription
import android.media.MediaMetadata
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import com.realgear.mediaplayer.model.Song
import java.io.ByteArrayInputStream
import java.util.TreeMap


// 与数据库进行交互
class LibraryManager {

    companion object {
        // 用于指定从数据库检索数据时返回的列
        private val CURSOR_PROJECTION =
            arrayOf("_id", "artist", "album", "title", "duration", "_display_name", "_data", "_size")

        // 用来获取设备上的音乐文件列表
        fun getSongs(context: Context): List<Song> {
            val cursor = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                CURSOR_PROJECTION,
                "is_music != 0",
                null,
                "_display_name ASC"
            )
            val result: MutableList<Song> = ArrayList()
            val startTime = System.currentTimeMillis()
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = getCursorStringByIndex(cursor, "_id").toInt()
                    var artistName = getCursorStringByIndex(cursor, "artist")
                    val albumName = getCursorStringByIndex(cursor, "album")
                    val title = getCursorStringByIndex(cursor, "title")
                    var displayName = getCursorStringByIndex(cursor, "_display_name")
                    val data = getCursorStringByIndex(cursor, "_data")
                    val duration = getCursorLongByIndex(cursor, "duration")
                    if (artistName == null || artistName.isEmpty()) {
                        artistName = "<no_name>"
                    }
                    if (displayName.contains("AUD-") && !title.isEmpty()) displayName = title
                    result.add(
                        Song(
                            id.toLong(),
                            title,
                            duration,
                            data,
                            albumName,
                            artistName,
                            displayName
                        )
                    )
                }
            }
            cursor?.close()
            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime // This is just to determine how fast the songs are gathered from the device
            return result
        }

        // 生成歌曲列表对应的map
        fun getTreemapOfSongs(songs: List<Song>): TreeMap<Int, Song> {
            val results = TreeMap<Int, Song>()
            for (song in songs) {
                results[song.id.toInt()] = song
            }
            return results
        }

        fun getMediaDescription(song: Song?): MediaDescription? {
            if (song == null)
                return null

            val builder = MediaDescription.Builder()
            builder.setMediaId(song.id.toString())
            builder.setIconBitmap(getSongArt(song))
            builder.setTitle(song.title)
            builder.setSubtitle(song.artistName)
            builder.setDescription(song.title)
            return builder.build()
        }

        fun getMediaMetadata(song: Song?): MediaMetadata? {
            if (song == null) return null
            val builder = MediaMetadata.Builder()
            builder.putString(MediaMetadata.METADATA_KEY_MEDIA_ID, song.id.toString())
            builder.putString(MediaMetadata.METADATA_KEY_ALBUM, song.albumName)
            builder.putString(MediaMetadata.METADATA_KEY_ARTIST, song.artistName)
            builder.putString(MediaMetadata.METADATA_KEY_TITLE, song.title)
            builder.putString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE, song.displayName)
            builder.putLong(MediaMetadata.METADATA_KEY_DURATION, song.duration)
            val songArt = getSongArt(song)
            if (songArt != null) {
                builder.putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, songArt)
            }
            return builder.build()
        }

        // 从数据库查询的结果中提取特定列的Long值
        private fun getCursorLongByIndex(cursor: Cursor, columnName: String) : Long {
            val index = cursor.getColumnIndex(columnName)
            return if (index > -1) cursor.getLong(index) else -1L
        }

        // 从数据库查询的结果中提取特定列的String值
        private fun getCursorStringByIndex(cursor: Cursor, columnName: String): String {
            val index = cursor.getColumnIndex(columnName)
            return if (index > -1) cursor.getString(index) else ""
        }

        private fun getSongArt(song: Song): Bitmap? {
            try {
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(song.data)
                val data = retriever.embeddedPicture

                if (data != null && data.size > 0) {
                    val bitmap : Bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                    return bitmap
                }
            }
            catch (ignore : Exception) {}

            return null
        }
    }
}