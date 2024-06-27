package com.androidcourse.musicplayerdemo.service

import android.util.Log
import okhttp3.Callback
import com.androidcourse.musicplayerdemo.ui.MediaPlayerThread.Companion.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

object ApiRequest {

    private val okHttpClient = OkHttpClient()

    suspend fun apiRequest(request: Request): String {
        return withContext(Dispatchers.IO) {
            val response = okHttpClient.newCall(request).execute()
            response.body?.string() ?: ""
        }
    }

    private const val TAG = "ApiRequest"
}
