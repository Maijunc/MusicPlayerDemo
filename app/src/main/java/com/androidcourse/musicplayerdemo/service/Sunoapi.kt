package com.androidcourse.musicplayerdemo.service

import com.androidcourse.musicplayerdemo.ui.adapters.models.AIHistoryRecyclerViewItem
import com.androidcourse.musicplayerdemo.ui.adapters.models.CreateMusicInfo
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray

class Sunoapi {
    private lateinit var m_Request:Request
    private lateinit var m_ResponseBody:String
    private lateinit var m_CreateInfo:CreateMusicInfo

    suspend fun getMusicInfo(): ArrayList<AIHistoryRecyclerViewItem>? {
        val apiUrl = "http://10.0.2.2:3000/api/get"
        m_Request = okhttp3.Request.Builder()
            .url(apiUrl)
            .get()
            .build()
        m_ResponseBody=ApiRequest.apiRequest(m_Request)
        val AIHistory = ArrayList<AIHistoryRecyclerViewItem>()
        try {
            val jsonArray = JSONArray(m_ResponseBody)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val id=jsonObject.getString("id")
                val title=jsonObject.getString("title")
                val imageurl=jsonObject.getString("image_url")
                val audiourl=jsonObject.getString("audio_url")
                val thesong = AIHistoryRecyclerViewItem(id,imageurl,audiourl,title)
                println("ID: $id\nTITLE:$title\nIMAGE:$imageurl\nAUDIO:$audiourl")
                AIHistory.add(thesong)
            }
            println("ID: $AIHistory")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return AIHistory
    }

    suspend fun createMusic(createInfo:CreateMusicInfo){
        m_CreateInfo=createInfo
        val json= setRequestJson()!!.trimIndent()
        val apiUrl=setApiUrl()
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(mediaType)

        val request = okhttp3.Request.Builder()
            .url(apiUrl)
            .post(requestBody)
            .build()
        m_ResponseBody=ApiRequest.apiRequest(request)
        println(m_ResponseBody)
    }

    private fun setRequestJson(): String? {
        val json = """
            {
                "wait_audio": false
            }"""

        // 使用 Gson 解析 JSON 字符串为一个 Map
        val gson = Gson()
        val jsonMap = gson.fromJson(json, Map::class.java).toMutableMap()

        // 添加新字段
        if(m_CreateInfo.isCustom=="true"){
            jsonMap["prompt"] = m_CreateInfo.prompt
            jsonMap["tags"] = m_CreateInfo.prompt
            jsonMap["title"] = m_CreateInfo.prompt
            jsonMap["make_instrumental"] = m_CreateInfo.make_instrumental
        }else{
            jsonMap["prompt"] = m_CreateInfo.prompt
            jsonMap["make_instrumental"] = m_CreateInfo.make_instrumental
        }

        val modifiedJsonString = gson.toJson(jsonMap)
        println(modifiedJsonString)
        return modifiedJsonString
    }

    private fun setApiUrl(): String {
        return if(m_CreateInfo.isCustom=="true"){
            "http://10.0.2.2:3000/api/generate"
        }else{
            "http://10.0.2.2:3000/api/custom_generate"
        }
    }
}
//    格式化JSON数据
//    private fun formatJson(jsonString: String?): String {
//        return try {
//            val gson = GsonBuilder().setPrettyPrinting().create()
//            val jsonElement = JsonParser.parseString(jsonString)
//            gson.toJson(jsonElement)
//        } catch (e: JsonParseException) {
//            "Invalid JSON format"
//        }
//    }