package com.publicmaders.android.chessopeningsquiz.activities

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.publicmaders.android.chessopeningsquiz.*
import com.publicmaders.android.chessopeningsquiz.animations.CountDownAnimation
import com.publicmaders.android.chessopeningsquiz.controllers.OpeningManager
import com.publicmaders.android.chessopeningsquiz.controllers.QuizManager
import com.publicmaders.android.chessopeningsquiz.delegates.ChessDelegate
import com.publicmaders.android.chessopeningsquiz.models.BoardState
import com.publicmaders.android.chessopeningsquiz.models.ChessPiece
import com.publicmaders.android.chessopeningsquiz.models.Settings
import com.publicmaders.android.chessopeningsquiz.models.Square
import com.publicmaders.android.chessopeningsquiz.views.TrainView


class QuizActivity : AppCompatActivity(), ChessDelegate, CountDownAnimation.CountDownListener
{
    private lateinit var chessBoardView: TrainView
    private lateinit var bFirst: Button
    private lateinit var bSecond: Button
    private lateinit var bThird: Button
    private lateinit var bFourth: Button
    private lateinit var bNext: Button
    private lateinit var pbProgress: ProgressBar
    private lateinit var quizManager: QuizManager
    private var openingManager = OpeningManager()
    private var textView: TextView? = null
    private var countDownAnimation: CountDownAnimation? = null
    private var startCount = 3
    private lateinit var buttonList: List<Button>

    // Для того, чтобы отличать был ли уже обратный отчет или нет.
    private var wasCountDown: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        chessBoardView = findViewById(R.id.train_quiz_view)
        bFirst = findViewById(R.id.button_first_answer)
        bSecond = findViewById(R.id.button_second_answer)
        bThird = findViewById(R.id.button_third_answer)
        bFourth = findViewById(R.id.button_fourth_answer)
        textView = findViewById<View>(R.id.textView) as TextView
        bNext = findViewById<View>(R.id.button_next_quiz) as Button
        pbProgress = findViewById(R.id.pbProgress)
        quizManager = QuizManager(openingManager)
        buttonList = mutableListOf(bFirst, bSecond, bThird, bFourth)

        chessBoardView.chessDelegate = this
        chessBoardView.isFocusableInTouchMode = true
        chessBoardView.requestFocus()

        bFirst.setOnClickListener {
            val rightQuizNumber = quizManager.getCurrentTaskRightIndex()
            setButtonsColor(it, rightQuizNumber)
            setAnswerButtonsEnableState(false)
            bNext.isEnabled = true
            processAnswer(it, rightQuizNumber)
            pbProgress.progress =
                (quizManager.currentTaskNumber + 1) * 100 / Settings.TasksCount
            checkLastTask()
        }

        bSecond.setOnClickListener {
            val rightQuizNumber = quizManager.getCurrentTaskRightIndex()
            setButtonsColor(it, rightQuizNumber)
            setAnswerButtonsEnableState(false)
            bNext.isEnabled = true
            processAnswer(it, rightQuizNumber)
            pbProgress.progress =
                (quizManager.currentTaskNumber + 1) * 100 / Settings.TasksCount
            checkLastTask()
        }

        bThird.setOnClickListener {
            val rightQuizNumber = quizManager.getCurrentTaskRightIndex()
            setButtonsColor(it, rightQuizNumber)
            setAnswerButtonsEnableState(false)
            bNext.isEnabled = true
            processAnswer(it, rightQuizNumber)
            pbProgress.progress =
                (quizManager.currentTaskNumber + 1) * 100 / Settings.TasksCount
            checkLastTask()
        }

        bFourth.setOnClickListener {
            val rightQuizNumber = quizManager.getCurrentTaskRightIndex()
            setButtonsColor(it, rightQuizNumber)
            setAnswerButtonsEnableState(false)
            bNext.isEnabled = true
            processAnswer(it, rightQuizNumber)
            pbProgress.progress =
                (quizManager.currentTaskNumber + 1) * 100 / Settings.TasksCount
            checkLastTask()
        }

        bNext.setOnClickListener {
            if (!wasCountDown)
            {
                startCountDownAnimation()
                wasCountDown = true
            }
            else if (bNext.text == getString(R.string.start_new))
            {
                bNext.text = getString(R.string.next_quiz)
                pbProgress.progress = 0
                quizManager.resetQuiz()
                resetButtonsColor()
                startCountDownAnimation()
                wasCountDown = true
            }
            else
            {
                quizManager.incrementCurrentTaskNumber()
                resetButtonsColor()
                chessBoardView.needDrawOpening = true
                drawOpening(quizManager.getCurrentTaskRightPgn())
                setOptionsButtonsName()
            }
            setAnswerButtonsEnableState(true)
            bNext.isEnabled = false
        }

        initCountDownAnimation()
        bNext.text = getString(R.string.start)
    }

    private fun drawOpening(pgn: String)
    {
        BoardState.reset()
        chessBoardView.openingMoves = pgn

        // Создаем список матриц ходов.
        chessBoardView.initMovesPathsFromPgn(pgn)
        BoardState.reset()
        chessBoardView.invalidate()
    }

    override fun pieceAt(square: Square): ChessPiece?
    {
        return BoardState.pieceAt(square)
    }

    override fun movePiece(from: Square, to: Square)
    {
        chessBoardView.invalidate()
    }

    private fun checkLastTask()
    {
        if (quizManager.currentTaskNumber == Settings.TasksCount - 1)
        {
            Toast.makeText(this,
                getString(R.string.you_have_solved) + quizManager.rightAnswersCount +
                        getString(R.string.problems_out_of) + Settings.TasksCount,
                Toast.LENGTH_SHORT).show()
            bNext.text = getString(R.string.start_new)
        }
    }

    private fun processAnswer(button: View, rightQuizNumber: Int)
    {
        if ((button.tag as String).toInt() == rightQuizNumber) quizManager.rightAnswersCount =
            quizManager.rightAnswersCount + 1
    }

    private fun resetButtonsColor()
    {
        buttonList.forEach {
            it.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.answer_button_color, theme))
        }
    }

    private fun setOptionsButtonsName()
    {
        val names = quizManager.getCurrentTaskOptions()
        if (names.count() < 4) Log.d("MyInfo", names[0])

        bFirst.text = names[0]
        bSecond.text = names[1]
        bThird.text = names[2]
        bFourth.text = names[3]
    }

    private fun setAnswerButtonsEnableState(enable: Boolean)
    {
        for (button in buttonList)
        {
            button.isEnabled = enable
        }
    }

    private fun setButtonsColor(button: View, rightQuizNumber: Int)
    {
        Log.d("MyInfo", "#" + Integer.toHexString(R.color.rightAnswer))

        buttonList[rightQuizNumber].backgroundTintList =
            ColorStateList.valueOf(Color.parseColor("#" + Integer.toHexString(R.color.rightAnswer)))
        if ((button.tag as String).toInt() != rightQuizNumber)
        {
            button.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#" + Integer.toHexString(R.color.wrongAnswer)))
        }
    }

    //region Counter

    private fun initCountDownAnimation()
    {
        countDownAnimation = CountDownAnimation(textView!!, startCount)
        countDownAnimation!!.setCountDownListener(this)
    }

    private fun startCountDownAnimation()
    {
        val scaleAnimation: Animation = ScaleAnimation(1.0f,
            0.0f,
            1.0f,
            0.0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f)
        val alphaAnimation: Animation = AlphaAnimation(1.0f, 0.0f)
        val animationSet = AnimationSet(false)
        animationSet.addAnimation(scaleAnimation)
        animationSet.addAnimation(alphaAnimation)

        countDownAnimation!!.animation = animationSet
        countDownAnimation!!.start()
    }

    override fun onCountDownEnd(animation: CountDownAnimation?)
    {
        Toast.makeText(this, getString(R.string.startQuizText), Toast.LENGTH_SHORT).show()
        chessBoardView.needDrawOpening = true
        drawOpening(quizManager.getCurrentTaskRightPgn())
        setOptionsButtonsName()
        bNext.text = getString(R.string.next_quiz)
    }

    //endregion
}