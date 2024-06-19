package com.androidcourse.mediaplayer.interfaces

import android.media.session.PlaybackState
import com.realgear.mediaplayer.model.Song

interface IPlaybackCallback {

    fun onPlaybackStateChange (playbackState : PlaybackState)

    fun onUpdateMetadata(song : Song?)


    fun onSetRepeatType(repeatType: Int)
}