package com.realgear.mediaplayer.model

class Song(
    val id: Long,
    val title: String,
    val duration: Long,
    val data: String,
    val albumName: String,
    val artistName: String,
    val displayName: String
) {

    companion object {
        fun emptySong(): Song {
            return Song(-1, "", -1, "", "", "", "")
        }
    }
}