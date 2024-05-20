package com.androidcourse.musicplayerdemo.views

import android.view.View
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.androidcourse.musicplayerdemo.R
import com.google.android.material.progressindicator.LinearProgressIndicator

public class MediaPlayerView (rootView : View){

    companion object {
        const val STATE_NORMAL: Int = 0

        const val STATE_PARTIAL : Int = 1
    }


    private val mRootView : View

    private var mState : Int = 0

    private val mBottomSheet : FrameLayout

    private val mControlsContainer : ConstraintLayout

    init {
        mRootView = rootView

        mBottomSheet = findViewById(R.id.media_player_bottom_sheet_behavior)
        mControlsContainer = findViewById(R.id.media_player_controls_container)

        mRootView.alpha = 0.0F
    }

    // slideOffset 滑动偏移量 alpha 是透明度的设置 1F 表示完全不透明 0表示完全透明
    fun onSliding(slideOffset : Float, state : Int) {
        val fadeStart : Float = 0.25F
        val alpha : Float = (slideOffset - fadeStart) * (1F / (1F - fadeStart))

        //正常状态就显示
        if(state == STATE_NORMAL) {
            mRootView.alpha = alpha
            mControlsContainer.alpha = 1F
        }
        else { //STATE_PARTIAL 部分显示
            mControlsContainer.alpha = 1F - alpha
        }

        mState = state
    }

    //为何这里要做一个转换呢 大概因为AppCompatActivity本质也是个实现了findViewById的view
    fun <T : View> findViewById(@IdRes id : Int) : T {
        return mRootView.findViewById(id)
    }

}