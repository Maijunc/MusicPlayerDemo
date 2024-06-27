package com.androidcourse.musicplayerdemo.ui.adapters

import android.app.AlertDialog
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidcourse.musicplayerdemo.R
import com.androidcourse.musicplayerdemo.ui.adapters.models.AIHistoryRecyclerViewItem
import com.androidcourse.musicplayerdemo.utils.download.DownLoadUtil
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AIHistoryRecyclerViewAdapter(private val AIHistory: ArrayList<AIHistoryRecyclerViewItem>) :
    RecyclerView.Adapter<AIHistoryRecyclerViewAdapter.AISongViewHolder>() {
    inner class AISongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val Image:ImageView = itemView.findViewById(R.id.ai_history_image)
        val title:TextView = itemView.findViewById(R.id.ai_history_title)
        val id:TextView = itemView.findViewById(R.id.ai_history_id)
        var audiourl:String = ""
        var Title_text:String = ""

        init{
            // 设置长按监听器
            itemView.setOnLongClickListener {
                // 处理长按事件
                // 这里可以根据需要进行逻辑处理，比如显示上下文菜单等
                //Log.i("MyActivity","这是个长按事件")  //长按事件测试
                // 假如用户长按了某一首歌，弹出询问框询问是否下载
                ShowDownloadDialog(itemView)
                // 返回 true 表示事件已经被处理，不会继续传递给其他监听器
                true
            }
        }
    }

    fun ShowDownloadDialog(itemView: View){
        val holder = itemView.tag as? AISongViewHolder ?: return

        val builder = AlertDialog.Builder(itemView.context)
        val DownloadFactory = DownLoadUtil(itemView.context)
        builder.setTitle("下载确认")
            .setMessage("您确定要下载这首歌吗？")
            .setPositiveButton("确定") { dialog, which ->
                // 在这里执行下载文件的操作，或者调用下载方法
                DownloadFactory.downloadFile(holder.Title_text,holder.audiourl)
            }
            .setNegativeButton("取消") { dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AISongViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_ai_history_view,parent,false)
        return AISongViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return AIHistory.size
    }

    override fun onBindViewHolder(holder: AISongViewHolder, position: Int) {
        //绑定数据
        val thesong: AIHistoryRecyclerViewItem = AIHistory[position]

        holder.title.text = thesong.title
        holder.Title_text = thesong.title
        holder.id.text = thesong.id
        holder.audiourl = thesong.audiourl

        // 设置 ViewHolder 对象为 itemView 的标签
        holder.itemView.tag = holder

        var image: Bitmap? = null

        val request = Request.Builder()
            .url(thesong.imageurl)
            .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i(ContentValues.TAG, "onFailure$e")
            }

            override fun onResponse(call: Call, response: Response) {
                //获取响应主体的字符串表示
                val responseBody = response.body?.byteStream()
                try{
                    //先对string的内容进行base64解码
                    //val imageDataByte = Base64.decode(responseBody, Base64.DEFAULT)

                    //然后想解码出来的字节数组解码为bitmap
                    image = BitmapFactory.decodeStream(responseBody)
                    //至此，已经获得了对应图片的bitmap，我们就更新图片组件
                    holder.Image.setImageBitmap(image)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }
}

