package com.androidcourse.musicplayerdemo.views

import android.view.View
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.androidcourse.musicplayerdemo.R
import com.google.android.material.progressindicator.LinearProgressIndicator

class MediaPlayerBarView (rootView : View){

    companion object {
        const val STATE_NORMAL: Int = 0

        const val STATE_PARTIAL : Int = 1
    }


    private val mRootView : View

    private var mState : Int = 0

    private val mBackgroundView : FrameLayout

    private val mProgressIndicator : LinearProgressIndicator

    private val mControlsContainer : ConstraintLayout

    init {
        mRootView = rootView

        mBackgroundView = findViewById(R.id.media_player_bar_bg)
        mControlsContainer = findViewById(R.id.media_player_bar_controls_container)
        mProgressIndicator = findViewById(R.id.media_player_bar_progress_indicator)

        mRootView.alpha = 1.0F
    }

    // slideOffset 滑动偏移量 alpha 是透明度的设置 1F 表示完全不透明 0表示完全透明
    fun onSliding(slideOffset : Float, state : Int) {
        val fadeStart : Float = 0.25F
        val alpha : Float = (slideOffset - fadeStart)

        //正常状态就显示
        if(state == STATE_NORMAL) {
            mRootView.alpha = 1F - alpha
            mBackgroundView.alpha = 1F
            mProgressIndicator.alpha = 1F
            mControlsContainer.alpha = 1F
        }
        else { //STATE_PARTIAL 部分显示
            mRootView.alpha = alpha
            mBackgroundView.alpha = 0F
            mProgressIndicator.alpha = 0F
            mControlsContainer.alpha = 1F
        }

        mState = state
    }

    //为何这里要做一个转换呢 大概因为AppCompatActivity本质也是个实现了findViewById的view
    fun <T : View> findViewById(@IdRes id : Int) : T {
        return mRootView.findViewById(id)
    }
}