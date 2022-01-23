package com.publicmaders.android.chessopeningsquiz

import android.content.Intent
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import kotlin.math.round
import android.widget.FrameLayout
import android.view.animation.ScaleAnimation

import android.view.animation.Animation

import android.view.animation.TranslateAnimation

import android.view.animation.AnimationSet





class MainActivity : AppCompatActivity() {

    private lateinit var trainingButton: Button
    private lateinit var quizButton: Button
    private lateinit var testButton: Button
    private lateinit var testImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trainingButton = findViewById(R.id.button_training)
        quizButton = findViewById(R.id.button_quiz)
        testButton = findViewById(R.id.test_button)
        testImageView = findViewById(R.id.test_iv)

        trainingButton.setOnClickListener { view: View ->
            val intent = Intent(this, TrainActivity::class.java)
            startActivity(intent)
        }

        quizButton.setOnClickListener { view: View ->
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }

        testButton.setOnClickListener { view: View ->
            //var matrix = Matrix()
//
            //var dWidth = testImageView.drawable.intrinsicWidth
            //val dHeight = testImageView.drawable.intrinsicHeight
            //val vWidth = testImageView.measuredWidth
            //val vHeight = testImageView.measuredHeight
//
            //matrix.setTranslate(
            //        round((vWidth - dWidth) * 0.5f),
            //        round((vHeight - dHeight) * 0.5f)
            //    )

            //val mParams = testImageView.animationMatrix
            //mParams.height = mParams.height + 100
            //testImageView.layoutParams = mParams

            var mZoomIn = AnimationSet(true)

            var mTranslate = TranslateAnimation(
                Animation.ABSOLUTE,0.0f, Animation.ABSOLUTE,
                -testImageView.x / (this.window.decorView.width / testImageView.width),
                Animation.ABSOLUTE,0.0f, Animation.ABSOLUTE,
                -testImageView.y / (this.window.decorView.width / testImageView.width)
            )
            mTranslate.duration = 2000

            var mScale = ScaleAnimation(
                1F, (this.window.decorView.width / 100).toFloat(),
                1F, (this.window.decorView.width / 100).toFloat()
            )
            mScale.duration = 200

            mZoomIn.addAnimation(mTranslate)
            mZoomIn.addAnimation(mScale)
            mZoomIn.fillAfter = true
            mZoomIn.isFillEnabled = true

            testImageView.startAnimation(mZoomIn)
        }
    }
}
