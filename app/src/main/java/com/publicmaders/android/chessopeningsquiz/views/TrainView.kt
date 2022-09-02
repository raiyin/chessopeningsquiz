package com.publicmaders.android.chessopeningsquiz.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min
import androidx.core.graphics.drawable.toBitmap
import android.graphics.Bitmap
import android.graphics.PathMeasure
import android.widget.ListView
import com.publicmaders.android.chessopeningsquiz.controllers.PgnParser
import com.publicmaders.android.chessopeningsquiz.R
import com.publicmaders.android.chessopeningsquiz.delegates.ChessDelegate
import com.publicmaders.android.chessopeningsquiz.models.*


class TrainView(context: Context?, attrs: AttributeSet?) : View(context, attrs)
{
    private val scaleFactor = 1.0f
    private var originX = 20f
    private var originY = 200f
    private var cellSide = 130f
    private val lightColor = Color.parseColor("#b6ac9d")
    private val darkColor = Color.parseColor("#70503a")
    var openingMoves = ""
    var coordPxSize = 20

    private var view: View? = null

    // Для разблокировки после отрисовки дебюта.
    lateinit var lvOpening: ListView

    var animStepIndex = 0
    var moveIndex = 0
    private lateinit var curMovingPiece: ChessPiece

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
    var needDrawOpening: Boolean = false

    private var movingPieceBitmap: Bitmap? = null
    private var movingPiece: ChessPiece? = null
    private var movingPieceX = -1f
    private var movingPieceY = -1f
    private var movesPaths: MutableList<Triple<Int, Path, List<Square>>>

    var chessDelegate: ChessDelegate? = null

    init
    {
        loadBitmaps()
        view = this
        movesPaths = mutableListOf()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val smaller = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(smaller, smaller)
    }

    override fun onDraw(canvas: Canvas?)
    {
        canvas ?: return

        val chessBoardSide = min(width,
            height) * scaleFactor - if (Settings.CoordMode == CoordinatesMode.OUTSIDE) coordPxSize else 0
        cellSide = chessBoardSide / 8f
        originX = (width - chessBoardSide) / 2f
        originY = (height - chessBoardSide) / 2f

        drawChessboard(canvas)
        drawCoordinates(canvas, Settings.CoordMode)
        drawPieces(canvas, Settings.CoordMode)
        if (needDrawOpening)
        {
            drawPgn(canvas)
        }
    }

    private fun drawCoordinates(canvas: Canvas, coordMode: CoordinatesMode)
    {
        //calc( 8px + 4 * ((100vw - 320px) / 880));
        if (coordMode == CoordinatesMode.INSIDE)
        {
            paint.textSize = 40f;
            paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));

            val vertical = arrayOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')
            for (index in 0..7)
            {
                if (index % 2 == 0)
                {
                    paint.color = Color.parseColor("#f0d9b5")
                }
                else
                {
                    paint.color = Color.parseColor("#946f51")
                }

                canvas.drawText(vertical[index].toString(),
                    index * cellSide + coordPxSize / 2,
                    8 * cellSide - coordPxSize / 2,
                    paint)
            }

            for (index in 1..8)
            {
                if (index % 2 == 1)
                {
                    paint.color = Color.parseColor("#f0d9b5")
                }
                else
                {
                    paint.color = Color.parseColor("#946f51")
                }
                canvas.drawText(index.toString(),
                    (8 * cellSide - 1.5 * coordPxSize).toFloat(),
                    ((index - 1) * cellSide + 1.5 * coordPxSize).toFloat(),
                    paint)
            }
        }
    }

    fun initMovesPathsFromPgn(pgn: String)
    {
        val pgnParser = PgnParser()
        val moves = pgnParser.parse(pgn)
        BoardState.reset()
        movesPaths = mutableListOf()

        for (move in moves)
        {
            var piece = BoardState.pieceAt(Square(move[0].row, move[0].col)) ?: return

            var xStart = originX + move[0].col * cellSide
            var yStart = originY + (7 - move[0].row) * cellSide
            var xEnd = originX + move[1].col * cellSide
            var yEnd = originY + (7 - move[1].row) * cellSide

            var path = Path()
            path.moveTo(xStart, yStart)
            path.lineTo(xEnd, yEnd)
            movesPaths.add(Triple(piece.resID,
                path,
                listOf(Square(move[0].row, move[0].col), Square(move[1].row, move[1].col))))

            BoardState.movePiece(Square(move[0].row, move[0].col), Square(move[1].row, move[1].col))

            // Рокировка.
            if (move.count() > 2)
            {
                piece = BoardState.pieceAt(Square(move[2].row, move[2].col)) ?: return

                xStart = originX + move[2].col * cellSide
                yStart = originY + (7 - move[2].row) * cellSide
                xEnd = originX + move[3].col * cellSide
                yEnd = originY + (7 - move[3].row) * cellSide

                path = Path()
                path.moveTo(xStart, yStart)
                path.lineTo(xEnd, yEnd)
                movesPaths.add(Triple(piece.resID,
                    path,
                    listOf(Square(move[2].row, move[2].col), Square(move[3].row, move[3].col))))

                BoardState.movePiece(Square(move[2].row, move[2].col),
                    Square(move[3].row, move[3].col))
            }
        }
    }

    private fun drawPgn(canvas: Canvas)
    {
        if (moveIndex >= movesPaths.count())
        {
            moveIndex = 0
            if (this::lvOpening.isInitialized)
            {
                lvOpening.isEnabled = true
            }
            needDrawOpening = false
            BoardState.reset()
            return
        }

        if (animStepIndex == 0)
        {
            curMovingPiece = BoardState.popPiece(movesPaths[moveIndex].third[0].row,
                movesPaths[moveIndex].third[0].col)
        }

        val mxTransform = Matrix()
        val pm = PathMeasure(movesPaths[moveIndex].second, false)
        val fSegmentLen = pm.length / Settings.IterationCount
        if (animStepIndex <= Settings.IterationCount)
        {
            pm.getMatrix(fSegmentLen * animStepIndex, mxTransform, PathMeasure.POSITION_MATRIX_FLAG)

            val bitmapPiece: Bitmap = bitmaps[movesPaths[moveIndex].first]!!
            canvas.drawBitmap(bitmapPiece, mxTransform, null)
            animStepIndex++
            invalidate()
        }
        else
        {
            BoardState.pushPiece(curMovingPiece)
            BoardState.movePiece(movesPaths[moveIndex].third[0], movesPaths[moveIndex].third[1])
            animStepIndex = 0
            moveIndex++
            invalidate()
        }
    }

    private fun drawPieces(canvas: Canvas, coordMode: CoordinatesMode)
    {
        for (row in 0 until 8) for (col in 0 until 8) chessDelegate?.pieceAt(Square(row, col))
            ?.let { piece ->
                if (piece != movingPiece)
                {
                    drawPieceAt(canvas, row, col, piece.resID)
                }
            }

        movingPieceBitmap?.let {
            canvas.drawBitmap(it,
                null,
                RectF(movingPieceX - cellSide / 2,
                    movingPieceY - cellSide / 2,
                    movingPieceX + cellSide / 2,
                    movingPieceY + cellSide / 2),
                paint)
        }
    }

    private fun drawPieceAt(canvas: Canvas, row: Int, col: Int, resID: Int)
    {
        canvas.drawBitmap(bitmaps[resID]!!,
            null,
            RectF(originX + col * cellSide,
                originY + (7 - row) * cellSide,
                originX + (col + 1) * cellSide,
                originY + ((7 - row) + 1) * cellSide),
            paint)
    }

    private fun loadBitmaps()
    {
        imgResIDs.forEach { imgResID ->
            val bitmap = context.resources.getDrawable(imgResID, null).toBitmap()
            bitmaps[imgResID] = bitmap
        }
    }

    private fun drawChessboard(canvas: Canvas)
    {
        for (row in 0 until 8) for (col in 0 until 8) drawSquareAt(canvas,
            row,
            col,
            (col + row) % 2 == 1)
    }

    private fun drawSquareAt(canvas: Canvas, row: Int, col: Int, isDark: Boolean)
    {
        paint.color = if (isDark) darkColor else lightColor
        canvas.drawRect(originX + col * cellSide,
            originY + row * cellSide,
            originX + (col + 1) * cellSide,
            originY + (row + 1) * cellSide,
            paint)
    }
}
