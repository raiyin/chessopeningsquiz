package com.publicmaders.android.chessopeningsquiz.animations

import android.os.Handler
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView

class CountDownAnimation(private val mTextView: TextView, private var startCount: Int)
{
    private var mAnimation: Animation
    private var mCurrentCount = 0
    private var mStartCount = 0
    private var mListener: CountDownListener? = null
    private val mHandler: Handler = Handler()

    init {
        mStartCount = startCount
        mAnimation = AlphaAnimation(1.0f, 0.0f)
        mAnimation.duration = 1000
    }

    private val mCountDown = Runnable {
        if (mCurrentCount > 0) {
            mTextView.text = mCurrentCount.toString()
            mTextView.startAnimation(mAnimation)
            mCurrentCount--
        } else {
            mTextView.visibility = View.GONE
            if (mListener != null) mListener!!.onCountDownEnd(this@CountDownAnimation)
        }
    }

    fun start() {
        mHandler.removeCallbacks(mCountDown)
        mTextView.text = startCount.toString()
        mTextView.visibility = View.VISIBLE
        mCurrentCount = startCount
        mHandler.post(mCountDown)
        for (i in 1..startCount) {
            mHandler.postDelayed(mCountDown, (i * 1000).toLong())
        }
    }

    var animation: Animation
        get() = mAnimation
        set(animation) {
            mAnimation = animation
            if (mAnimation.duration == 0L) mAnimation.duration = 1000
        }

    fun setCountDownListener(listener: CountDownListener) {
        mListener = listener
    }

    interface CountDownListener {
        fun onCountDownEnd(animation: CountDownAnimation?)
    }
}
