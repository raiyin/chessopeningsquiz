package com.publicmaders.android.chessopeningsquiz

import android.graphics.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.io.PrintWriter
import java.net.ServerSocket
import kotlin.math.min


class TrainActivity : AppCompatActivity(), ChessDelegate{
    private lateinit var trainView: TrainView
    private val scaleFactor = 1.0f
    private var originX = 20f
    private var originY = 200f
    private var cellSide = 130f
    private val imgResIDs = setOf(
        R.drawable.ic_chess_bb,
        R.drawable.ic_chess_wb,
        R.drawable.ic_chess_bk,
        R.drawable.ic_chess_wk,
        R.drawable.ic_chess_bq,
        R.drawable.ic_chess_wq,
        R.drawable.ic_chess_br,
        R.drawable.ic_chess_wr,
        R.drawable.ic_chess_bn,
        R.drawable.ic_chess_wn,
        R.drawable.ic_chess_bp,
        R.drawable.ic_chess_bp,
    )

    private val paint = Paint()
    private val bitmaps = mutableMapOf<Int, Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train)
        trainView = findViewById<TrainView>(R.id.train_view)

        loadBitmaps()
    }

    private fun loadBitmaps()
    {
        //imgResIDs.forEach { imgResID ->
        //    bitmaps[imgResID] = BitmapFactory.decodeResource(resources, imgResID)
        //}
    }

    fun onDraw(canvas: Canvas?) {
        canvas ?: return

        //val chessBoardSide = min(width, height) * scaleFactor
        //cellSide = chessBoardSide / 8f
        //originX = (width - chessBoardSide) / 2f
        //originY = (height - chessBoardSide) / 2f

        drawPieces(canvas)
    }

    private fun drawPieces(canvas: Canvas) {
        for (row in 0 until 8)
            for (col in 0 until 8)
                pieceAt(Square(col, row))?.let { piece ->
                    drawPieceAt(canvas, col, row, piece.resID)
                    drawPieceAt(canvas, col, row, R.drawable.ic_chess_bb)
                }

        //movingPieceBitmap?.let {
        //    canvas.drawBitmap(it, null, RectF(movingPieceX - cellSide/2, movingPieceY - cellSide/2,movingPieceX + cellSide/2,movingPieceY + cellSide/2), paint)
        //}
    }

    private fun drawPieceAt(canvas: Canvas, col: Int, row: Int, resID: Int)
    {
        canvas.drawBitmap(bitmaps[resID]!!, null, RectF(originX + col * cellSide,originY + (7 - row) * cellSide,originX + (col + 1) * cellSide,originY + ((7 - row) + 1) * cellSide), paint)
    }

    override fun pieceAt(square: Square): ChessPiece? = ChessGame.pieceAt(square)

    override fun movePiece(from: Square, to: Square) {
        ChessGame.movePiece(from, to)
        trainView.invalidate()
    }
}