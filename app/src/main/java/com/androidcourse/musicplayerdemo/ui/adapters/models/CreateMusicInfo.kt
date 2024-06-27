package com.androidcourse.musicplayerdemo.ui.adapters.models

data class CreateMusicInfo(
    val prompt: String,
    val tags: String,
    val title: String,
    val make_instrumental:String,
    val isCustom:String) {}