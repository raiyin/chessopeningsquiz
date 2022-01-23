package com.publicmaders.android.chessopeningsquiz

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.TranslateAnimation
import android.widget.*
import java.io.PrintWriter
import java.net.ServerSocket
import kotlin.math.min


class TrainActivity : AppCompatActivity(), ChessDelegate {
    private lateinit var trainView: TrainView
    private var openingManager = OpeningManager()
    private lateinit var lvOpening: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train)
        trainView = findViewById(R.id.train_view)
        lvOpening = findViewById(R.id.lv_train_openings)
        trainView.chessDelegate = this

        val adapter: ListAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            openingManager.openings
        )
        lvOpening.adapter = adapter
        lvOpening.setOnItemClickListener { parent, view, position, id -> drawOpening((lvOpening.getItemAtPosition(position) as Opening).pgn)
            //Toast.makeText(
            //    this,
            //    "Clicked item : " + position,
            //    Toast.LENGTH_SHORT
            //).show()
        }
    }

    private fun drawOpening(pgn: String)
    {
        BoardState.reset()
        trainView.mustDrawMoves = true
        trainView.openingMoves = pgn
        // Создаем список матриц ходов.
        trainView.initMovesPathsFromPgn(pgn)
        BoardState.reset()
        trainView.invalidate()
    }

    //override fun pieceAt(square: Square): ChessPiece? = ChessGame.pieceAt(square)
    override fun pieceAt(square: Square): ChessPiece?
    {
        return BoardState.pieceAt(square)
    }


    //override fun movePiece(from: Square, to: Square) {
    //    ChessGame.movePiece(from, to)
    //    //ObjectAnimator.ofFloat(trainView.movingPieceBitmap, "x", 100f).apply {
    //    //    duration = 2000
    //    //    start()
    //    //}
    //    trainView.invalidate()
    //}
    override fun movePiece(from: Square, to: Square) {
        //BoardState.movePiece(from, to)



        //ObjectAnimator.ofFloat(trainView.movingPieceBitmap, "x", 100f).apply {
        //    duration = 2000
        //    start()
        //}
        trainView.invalidate()
    }
}