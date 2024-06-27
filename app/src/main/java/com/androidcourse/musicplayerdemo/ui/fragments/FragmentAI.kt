package com.androidcourse.musicplayerdemo.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidcourse.musicplayerdemo.R
import com.androidcourse.musicplayerdemo.service.Sunoapi
import com.androidcourse.musicplayerdemo.ui.adapters.AIHistoryRecyclerViewAdapter
import com.androidcourse.musicplayerdemo.ui.adapters.models.AIHistoryRecyclerViewItem
import com.androidcourse.musicplayerdemo.ui.adapters.models.CreateMusicInfo
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentAI() : Fragment() {

    private lateinit var m_RootView : View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.m_RootView = inflater.inflate(R.layout.fragment_ai, container, false)

        return this.m_RootView
    }


    private var AIHistory = ArrayList<AIHistoryRecyclerViewItem>()  //歌曲类的数组，用于RecycleView的显示
    //控件
    private lateinit var m_CreateButton:Button
    private lateinit var m_HistoryButton:ImageButton
    private lateinit var m_SwitchCustom:Switch
    private lateinit var m_SwitchInstrmental:Switch
    private lateinit var m_CustomTextInputTitle:TextView
    private lateinit var m_MusicTitleTextInput:TextInputLayout
    private lateinit var m_CustomTextInput:TextInputLayout
    private lateinit var m_InstrmentTextInput:TextInputLayout

    //布局
    private lateinit var m_CustomInputLayout:LinearLayout
    private lateinit var m_SwitchInstrmentalLayout:LinearLayout
    private lateinit var m_InstrmentalInputLayout:LinearLayout
    private lateinit var m_CreateButtonLayout:LinearLayout

    //下载状态
    private var downloadId:Long = 0


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        //控件
        m_CreateButton=findViewById(R.id.btn_createMusic)
        m_HistoryButton=findViewById(R.id.btn_history)

        m_SwitchCustom= findViewById(R.id.customSwitch)
        m_SwitchInstrmental=findViewById(R.id.instrmSwicth)

        m_CustomTextInput=findViewById(R.id.textInput_custom)
        m_InstrmentTextInput=findViewById(R.id.textInput_instrmental)
        m_MusicTitleTextInput=findViewById(R.id.textInput_musicTitle)

        m_CustomTextInputTitle=findViewById(R.id.textInput_title_custom)

        //布局
        m_CustomInputLayout=findViewById(R.id.custom_input)
        m_SwitchInstrmentalLayout=findViewById(R.id.instrmental)
        m_InstrmentalInputLayout=findViewById(R.id.instrmental_layout)
        m_CreateButtonLayout=findViewById(R.id.creatButton)

        m_SwitchCustom.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    m_CustomTextInputTitle.text="Lyrics"
                    m_CustomTextInput
                        .setHint("Enter your own lyrics or describe a song and click Generate Lyrics...")
                    setVisible(true)
                    if(m_SwitchInstrmental.isChecked)
                        setLayoutParams(true)
                    else
                        setLayoutParams(false)
                } else {
                    m_CustomTextInputTitle.text="Song Description"
                    m_CustomTextInput
                        .setHint("a chill opera song about a bad breakup")
                    setVisible(false)
                    setLayoutParams(false)
                }
            }
        })
        m_SwitchInstrmental.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked && m_SwitchCustom.isChecked) {
                    setLayoutParams(true)
                } else {
                    setLayoutParams(false)
                }
            }
        })

        m_CreateButton.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch {
                createSunoMusic()
            }
        }

        m_HistoryButton.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch {
                getSunoMusicInfo()
//                这下面是测试数据
//                val id = "415303f4-327d-4de1-8d84-883565ee115e"
//                val imageurl = "https://cdn1.suno.ai/image_415303f4-327d-4de1-8d84-883565ee115e.png"
//                val audiourl = "https://cdn1.suno.ai/415303f4-327d-4de1-8d84-883565ee115e.mp3"
//                val title = "Laugh All Day"
//                var thesong = AIHistoryRecyclerViewItem(id,imageurl,audiourl,title)
//                AIHistory.add(thesong)
                showHistoryCreate()
            }
        }
    }


    private fun showHistoryCreate(){
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.ai_history_library)

        val recyclerView = dialog.findViewById<RecyclerView>(R.id.library_recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        val adapter = AIHistoryRecyclerViewAdapter(AIHistory)  // songs 是你已经获取到的歌曲列表
        recyclerView?.adapter = adapter
        dialog.show()

    }
    // isCustom
    private suspend fun createSunoMusic() {
        val createInfo = CreateMusicInfo(
            prompt= m_CustomTextInput.editText?.text.toString(),
            tags=m_InstrmentTextInput.editText?.text.toString(),
            title=m_MusicTitleTextInput.editText?.text.toString(),
            make_instrumental=m_SwitchInstrmental.isChecked.toString(),
            isCustom=m_SwitchCustom.isChecked.toString()
        )
        Sunoapi().createMusic(createInfo)
    }
    private suspend fun getSunoMusicInfo() {
        val musiclist=Sunoapi().getMusicInfo()
        if (musiclist != null) {
            AIHistory=musiclist
            println("ID: $AIHistory")
        }
    }

    private fun setVisible(type:Boolean) {
        val parms_creatButton=m_CreateButtonLayout.layoutParams as ConstraintLayout.LayoutParams
        //控制显示隐藏
        if(type)
        {
            m_InstrmentalInputLayout.visibility=View.VISIBLE
            parms_creatButton.topToBottom=R.id.instrmental_layout
        }else{
            m_CustomTextInput.visibility=View.VISIBLE
            m_InstrmentalInputLayout.visibility=View.GONE
            parms_creatButton.topToBottom=R.id.instrmental
        }
        m_CreateButtonLayout.layoutParams=parms_creatButton
    }
    private fun setLayoutParams(type:Boolean) {
        //获取布局信息
        val parms_instrmental=m_SwitchInstrmentalLayout.layoutParams as ConstraintLayout.LayoutParams
        //更改布局约束
        if(type)
        {
            m_CustomTextInput.visibility=View.GONE
            parms_instrmental.topToBottom=R.id.custom
        }else{
            m_CustomTextInput.visibility=View.VISIBLE
            parms_instrmental.topToBottom=R.id.custom_input
        }
        m_SwitchInstrmentalLayout.layoutParams=parms_instrmental
    }

    fun <T : View> findViewById(@IdRes id : Int) : T {
        return this.m_RootView.findViewById(id)
    }

}