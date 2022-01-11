package com.publicmaders.android.chessopeningsquiz

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.min
import androidx.core.graphics.drawable.toBitmap
import android.graphics.Bitmap
import android.util.Log
import android.R.anim
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.graphics.PathMeasure

class TrainView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val scaleFactor = 1.0f
    private var originX = 20f
    private var originY = 200f
    private var cellSide = 130f
    private val lightColor = Color.parseColor("#EEEEEE")
    private val darkColor = Color.parseColor("#BBBBBB")
    private var mustDrawMoves = false
    private var currentMoves = ""
    //private val boardState = BoardState()

    // Animation
    var view: View? = null

    var iCurStep = 0
    var pathMoveAlong = Path()
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
        R.drawable.ic_chess_wp,
    )
    private val bitmaps = mutableMapOf<Int, Bitmap>()
    private val paint = Paint()

    var movingPieceBitmap: Bitmap? = null
    var movingPiece: ChessPiece? = null
    private var fromCol: Int = -1
    private var fromRow: Int = -1
    private var movingPieceX = -1f
    private var movingPieceY = -1f

    var chessDelegate: ChessDelegate? = null

    init {
        loadBitmaps()
        view = this
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val smaller = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(smaller, smaller)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        val chessBoardSide = min(width, height) * scaleFactor
        cellSide = chessBoardSide / 8f
        originX = (width - chessBoardSide) / 2f
        originY = (height - chessBoardSide) / 2f

        drawChessboard(canvas)
        drawPieces(canvas)
        MovePiece(canvas, 1,0,3,0)
    }

    // Точка начала отрисовки дебюта.
    fun MoveOpening(canvas: Canvas, opening: String)
    {

    }

    fun MovePiece(canvas: Canvas, rowStart: Int, colStart: Int, rowEnd: Int, colEnd: Int) {
        // Получить битмап фигуры.
        var bitmapPiece: Bitmap = bitmaps[imgResIDs.elementAt(1)]!!

        // Удалить ее в том месте, где она находилась.
        // Произвести отрисовку ее движения.
        // Нарисовать в том месте, где она должна появиться.

        val xStart = originX + colStart * cellSide
        val yStart = originY + (7 - rowStart) * cellSide
        val xEnd = originX + colEnd * cellSide
        val yEnd = originY + (7 - rowEnd) * cellSide

        pathMoveAlong.moveTo(xStart, yStart)
        pathMoveAlong.lineTo(xEnd, yEnd)

        val mxTransform = Matrix()
        val pm = PathMeasure(pathMoveAlong, false)
        val fSegmentLen = pm.length / 40
        if (iCurStep <= 40) {
            pm.getMatrix(fSegmentLen * iCurStep, mxTransform, PathMeasure.POSITION_MATRIX_FLAG)
            canvas.drawBitmap(bitmapPiece, mxTransform, null)
            iCurStep++
            invalidate()
        } else {
            iCurStep = 0
        }
    }

    fun MovePiece(canvas: Canvas) {
        var bitmapPiece: Bitmap = bitmaps[imgResIDs.elementAt(1)]!!

        val mxTransform = Matrix()
        val pm = PathMeasure(pathMoveAlong, false)
        val fSegmentLen = pm.length / 40
        if (iCurStep <= 40) {
            pm.getMatrix(fSegmentLen * iCurStep, mxTransform, PathMeasure.POSITION_MATRIX_FLAG)
            canvas.drawBitmap(bitmapPiece, mxTransform, null)
            iCurStep++
            invalidate()
        } else {
            iCurStep = 0
        }
    }

    fun DeletePiece() {}

    fun SetPiece() {}

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                fromCol = ((event.x - originX) / cellSide).toInt()
                fromRow = 7 - ((event.y - originY) / cellSide).toInt()

                chessDelegate?.pieceAt(Square(fromCol, fromRow))?.let {
                    movingPiece = it
                    movingPieceBitmap = bitmaps[it.resID]
                }
            }
            MotionEvent.ACTION_MOVE -> {
                movingPieceX = event.x
                movingPieceY = event.y
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                val col = ((event.x - originX) / cellSide).toInt()
                val row = 7 - ((event.y - originY) / cellSide).toInt()
                if (fromCol != col || fromRow != row) {
                    chessDelegate?.movePiece(Square(fromCol, fromRow), Square(col, row))
                }
                movingPiece = null
                movingPieceBitmap = null
                invalidate()
            }
        }
        return true
    }

    private fun drawPieces(canvas: Canvas) {
        for (row in 0 until 8)
            for (col in 0 until 8)
                chessDelegate?.pieceAt(Square(row, col))?.let { piece ->
                    if (piece != movingPiece) {
                        drawPieceAt(canvas, row, col, piece.resID)
                    }
                }

        movingPieceBitmap?.let {
            canvas.drawBitmap(
                it,
                null,
                RectF(
                    movingPieceX - cellSide / 2,
                    movingPieceY - cellSide / 2,
                    movingPieceX + cellSide / 2,
                    movingPieceY + cellSide / 2
                ),
                paint
            )
        }
    }

    private fun drawPieceAt(canvas: Canvas, row: Int, col: Int, resID: Int) {
        canvas.drawBitmap(
            bitmaps[resID]!!,
            null,
            RectF(
                originX + col * cellSide,
                originY + (7 - row) * cellSide,
                originX + (col + 1) * cellSide,
                originY + ((7 - row) + 1) * cellSide
            ),
            paint
        )
    }

    private fun loadBitmaps() {
        imgResIDs.forEach { imgResID ->
            val bitmap = resources.getDrawable(imgResID, null).toBitmap()
            bitmaps[imgResID] = bitmap
        }
    }

    private fun drawChessboard(canvas: Canvas) {
        for (row in 0 until 8)
            for (col in 0 until 8)
                drawSquareAt(canvas, row, col, (col + row) % 2 == 1)
    }

    private fun drawSquareAt(canvas: Canvas, row: Int, col: Int, isDark: Boolean) {
        paint.color = if (isDark) darkColor else lightColor
        canvas.drawRect(
            originX + col * cellSide,
            originY + row * cellSide,
            originX + (col + 1) * cellSide,
            originY + (row + 1) * cellSide,
            paint
        )
    }
}
