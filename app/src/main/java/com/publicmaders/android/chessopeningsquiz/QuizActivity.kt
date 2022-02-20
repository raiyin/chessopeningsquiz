package com.publicmaders.android.chessopeningsquiz

import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class QuizActivity : AppCompatActivity(), ChessDelegate, CountDownAnimation.CountDownListener {

    private lateinit var chessBoardView: TrainView
    private lateinit var bFirst: Button
    private lateinit var bSecond: Button
    private lateinit var bThird: Button
    private lateinit var bFourth: Button
    private lateinit var pbProgress: ProgressBar
    private lateinit var quizManager: QuizManager
    private var openingManager = OpeningManager()
    private var textView: TextView? = null
    private var countDownAnimation: CountDownAnimation? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        chessBoardView = findViewById(R.id.train_quiz_view)
        bFirst = findViewById(R.id.button_first_answer)
        bSecond = findViewById(R.id.button_second_answer)
        bThird = findViewById(R.id.button_third_answer)
        bFourth = findViewById(R.id.button_fourth_answer)
        pbProgress = findViewById(R.id.pbProgress)
        quizManager = QuizManager(10, openingManager)

        bFirst.setOnClickListener {
        }

        bSecond.setOnClickListener {
        }

        bThird.setOnClickListener {
        }

        bFourth.setOnClickListener {
        }

        textView = findViewById<View>(R.id.textView) as TextView
        val startButton = findViewById<View>(R.id.button_next_quiz) as Button
        startButton.setOnClickListener {
            startCountDownAnimation()
        }
        initCountDownAnimation()
    }

    private fun drawOpening(pgn: String) {
        BoardState.reset()
        chessBoardView.openingMoves = pgn

        // Создаем список матриц ходов.
        chessBoardView.initMovesPathsFromPgn(pgn)
        BoardState.reset()
        chessBoardView.invalidate()
    }

    override fun pieceAt(square: Square): ChessPiece? {
        return BoardState.pieceAt(square)
    }

    override fun movePiece(from: Square, to: Square) {
        chessBoardView.invalidate()
    }

    private fun initCountDownAnimation() {
        countDownAnimation = CountDownAnimation(textView!!, getStartCount())
        countDownAnimation!!.setCountDownListener(this)
    }

    private fun startCountDownAnimation() {
        val scaleAnimation: Animation = ScaleAnimation(
            1.0f, 0.0f, 1.0f,
            0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        val alphaAnimation: Animation = AlphaAnimation(1.0f, 0.0f)
        val animationSet = AnimationSet(false)
        animationSet.addAnimation(scaleAnimation)
        animationSet.addAnimation(alphaAnimation)
        countDownAnimation!!.animation = animationSet

        countDownAnimation!!.startCount = getStartCount()
        countDownAnimation!!.start()
    }

    private fun getStartCount(): Int {
        return 3
    }

    override fun onCountDownEnd(animation: CountDownAnimation?) {
        Toast.makeText(this, "Start!", Toast.LENGTH_SHORT).show()
    }
}