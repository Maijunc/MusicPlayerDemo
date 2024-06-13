package com.androidcourse.mediaplayer.statics

class IntentFields {
    // Media Notification Constants

    companion object {
        val ACTION_NEXT : String = "com.androidcourse.musicplayerdemo.NEXT"
        val ACTION_PREV : String = "com.androidcourse.musicplayerdemo.PREV"
        val ACTION_PAUSE : String = "com.androidcourse.musicplayerdemo.PAUSE"
        val ACTION_PLAY : String = "com.androidcourse.musicplayerdemo.PLAY"
        val ACTION_FAVOURITE : String = "com.example.softmusic_beta.FAVOURITE";
        val CHANNEL_ID : String = "com.androidcourse.musicplayerdemo"

        // Broadcast Constants
        val  EXTRA_REPEAT_STATE : String = "REPEAT_STATE"
        val EXTRA_TRACKS_QUEUE : String = "TRACKS_QUEUE"
        val EXTRA_TRACK_ID : String = "TRACKS_ID"
        val EXTRA_TRACK_INDEX : String = "TRACK_INDEX"
        val EXTRA_SEEK_BAR_POSITION = "SEEK_BAR_POSITION"

        // Media Player Service Constants

        val INTENT_ADD_TRACK_NEXT : String = "com.androidcourse.musicplayerdemo.service.MediaPlayerService.ADD_TRACK_NEXT"
        val INTENT_CHANGE_REPEAT : String = "com.androidcourse.musicplayerdemo.service.MediaPlayerService.CHANGE_REPEAT"
        val INTENT_PLAY : String = "com.androidcourse.musicplayerdemo.service.MediaPlayerService.PLAY"
        val INTENT_PLAY_INDEX : String = "com.androidcourse.musicplayerdemo.service.MediaPlayerService.PLAY_INDEX"
        val INTENT_PLAY_PAUSE : String = "com.androidcourse.musicplayerdemo.service.MediaPlayerService.PLAY_PAUSE"
        val INTENT_PLAY_PREV : String = "com.androidcourse.musicplayerdemo.service.MediaPlayerService.PLAY_PREV"
        val INTENT_PLAY_NEXT : String = "com.androidcourse.musicplayerdemo.service.MediaPlayerService.PLAY_NEXT"
        val INTENT_SET_SEEKBAR : String = "com.androidcourse.musicplayerdemo.service.MediaPlayerService.SET_SEEKBAR"
        val INTENT_UPDATE_QUEUE : String = "com.androidcourse.musicplayerdemo.service.MediaPlayerService.UPDATE_QUEUE"

    }

}