package com.publicmaders.android.chessopeningsquiz

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min
import androidx.core.graphics.drawable.toBitmap
import android.graphics.Bitmap
import android.graphics.PathMeasure

//import androidx.core.content.res.ResourcesCompat

class TrainView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val scaleFactor = 1.0f
    private var originX = 20f
    private var originY = 200f
    private var cellSide = 130f
    private val lightColor = Color.parseColor("#b6ac9d")
    private val darkColor = Color.parseColor("#70503a")
    var mustDrawMoves = false
    var openingMoves = ""

    private var view: View? = null

    private var animStepIndex = 0
    private var moveIndex = 0
    private lateinit var curMovingPiece: ChessPiece

    private var pathMoveAlong = Path()
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

    private var movingPieceBitmap: Bitmap? = null
    private var movingPiece: ChessPiece? = null
    private var fromCol: Int = -1
    private var fromRow: Int = -1
    private var movingPieceX = -1f
    private var movingPieceY = -1f

    // Piece resId, move path, start and finish squares.
    private lateinit var movesPaths: MutableList<Triple<Int, Path, List<Square>>>

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
        if (mustDrawMoves) {
            drawPgn(canvas)
        }
    }

    fun initMovesPathsFromPgn(pgn: String) {
        var pgnParser = PgnParser()
        val moves = pgnParser.parse(pgn)
        BoardState.reset()
        movesPaths = mutableListOf()

        for (move in moves) {
            val piece = BoardState.pieceAt(Square(move[0].row, move[0].col)) ?: return

            val xStart = originX + move[0].col * cellSide
            val yStart = originY + (7 - move[0].row) * cellSide
            val xEnd = originX + move[1].col * cellSide
            val yEnd = originY + (7 - move[1].row) * cellSide

            var path = Path()
            path.moveTo(xStart, yStart)
            path.lineTo(xEnd, yEnd)
            movesPaths.add(
                Triple(
                    piece.resID,
                    path,
                    listOf(Square(move[0].row, move[0].col), Square(move[1].row, move[1].col))
                )
            )

            BoardState.movePiece(Square(move[0].row, move[0].col), Square(move[1].row, move[1].col))
        }
    }

    private fun drawPgn(canvas: Canvas) {
        if (moveIndex >= movesPaths.count()) {
            moveIndex = 0
            return
        }

        if (animStepIndex == 0) {
            curMovingPiece = BoardState.pop_piece(
                movesPaths[moveIndex].third[0].row,
                movesPaths[moveIndex].third[0].col
            )
        }

        val mxTransform = Matrix()
        val pm = PathMeasure(movesPaths[moveIndex].second, false)
        val fSegmentLen = pm.length / 40
        if (animStepIndex <= 40) {
            pm.getMatrix(fSegmentLen * animStepIndex, mxTransform, PathMeasure.POSITION_MATRIX_FLAG)

            val bitmapPiece: Bitmap = bitmaps[movesPaths[moveIndex].first]!!
            canvas.drawBitmap(bitmapPiece, mxTransform, null)
            animStepIndex++
            invalidate()
        } else {
            BoardState.push_piece(curMovingPiece)
            BoardState.movePiece(movesPaths[moveIndex].third[0], movesPaths[moveIndex].third[1])
            animStepIndex = 0
            moveIndex++
            invalidate()
        }
    }

    private fun movePiece(canvas: Canvas, rowStart: Int, colStart: Int, rowEnd: Int, colEnd: Int) {
        // Получить битмап фигуры.
        // var bitmapPiece: Bitmap = bitmaps[imgResIDs.elementAt(1)]!!
        val piece = BoardState.pieceAt(Square(rowStart, colStart)) ?: return
        val bitmapPiece: Bitmap = bitmaps[piece.resID]!!

        // Удалить фигуру в классе состояния доски и отрисовать изменения.
        //BoardState.pop_piece(rowStart, colStart)
        //drawPieces(canvas)

        // Произвести отрисовку ее движения.
        val xStart = originX + colStart * cellSide
        val yStart = originY + (7 - rowStart) * cellSide
        val xEnd = originX + colEnd * cellSide
        val yEnd = originY + (7 - rowEnd) * cellSide

        pathMoveAlong.moveTo(xStart, yStart)
        pathMoveAlong.lineTo(xEnd, yEnd)

        val mxTransform = Matrix()
        val pm = PathMeasure(pathMoveAlong, false)
        val fSegmentLen = pm.length / 40
        if (animStepIndex <= 40) {
            pm.getMatrix(fSegmentLen * animStepIndex, mxTransform, PathMeasure.POSITION_MATRIX_FLAG)
            canvas.drawBitmap(bitmapPiece, mxTransform, null)
            animStepIndex++
            invalidate()
        } else {
            animStepIndex = 0
        }

        // Вернуть фигуру на доску и сделать ею ход в классе доски и отрисовать.
        //BoardState.push_piece(piece)
        //BoardState.movePiece(Square(rowStart, colStart), Square(rowEnd, colEnd))
        //drawPieces(canvas)
    }

    private fun movePiece(canvas: Canvas) {
        val bitmapPiece: Bitmap = bitmaps[imgResIDs.elementAt(1)]!!

        val xStart = originX + 0 * cellSide
        val yStart = originY + (7 - 1) * cellSide
        val xEnd = originX + 0 * cellSide
        val yEnd = originY + (7 - 3) * cellSide

        pathMoveAlong.moveTo(xStart, yStart)
        pathMoveAlong.lineTo(xEnd, yEnd)

        val mxTransform = Matrix()
        val pm = PathMeasure(pathMoveAlong, false)
        val fSegmentLen = pm.length / 40
        if (animStepIndex <= 40) {
            pm.getMatrix(fSegmentLen * animStepIndex, mxTransform, PathMeasure.POSITION_MATRIX_FLAG)
            canvas.drawBitmap(bitmapPiece, mxTransform, null)
            animStepIndex++
            invalidate()
        } else {
            animStepIndex = 0
        }
    }

    //override fun onTouchEvent(event: MotionEvent?): Boolean {
    //    event ?: return false
    //
    //    when (event.action) {
    //        MotionEvent.ACTION_DOWN -> {
    //            fromCol = ((event.x - originX) / cellSide).toInt()
    //            fromRow = 7 - ((event.y - originY) / cellSide).toInt()
    //
    //            chessDelegate?.pieceAt(Square(fromCol, fromRow))?.let {
    //                movingPiece = it
    //                movingPieceBitmap = bitmaps[it.resID]
    //            }
    //        }
    //        MotionEvent.ACTION_MOVE -> {
    //            movingPieceX = event.x
    //            movingPieceY = event.y
    //            invalidate()
    //        }
    //        MotionEvent.ACTION_UP -> {
    //            val col = ((event.x - originX) / cellSide).toInt()
    //            val row = 7 - ((event.y - originY) / cellSide).toInt()
    //            if (fromCol != col || fromRow != row) {
    //                chessDelegate?.movePiece(Square(fromCol, fromRow), Square(col, row))
    //            }
    //            movingPiece = null
    //            movingPieceBitmap = null
    //            invalidate()
    //        }
    //    }
    //    return true
    //}

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
