package com.androidcourse.mediaplayer.interfaces

interface IPlayerCallback {
    fun onClickPlay(queueIndex : Int, queue : List<Int>)

    fun onClickPlayIndex(index : Int)

    fun onClickPlayNext()

    fun onClickPlayPrev()

    fun onClickPause()

    fun onSetSeekbar(position : Int)

    fun onUpdateQueue(queue : List<Int>, queueIndex : Int)

    fun onDestroy()
}