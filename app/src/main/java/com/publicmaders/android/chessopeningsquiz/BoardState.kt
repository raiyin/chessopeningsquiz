package com.publicmaders.android.chessopeningsquiz

import java.lang.Integer.*
import kotlin.math.abs

//class BoardState {
object BoardState {
    private var piecesBox = mutableSetOf<ChessPiece>()

    init {
        reset()
    }

    private fun addPiece(piece: ChessPiece) {
        piecesBox.add(piece)
    }

    fun reset() {
        piecesBox.clear()
        for (i in 0 until 2) {
            addPiece(ChessPiece(0, 0 + i * 7, Player.WHITE, Chessman.ROOK, R.drawable.ic_chess_wr))
            addPiece(ChessPiece(7, 0 + i * 7, Player.BLACK, Chessman.ROOK, R.drawable.ic_chess_br))
            addPiece(
                ChessPiece(
                    0,
                    1 + i * 5,
                    Player.WHITE,
                    Chessman.KNIGHT,
                    R.drawable.ic_chess_wn
                )
            )
            addPiece(
                ChessPiece(
                    7,
                    1 + i * 5,
                    Player.BLACK,
                    Chessman.KNIGHT,
                    R.drawable.ic_chess_bn
                )
            )
            addPiece(
                ChessPiece(
                    0,
                    2 + i * 3,
                    Player.WHITE,
                    Chessman.BISHOP,
                    R.drawable.ic_chess_wb
                )
            )
            addPiece(
                ChessPiece(
                    7,
                    2 + i * 3,
                    Player.BLACK,
                    Chessman.BISHOP,
                    R.drawable.ic_chess_bb
                )
            )
        }

        for (i in 0 until 8) {
            addPiece(ChessPiece(1, i, Player.WHITE, Chessman.PAWN, R.drawable.ic_chess_wp))
            addPiece(ChessPiece(6, i, Player.BLACK, Chessman.PAWN, R.drawable.ic_chess_bp))
        }

        addPiece(ChessPiece(0, 3, Player.WHITE, Chessman.QUEEN, R.drawable.ic_chess_wq))
        addPiece(ChessPiece(7, 3, Player.BLACK, Chessman.QUEEN, R.drawable.ic_chess_bq))
        addPiece(ChessPiece(0, 4, Player.WHITE, Chessman.KING, R.drawable.ic_chess_wk))
        addPiece(ChessPiece(7, 4, Player.BLACK, Chessman.KING, R.drawable.ic_chess_bk))
    }

    fun pop_piece(row: Int, col: Int): ChessPiece {
        val piece = piecesBox.filter { piece -> piece.row == row && piece.col == col }[0]
        piecesBox.remove(piece)
        return piece
    }

    fun push_piece(piece: ChessPiece) {
        piecesBox.add(piece)
    }

    private fun getPieceFrom(square: Square): ChessPiece? {
        for (piece in piecesBox) {
            if (piece.col == square.col && piece.row == square.row) {
                return piece
            }
        }
        return null
    }

    fun getPawnThatCanGoTo(square: Square, player: Player): ChessPiece? {
        if (player == Player.WHITE) {
            var tempSquare = Square(square.row - 1, square.col)
            if (getPieceFrom(tempSquare) != null) {
                return getPieceFrom(tempSquare)
            }

            // Проверяем для случая хода на два ряда.
            tempSquare = Square(square.row - 2, square.col)
            if (square.row == 3 && getPieceFrom(tempSquare) != null) {
                return getPieceFrom(tempSquare)
            }
            return null
        } else {
            var tempSquare = Square(square.row + 1, square.col)
            if (getPieceFrom(tempSquare) != null) {
                return getPieceFrom(tempSquare)
            }

            // Проверяем для случая хода на два ряда.
            tempSquare = Square(square.row + 2, square.col)
            if (square.row == 4 && getPieceFrom(tempSquare) != null) {
                return getPieceFrom(tempSquare)
            }
            return null
        }
    }

    fun getPawnsThatCanTakeOn(square: Square, player: Player): MutableList<ChessPiece> {
        var result: MutableList<ChessPiece> = mutableListOf()

        if (player == Player.WHITE) {
            // Слева.
            var tempSquare = Square(square.row - 1, square.col - 1)
            var tempPiece = getPieceFrom(tempSquare)
            if (tempPiece != null && tempPiece.player == player) {
                result.add(tempPiece)
            }

            // Справа.
            tempSquare = Square(square.row - 1, square.col + 1)
            tempPiece = getPieceFrom(tempSquare)
            if (tempPiece != null && tempPiece.player == player) {
                result.add(tempPiece)
            }
        } else {
            // Слева.
            var tempSquare = Square(square.row + 1, square.col - 1)
            var tempPiece = getPieceFrom(tempSquare)
            if (tempPiece != null && tempPiece.player == player) {
                result.add(tempPiece)
            }

            // Справа.
            tempSquare = Square(square.row + 1, square.col + 1)
            tempPiece = getPieceFrom(tempSquare)
            if (tempPiece != null && tempPiece.player == player) {
                result.add(tempPiece)
            }
        }

        return result
    }

    fun getRookThatCanGoTo(square: Square, player: Player): MutableList<ChessPiece> {
        val resultList: MutableList<ChessPiece> = mutableListOf()

        // Перебираем ряд
        for (col in 0..7) {
            if (col != square.col) {
                val tempSquare = Square(square.row, col)
                val tempPiece = getPieceFrom(tempSquare)
                if (tempPiece?.chessman == Chessman.ROOK && isRowCleanBetween(
                        tempSquare,
                        square
                    ) && tempPiece.player == player
                ) {
                    resultList.add(tempPiece)
                }
            }
        }

        // Перебираем столбец
        for (row in 0..7) {
            if (row != square.row) {
                val tempSquare = Square(row, square.col)
                val tempPiece = getPieceFrom(tempSquare)
                if (tempPiece?.chessman == Chessman.ROOK && isColumnCleanBetween(
                        tempSquare,
                        square
                    ) && tempPiece.player == player
                ) {
                    resultList.add(tempPiece)
                }
            }
        }

        return resultList
    }

    fun getKnightThatCanGoTo(square: Square, player: Player): MutableList<ChessPiece> {

        val resultList: MutableList<ChessPiece> = mutableListOf()
        val pairList = mutableListOf<Pair<Int, Int>>()
        pairList.add(Pair(1, -2))
        pairList.add(Pair(2, -1))
        pairList.add(Pair(2, 1))
        pairList.add(Pair(1, 2))
        pairList.add(Pair(-1, 2))
        pairList.add(Pair(-2, 1))
        pairList.add(Pair(-2, -1))
        pairList.add(Pair(-1, -2))

        for (pair in pairList) {
            val tempSquare = Square(square.row + pair.second, square.col + pair.first)
            val tempPiece = getPieceFrom(tempSquare)
            if (tempPiece?.chessman == Chessman.KNIGHT && tempPiece.player == player) {
                resultList.add(tempPiece)
            }
        }
        return resultList
    }

    fun getBishopThatCanGoTo(square: Square, player: Player): MutableList<ChessPiece> {
        val resultList: MutableList<ChessPiece> = mutableListOf()


        // Диагональ вправо вверх.
        var index = 1
        while (square.row + index < 8 && square.col + index < 8) {
            val tempSquare = Square(square.row + index, square.col + index)
            val tempPiece = getPieceFrom(tempSquare)
            if (tempPiece?.chessman == Chessman.BISHOP &&
                tempPiece.player == player &&
                isDiagonalCleanBetween(tempSquare, square)
            ) {
                resultList.add(tempPiece)
                break
            }
            index++
        }

        // Диагональ вправо вниз.
        index = 1
        while (square.row - index >= 0 && square.col + index < 8) {
            val tempSquare = Square(square.row - index, square.col + index)
            val tempPiece = getPieceFrom(tempSquare)
            if (tempPiece?.chessman == Chessman.BISHOP &&
                tempPiece.player == player &&
                isDiagonalCleanBetween(tempSquare, square)
            ) {
                resultList.add(tempPiece)
                break
            }
            index++
        }

        // Диагональ влево вниз.
        index = 1
        while (square.row - index >= 0 && square.col - index >= 0) {
            val tempSquare = Square(square.row - index, square.col - index)
            val tempPiece = getPieceFrom(tempSquare)
            if (tempPiece?.chessman == Chessman.BISHOP &&
                tempPiece.player == player &&
                isDiagonalCleanBetween(tempSquare, square)
            ) {
                resultList.add(tempPiece)
                break
            }
            index++
        }

        // Диагональ влево вверх.
        index = 1
        while (square.row + index < 8 && square.col - index >= 0) {
            val tempSquare = Square(square.row + index, square.col - index)
            val tempPiece = getPieceFrom(tempSquare)
            if (tempPiece?.chessman == Chessman.BISHOP &&
                tempPiece.player == player &&
                isDiagonalCleanBetween(tempSquare, square)
            ) {
                resultList.add(tempPiece)
                break
            }
            index++
        }

        return resultList
    }

    fun getKingThatCanGoTo(square: Square, player: Player): ChessPiece? {
        for (row in -1..1) {
            for (col in -1..1) {
                val tempSquare = Square(square.row + row, square.col + col)
                val tempPiece = getPieceFrom(tempSquare)
                if (tempPiece != null && tempPiece.chessman == Chessman.KING && tempPiece.player == player) {
                    return tempPiece
                }
            }
        }
        return null
    }

    fun getQueenThatCanGoTo(square: Square, player: Player): MutableList<ChessPiece> {
        val resultList: MutableList<ChessPiece> = mutableListOf()

        // Проверяем ряды и столбцы.
        for (col in 0..7) {
            if (col != square.col) {
                val tempSquare = Square(square.row, col)
                val tempPiece = getPieceFrom(tempSquare)
                if (tempPiece?.chessman == Chessman.QUEEN && isRowCleanBetween(
                        tempSquare,
                        square
                    ) && tempPiece.player == player
                ) {
                    resultList.add(tempPiece)
                }
            }
        }

        for (row in 0..7) {
            if (row != square.row) {
                val tempSquare = Square(row, square.col)
                val tempPiece = getPieceFrom(tempSquare)
                if (tempPiece?.chessman == Chessman.QUEEN && isColumnCleanBetween(
                        tempSquare,
                        square
                    ) && tempPiece.player == player
                ) {
                    resultList.add(tempPiece)
                }
            }
        }

        // Проверяем диагонали.
        var index = 0

        // Диагональ вправо вверх.
        while (square.col + index < 8 && square.row + index < 8) {
            val tempSquare = Square(square.row + index, square.col + index)
            val tempPiece = getPieceFrom(tempSquare)
            if (tempPiece?.chessman == Chessman.QUEEN &&
                tempPiece.player == player &&
                isDiagonalCleanBetween(tempSquare, square)
            ) {
                resultList.add(tempPiece)
            }
            index++
        }

        // Диагональ вправо вниз.
        index = 0
        while (square.col + index < 8 && square.row - index >= 0) {
            val tempSquare = Square(square.row - index, square.col + index)
            val tempPiece = getPieceFrom(tempSquare)
            if (tempPiece?.chessman == Chessman.QUEEN &&
                tempPiece.player == player &&
                isDiagonalCleanBetween(tempSquare, square)
            ) {
                resultList.add(tempPiece)
            }
            index++
        }

        // Диагональ влево вниз.
        index = 0
        while (square.col - index >= 0 && square.row - index >= 0) {
            val tempSquare = Square(square.row - index, square.col - index)
            val tempPiece = getPieceFrom(tempSquare)
            if (tempPiece?.chessman == Chessman.QUEEN &&
                tempPiece.player == player &&
                isDiagonalCleanBetween(tempSquare, square)
            ) {
                resultList.add(tempPiece)
            }
            index++
        }

        // Диагональ влево вверх.
        index = 0
        while (square.col - index >= 0 && square.row + index < 8) {
            val tempSquare = Square(square.row + index, square.col - index)
            val tempPiece = getPieceFrom(tempSquare)
            if (tempPiece?.chessman == Chessman.QUEEN &&
                tempPiece.player == player &&
                isDiagonalCleanBetween(tempSquare, square)
            ) {
                resultList.add(tempPiece)
            }
            index++
        }

        return resultList
    }

    private fun isRowCleanBetween(squareOne: Square, squareTwo: Square): Boolean {
        if (squareOne.row != squareTwo.row)
            throw Exception("There are diff rows in args")

        val minCol = min(squareOne.col, squareTwo.col)
        val maxCol = max(squareOne.col, squareTwo.col)

        if (minCol == maxCol || maxCol == minCol + 1)
            return true

        for (i in (minCol + 1) until maxCol) {
            if (getPieceFrom(Square(squareOne.row, i)) != null) {
                return false
            }
        }
        return true
    }

    private fun isColumnCleanBetween(squareOne: Square, squareTwo: Square): Boolean {
        if (squareOne.col != squareTwo.col)
            throw Exception("There are diff columns in args")

        val minRow = min(squareOne.row, squareTwo.row)
        val maxRow = max(squareOne.row, squareTwo.row)

        if (minRow == maxRow || maxRow == minRow + 1)
            return true

        for (row in (minRow + 1) until maxRow) {
            if (getPieceFrom(Square(row, squareOne.col)) != null) {
                return false
            }
        }
        return true
    }

    private fun isDiagonalCleanBetween(squareOne: Square, squareTwo: Square): Boolean {
        if (squareOne.row == squareTwo.row && squareOne.col == squareTwo.col) {
            return true
        }

        // Делаем, чтобы у второго поля столбец всегда был больше. Для краткости алгоритма.
        var sOne: Square = squareOne
        var sTwo: Square = squareTwo

        if (squareOne.col > squareTwo.col) {
            sOne = squareTwo
            sTwo = squareOne
        }

        if (abs(sOne.row - sTwo.row) != abs(sOne.col - sTwo.col)) {
            throw Exception("Squares are not on the diagonal")
        }

        if (sOne.row < sTwo.row) {
            for (index in 1 until (sTwo.row - sOne.row)) {
                if (getPieceFrom(Square(sOne.row + index, sOne.col + index)) != null) {
                    return false
                }
            }
            return true
        } else {
            for (index in 1 until (sOne.row - sTwo.row)) {
                if (getPieceFrom(Square(sOne.row - index, sOne.col + index)) != null) {
                    return false
                }
            }
            return true
        }
    }

    private fun deletePieceFromField(square: Square) {
        piecesBox.removeIf { it.row == square.row && it.col == square.col }
    }

    fun makeMove(move: MutableList<Square>) {
        if (getPieceFrom(move[1]) != null) {
            deletePieceFromField(move[1])
        }

        val tempPiece = piecesBox.find { it.row == move[0].row && it.col == move[0].col }
        piecesBox.removeIf { it.row == move[0].row && it.col == move[0].col }
        val piece = tempPiece?.let {
            ChessPiece(
                move[1].row,
                move[1].col,
                tempPiece.player,
                tempPiece.chessman,
                tempPiece.resID
            )
        }
        if (piece != null) {
            piecesBox.add(piece)
        }
    }

    fun pieceAt(square: Square): ChessPiece? {
        return pieceAt(square.row, square.col)
    }

    private fun pieceAt(row: Int, col: Int): ChessPiece? {
        for (piece in piecesBox) {
            if (row == piece.row && col == piece.col) {
                return piece
            }
        }
        return null
    }

    private fun isClearVerticallyBetween(from: Square, to: Square): Boolean {
        if (from.col != to.col) return false
        val gap = abs(from.row - to.row) - 1
        if (gap == 0) return true
        for (i in 1..gap) {
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            if (pieceAt(Square(nextRow, from.col)) != null) {
                return false
            }
        }
        return true
    }

    private fun isClearHorizontallyBetween(from: Square, to: Square): Boolean {
        if (from.row != to.row) return false
        val gap = abs(from.col - to.col) - 1
        if (gap == 0) return true
        for (i in 1..gap) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            if (pieceAt(Square(from.row, nextCol)) != null) {
                return false
            }
        }
        return true
    }

    private fun isClearDiagonally(from: Square, to: Square): Boolean {
        if (abs(from.col - to.col) != abs(from.row - to.row)) return false
        val gap = abs(from.col - to.col) - 1
        for (i in 1..gap) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            if (pieceAt(nextRow, nextCol) != null) {
                return false
            }
        }
        return true
    }

    private fun canKnightMove(from: Square, to: Square): Boolean {
        return abs(from.col - to.col) == 2 && abs(from.row - to.row) == 1 ||
                abs(from.col - to.col) == 1 && abs(from.row - to.row) == 2
    }

    private fun canRookMove(from: Square, to: Square): Boolean {
        if (from.col == to.col && isClearVerticallyBetween(from, to) ||
            from.row == to.row && isClearHorizontallyBetween(from, to)
        ) {
            return true
        }
        return false
    }

    private fun canBishopMove(from: Square, to: Square): Boolean {
        if (abs(from.col - to.col) == abs(from.row - to.row)) {
            return isClearDiagonally(from, to)
        }
        return false
    }

    private fun canQueenMove(from: Square, to: Square): Boolean {
        return canRookMove(from, to) || canBishopMove(from, to)
    }

    private fun canKingMove(from: Square, to: Square): Boolean {
        if (canQueenMove(from, to)) {
            val deltaCol = abs(from.col - to.col)
            val deltaRow = abs(from.row - to.row)
            return deltaCol == 1 && deltaRow == 1 || deltaCol + deltaRow == 1
        }
        return false
    }

    private fun canPawnMove(from: Square, to: Square, player: Player): Boolean {
        return if (player == Player.WHITE) {
            if (from.row == 1) {
                to.row == 2 || to.row == 3
            } else {
                to.row == from.row + 1
            }
        } else {
            if (from.row == 6) {
                to.row == 5 || to.row == 4
            } else {
                to.row == from.row - 1
            }
        }
    }

    fun canMove(from: Square, to: Square): Boolean {
        if (from.col == to.col && from.row == to.row) {
            return false
        }
        val movingPiece = pieceAt(from) ?: return false
        return when (movingPiece.chessman) {
            Chessman.KNIGHT -> canKnightMove(from, to)
            Chessman.ROOK -> canRookMove(from, to)
            Chessman.BISHOP -> canBishopMove(from, to)
            Chessman.QUEEN -> canQueenMove(from, to)
            Chessman.KING -> canKingMove(from, to)
            Chessman.PAWN -> canPawnMove(from, to, movingPiece.player)
        }
    }

    fun movePiece(from: Square, to: Square) {
        if (canMove(from, to)) {
            movePiece(from.row, from.col, to.row, to.col)
        }
    }

    private fun movePiece(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int) {
        if (fromCol == toCol && fromRow == toRow) return
        val movingPiece = pieceAt(fromRow, fromCol) ?: return

        pieceAt(toRow, toCol)?.let {
            if (it.player == movingPiece.player) {
                return
            }
            piecesBox.remove(it)
        }

        piecesBox.remove(movingPiece)
        addPiece(movingPiece.copy(row = toRow, col = toCol))
    }
}