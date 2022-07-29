package com.publicmaders.android.chessopeningsquiz.animations

import android.os.Handler
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.TextView
import com.publicmaders.android.chessopeningsquiz.interfaces.CountDownListener

class CountDownAnimation(private val tvAnimation: TextView)
{
    private var mAnimation: Animation
    private var startCount = 3
    private var mCurrentCount = 0
    private var mListener: CountDownListener? = null
    private val mHandler: Handler = Handler()

    init
    {
        val alphaAnimation: Animation = AlphaAnimation(1.0f, 0.0f)
        val animationSet = AnimationSet(false)
        animationSet.addAnimation(alphaAnimation)

        mAnimation = animationSet
        mAnimation.duration = 1000
    }

    private val mCountDown = Runnable {
        if (mCurrentCount > 0)
        {
            tvAnimation.text = mCurrentCount.toString()
            tvAnimation.startAnimation(mAnimation)
            mCurrentCount--
        }
        else
        {
            tvAnimation.visibility = View.GONE
            if (mListener != null)
            {
                mListener!!.onCountDownEnd(this@CountDownAnimation)
            }
        }
    }

    fun start()
    {
        mHandler.removeCallbacks(mCountDown)
        tvAnimation.visibility = View.VISIBLE
        mCurrentCount = startCount
        mHandler.post(mCountDown)
        for (i in 1..startCount)
        {
            mHandler.postDelayed(mCountDown, (i * mAnimation.duration))
        }
    }

    fun setCountDownListener(listener: CountDownListener)
    {
        mListener = listener
    }
}
