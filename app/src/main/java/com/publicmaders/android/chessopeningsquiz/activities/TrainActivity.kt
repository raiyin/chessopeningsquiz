package com.publicmaders.android.chessopeningsquiz.activities

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.publicmaders.android.chessopeningsquiz.*
import com.publicmaders.android.chessopeningsquiz.controllers.OpeningManager
import com.publicmaders.android.chessopeningsquiz.delegates.ChessDelegate
import com.publicmaders.android.chessopeningsquiz.interfaces.OnKeyboardVisibilityListener
import com.publicmaders.android.chessopeningsquiz.models.BoardState
import com.publicmaders.android.chessopeningsquiz.models.ChessPiece
import com.publicmaders.android.chessopeningsquiz.models.Opening
import com.publicmaders.android.chessopeningsquiz.models.Square
import com.publicmaders.android.chessopeningsquiz.views.TrainView
import android.util.AttributeSet


class TrainActivity : AppCompatActivity(), ChessDelegate, OnKeyboardVisibilityListener
{
    private lateinit var trainView: TrainView
    private var openingManager = OpeningManager()
    private lateinit var lvOpening: ListView
    private lateinit var tilOpeningFilter: TextInputLayout
    private lateinit var ietOpeningFilter: EditText
    private lateinit var parent: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train)
        supportActionBar?.hide()

        trainView = findViewById(R.id.train_view)
        lvOpening = findViewById(R.id.lv_train_openings)
        tilOpeningFilter = findViewById(R.id.tilOpeningFilter)
        ietOpeningFilter = findViewById(R.id.ietOpeningFilter)

        trainView.chessDelegate = this
        trainView.lvOpening = lvOpening
        trainView.isFocusableInTouchMode = true
        trainView.requestFocus()
        parent = trainView.parent as ViewGroup

        val adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, openingManager.openings)
        lvOpening.adapter = adapter
        lvOpening.setOnItemClickListener { _, _, position, _ ->
            if (trainView.animStepIndex == 0 && trainView.moveIndex == 0)
            {
                trainView.needDrawOpening = true
                drawOpening((lvOpening.getItemAtPosition(position) as Opening).en_pgn)
            }
        }

        ietOpeningFilter.addTextChangedListener(object : TextWatcher
        {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {
                adapter.filter.filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {
            }

            override fun afterTextChanged(s: Editable)
            {
            }
        })

        setKeyboardVisibilityListener(this)
    }

    private fun drawOpening(pgn: String)
    {
        BoardState.reset()
        lvOpening.isEnabled = false
        trainView.openingMoves = pgn

        // Создаем список матриц ходов.
        trainView.initMovesPathsFromPgn(pgn)
        BoardState.reset()
        trainView.invalidate()
    }

    override fun pieceAt(square: Square): ChessPiece?
    {
        return BoardState.pieceAt(square)
    }

    override fun movePiece(from: Square, to: Square)
    {
        trainView.invalidate()
    }

    private fun setKeyboardVisibilityListener(onKeyboardVisibilityListener: OnKeyboardVisibilityListener)
    {
        val parentView: View = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
        parentView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener
        {
            private var alreadyOpen = false
            private val defaultKeyboardHeightDP = 100
            private val EstimatedKeyboardDP =
                defaultKeyboardHeightDP + if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) 48 else 0
            private val rect: Rect = Rect()
            override fun onGlobalLayout()
            {
                val estimatedKeyboardHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    EstimatedKeyboardDP.toFloat(),
                    parentView.resources.displayMetrics).toInt()
                parentView.getWindowVisibleDisplayFrame(rect)
                val heightDiff: Int = parentView.rootView.height - (rect.bottom - rect.top)
                val isShown = heightDiff >= estimatedKeyboardHeight
                if (isShown == alreadyOpen)
                {
                    return
                }
                alreadyOpen = isShown
                onKeyboardVisibilityListener.onVisibilityChanged(isShown)
            }
        })
    }

    override fun onVisibilityChanged(visible: Boolean)
    {
        if (visible)
        {
            val parent = trainView.parent as ViewGroup
            parent.removeView(trainView)
        }
        else
        {
            parent.addView(trainView, 0)
        }
    }
}
