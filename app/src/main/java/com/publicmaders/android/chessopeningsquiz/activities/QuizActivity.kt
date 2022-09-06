package com.publicmaders.android.chessopeningsquiz.activities

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Color.*
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.publicmaders.android.chessopeningsquiz.*
import com.publicmaders.android.chessopeningsquiz.animations.CountDownAnimation
import com.publicmaders.android.chessopeningsquiz.controllers.OpeningManager
import com.publicmaders.android.chessopeningsquiz.controllers.QuizManager
import com.publicmaders.android.chessopeningsquiz.delegates.ChessDelegate
import com.publicmaders.android.chessopeningsquiz.interfaces.CountDownListener
import com.publicmaders.android.chessopeningsquiz.models.BoardState
import com.publicmaders.android.chessopeningsquiz.models.ChessPiece
import com.publicmaders.android.chessopeningsquiz.models.Settings
import com.publicmaders.android.chessopeningsquiz.models.Square
import com.publicmaders.android.chessopeningsquiz.views.TrainView
import java.util.*
import kotlin.concurrent.schedule


class QuizActivity : AppCompatActivity(), ChessDelegate, CountDownListener
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
    private var tvCountDown: TextView? = null
    private var countDownAnimation: CountDownAnimation? = null
    private lateinit var buttonList: List<Button>
    private lateinit var tlControls: TableLayout
    private lateinit var timer: Timer

    // Для того, чтобы изменять поведение на нажатиекнопок ответа
    private var answerDone: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        supportActionBar?.hide()

        chessBoardView = findViewById(R.id.train_quiz_view)
        bFirst = findViewById(R.id.button_first_answer)
        bSecond = findViewById(R.id.button_second_answer)
        bThird = findViewById(R.id.button_third_answer)
        bFourth = findViewById(R.id.button_fourth_answer)
        tvCountDown = findViewById<View>(R.id.tv_CountDown) as TextView
        bNext = findViewById<View>(R.id.button_next_quiz) as Button
        pbProgress = findViewById(R.id.pbProgress)
        tlControls = findViewById(R.id.tlControls)
        quizManager = QuizManager(openingManager)
        buttonList = mutableListOf(bFirst, bSecond, bThird, bFourth)

        chessBoardView.chessDelegate = this
        chessBoardView.isFocusableInTouchMode = true
        chessBoardView.requestFocus()

        bFirst.setOnClickListener {
            if (!answerDone)
            {
                val rightQuizNumber = quizManager.getCurrentTaskRightIndex()
                setButtonsColor(it, rightQuizNumber)
                bNext.isEnabled = true
                processAnswer(it, rightQuizNumber)
                pbProgress.progress =
                    (quizManager.currentTaskNumber + 1) * 100 / Settings.TasksCount
                checkLastTask()
                answerDone = true
            }
            else
            {
                bFirst.isSelected = true
            }
        }

        bSecond.setOnClickListener {
            if (!answerDone)
            {
                val rightQuizNumber = quizManager.getCurrentTaskRightIndex()
                setButtonsColor(it, rightQuizNumber)
                bNext.isEnabled = true
                processAnswer(it, rightQuizNumber)
                pbProgress.progress =
                    (quizManager.currentTaskNumber + 1) * 100 / Settings.TasksCount
                checkLastTask()
                bSecond.isSelected = true
                answerDone = true
            }
            else
            {
                bSecond.isSelected = true
            }
        }

        bThird.setOnClickListener {
            if (!answerDone)
            {
                val rightQuizNumber = quizManager.getCurrentTaskRightIndex()
                setButtonsColor(it, rightQuizNumber)
                bNext.isEnabled = true
                processAnswer(it, rightQuizNumber)
                pbProgress.progress =
                    (quizManager.currentTaskNumber + 1) * 100 / Settings.TasksCount
                checkLastTask()
                bThird.isSelected = true
                answerDone = true
            }
            else
            {
                bThird.isSelected = true
            }
        }

        bFourth.setOnClickListener {
            if (!answerDone)
            {
                val rightQuizNumber = quizManager.getCurrentTaskRightIndex()
                setButtonsColor(it, rightQuizNumber)
                bNext.isEnabled = true
                processAnswer(it, rightQuizNumber)
                pbProgress.progress =
                    (quizManager.currentTaskNumber + 1) * 100 / Settings.TasksCount
                checkLastTask()
                bFourth.isSelected = true
                answerDone = true
            }
            else
            {
                bFourth.isSelected = true
            }
        }

        bNext.setOnClickListener {
            bNextHandler()
        }

        countDownAnimation = CountDownAnimation(tvCountDown!!)
        countDownAnimation!!.setCountDownListener(this)
        bNext.text = getString(R.string.start)

        chessBoardView.post {
            val boardCenter =
                Point((chessBoardView.translationX + chessBoardView.width / 2).toInt(),
                    (chessBoardView.translationY + chessBoardView.height / 2).toInt())

            tvCountDown!!.x = (boardCenter.x - tvCountDown!!.width / 2).toFloat()
            tvCountDown!!.y = (boardCenter.y - tvCountDown!!.height / 2).toFloat()
        }

        setAnswerButtonsEnableState(false)
        setPrestartControlsVisible(false)
        bNextHandler()
    }

    private fun bNextHandler()
    {
        if (bNext.text == getString(R.string.start_new) || bNext.text == getString(R.string.start))
        {
            setAnswerButtonsEnableState(false)
            pbProgress.progress = 0
            quizManager.resetQuiz()
            resetButtonsColor()
            countDownAnimation!!.start()
        }
        else
        {
            quizManager.incrementCurrentTaskNumber()
            resetButtonsColor()
            chessBoardView.needDrawOpening = true
            drawOpening(quizManager.getCurrentTaskRightPgn())
            setOptionsButtonsName()
            setAnswerButtonsEnableState(true)
        }
        bNext.isEnabled = false
        answerDone = false
    }

    private fun setPrestartControlsVisible(isVisible: Boolean)
    {
        bNext.visibility = if (isVisible) VISIBLE; else INVISIBLE;
        pbProgress.visibility = if (isVisible) VISIBLE; else INVISIBLE;
        tlControls.visibility = if (isVisible) VISIBLE; else INVISIBLE;
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
                getString(R.string.you_have_solved) + quizManager.rightAnswersCount + getString(R.string.problems_out_of) + Settings.TasksCount,
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
            it.setBackgroundResource(R.drawable.button_default)
        }
    }

    private fun setOptionsButtonsName()
    {
        val names = quizManager.getCurrentTaskOptions()
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
        buttonList[rightQuizNumber].setBackgroundResource(R.drawable.button_right)

        if ((button.tag as String).toInt() != rightQuizNumber)
        {
            button.setBackgroundResource(R.drawable.button_wrong)
        }
    }

    override fun onCountDownEnd(animation: CountDownAnimation?)
    {
        Toast.makeText(this, getString(R.string.startQuizText), Toast.LENGTH_SHORT).show()
        chessBoardView.needDrawOpening = true
        drawOpening(quizManager.getCurrentTaskRightPgn())
        setOptionsButtonsName()
        setAnswerButtonsEnableState(true)
        bNext.text = getString(R.string.next_quiz)
        setPrestartControlsVisible(true)
    }
}