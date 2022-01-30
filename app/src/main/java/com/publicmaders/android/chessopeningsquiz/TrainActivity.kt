package com.publicmaders.android.chessopeningsquiz

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout


class TrainActivity : AppCompatActivity(), ChessDelegate {
    private lateinit var trainView: TrainView
    private var openingManager = OpeningManager()
    private lateinit var lvOpening: ListView
    private lateinit var tilOpeningFilter: TextInputLayout
    private lateinit var ietOpeningFilter: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train)
        trainView = findViewById(R.id.train_view)
        lvOpening = findViewById(R.id.lv_train_openings)
        tilOpeningFilter = findViewById(R.id.tilOpeningFilter);
        ietOpeningFilter = findViewById(R.id.ietOpeningFilter);

        trainView.chessDelegate = this
        trainView.lvOpening = lvOpening
        trainView.isFocusableInTouchMode = true;
        trainView.requestFocus()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            openingManager.openings
        )
        lvOpening.adapter = adapter
        lvOpening.setOnItemClickListener { parent, view, position, id ->
            if (trainView.animStepIndex == 0 && trainView.moveIndex == 0) {
                trainView.needDrawOpening = true
                drawOpening((lvOpening.getItemAtPosition(position) as Opening).pgn)
            }
        }

        ietOpeningFilter.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun drawOpening(pgn: String) {
        BoardState.reset()
        lvOpening.isEnabled = false
        trainView.openingMoves = pgn

        // Создаем список матриц ходов.
        trainView.initMovesPathsFromPgn(pgn)
        BoardState.reset()
        trainView.invalidate()
    }

    override fun pieceAt(square: Square): ChessPiece? {
        return BoardState.pieceAt(square)
    }

    override fun movePiece(from: Square, to: Square) {
        trainView.invalidate()
    }
}
