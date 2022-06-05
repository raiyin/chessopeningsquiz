package com.publicmaders.android.chessopeningsquiz

import java.security.InvalidParameterException

// Не проанализирована рокировка

class PgnParser
{

    fun reset()
    {
        BoardState.reset()
    }

    // List<List<Square>>, потому что необходимо два поля: начальное и конечное поле фигуры.
    fun parse(pgn: String): List<List<Square>>
    {
        val moves: MutableList<List<Square>> = mutableListOf()
        val regex: Regex = """\d[.]|\s""".toRegex()
        val splits = pgn.split(regex).map { it.trim() }.filter { it.isNotEmpty() }

        for (i in splits.indices)
        {
            val move = analyzeMove(splits[i], i)
            moves.add(move)
            // Делаем ход, чтобы состояние доски изменилось.
            BoardState.makeMove(move)
        }

        return moves
    }

    /**
     * Анализирует PGN-запись одного хода.
     * @param query PGN-запись одного хода.
     * @param moveIndex - номер хода.
     * @return начальное и конечное поле фигуры.
     */
    private fun analyzeMove(query: String, moveIndex: Int): MutableList<Square>
    {
        val result: MutableList<Square> = mutableListOf()
        val player: Player = if (moveIndex % 2 == 0)
        {
            Player.WHITE
        }
        else
        {
            Player.BLACK
        }

        if (query[0].isLowerCase())
        {
            if (query.contains('x'))
            {
                // Взятие.
                var startSquare: Square =
                    if (player == Player.WHITE) Square(query[3].digitToInt() - 2,
                        columnToDigit(query[0]))
                    else Square(query[3].digitToInt(), columnToDigit(query[0]))
                val endSquare = Square(query[3].digitToInt() - 1, columnToDigit(query[2]))
                result.add(startSquare)
                result.add(endSquare)
            }
            else
            {
                // Обычный ход.
                val row: Int = query[1].digitToInt() - 1
                val column: Int = columnToDigit(query[0])
                val pawn: ChessPiece? = BoardState.getPawnThatCanGoTo(Square(row, column), player)
                if (pawn != null)
                {
                    result.add(Square(pawn.row, pawn.col))
                    result.add(Square(query[1].digitToInt() - 1, column))
                }
                else
                {
                    throw Exception("Wrong pgn parsing")
                }
            }
        }
        else if (query[0] == 'R')
        {
            val cleanQuery = query.replace("x", "").replace("+", "").replace("#", "")
            val endColumn = columnToDigit(cleanQuery.takeLast(2)[0])
            val endRow = cleanQuery.last().digitToInt() - 1
            val endSquare = Square(endRow, endColumn)

            val rooks = BoardState.getRookThatCanGoTo(endSquare, player)

            when
            {
                cleanQuery.length == 3 ->
                {
                    // Вариант для ладьи только один.
                    val startColumn = rooks[0].col
                    val startRow = rooks[0].row
                    val startSquare = Square(startRow, startColumn)
                    result.add(startSquare)
                    result.add(endSquare)
                }
                cleanQuery[1].isLetter() ->
                {
                    // Неоднозначность по столбцу.
                    val startColumn = columnToDigit(cleanQuery[1])
                    val startRow = rooks.filter { it.col == startColumn }[0].row
                    val startSquare = Square(startRow, startColumn)
                    result.add(startSquare)
                    result.add(endSquare)
                }
                cleanQuery[1].isDigit() ->
                {
                    // Неоднозначность по строке.
                    val startRow = cleanQuery[1].digitToInt() - 1
                    val startColumn = rooks.filter { it.row == startRow }[0].col
                    val startSquare = Square(startRow, startColumn)
                    result.add(startSquare)
                    result.add(endSquare)
                }
                else ->
                {
                    throw Exception("Error pgn parsing: $cleanQuery")
                }
            }
        }
        else if (query[0] == 'N')
        {
            val cleanQuery = query.replace("x", "").replace("+", "").replace("#", "")
            val endColumn = columnToDigit(cleanQuery.takeLast(2)[0])
            val endRow = cleanQuery.last().digitToInt() - 1
            val endSquare = Square(endRow, endColumn)

            val knights = BoardState.getKnightThatCanGoTo(endSquare, player)

            when
            {
                cleanQuery.length == 3 ->
                {
                    // Вариант только один.
                    val startColumn = knights[0].col
                    val startRow = knights[0].row
                    val startSquare = Square(startRow, startColumn)
                    result.add(startSquare)
                    result.add(endSquare)
                }
                cleanQuery[1].isLetter() ->
                {
                    // Неоднозначность по столбцу.
                    val startColumn = columnToDigit(cleanQuery[1])
                    val startRow = knights.filter { it.col == startColumn }[0].row
                    val startSquare = Square(startRow, startColumn)
                    result.add(startSquare)
                    result.add(endSquare)
                }
                cleanQuery[1].isDigit() ->
                {
                    // Неоднозначность по строке.
                    val startRow = cleanQuery[1].digitToInt() - 1
                    val startColumn = knights.filter { it.row == startRow }[0].col
                    val startSquare = Square(startRow, startColumn)
                    result.add(startSquare)
                    result.add(endSquare)
                }
                else ->
                {
                    throw Exception("Error pgn parsing: $cleanQuery")
                }
            }
        }
        else if (query[0] == 'B')
        {
            val cleanQuery = query.replace("x", "").replace("+", "").replace("#", "")
            val endColumn = columnToDigit(cleanQuery.takeLast(2)[0])
            val endRow = cleanQuery.last().digitToInt() - 1
            val endSquare = Square(endRow, endColumn)

            val bishops = BoardState.getBishopThatCanGoTo(endSquare, player)

            when
            {
                cleanQuery.length == 3 ->
                {
                    // Вариант только один.
                    val startColumn = bishops[0].col
                    val startRow = bishops[0].row
                    val startSquare = Square(startRow, startColumn)
                    result.add(startSquare)
                    result.add(endSquare)
                }
                cleanQuery[1].isLetter() ->
                {
                    // Неоднозначность по столбцу.
                    val startColumn = columnToDigit(cleanQuery[1])
                    val startRow = bishops.filter { it.col == startColumn }[0].row
                    val startSquare = Square(startRow, startColumn)
                    result.add(startSquare)
                    result.add(endSquare)
                }
                cleanQuery[1].isDigit() ->
                {
                    // Неоднозначность по строке.
                    val startRow = cleanQuery[1].digitToInt() - 1
                    val startColumn = bishops.filter { it.row == startRow }[0].col
                    val startSquare = Square(startRow, startColumn)
                    result.add(startSquare)
                    result.add(endSquare)
                }
                else ->
                {
                    throw Exception("Error pgn parsing: $cleanQuery")
                }
            }
        }
        else if (query[0] == 'K')
        {
            val cleanQuery = query.replace("x", "").replace("+", "").replace("#", "")
            val endColumn = columnToDigit(cleanQuery.takeLast(2)[0])
            val endRow = cleanQuery.last().digitToInt() - 1
            val endSquare = Square(endRow, endColumn)

            val king = BoardState.getKingThatCanGoTo(endSquare, player)
            val startColumn = king?.col
            val startRow = king?.row
            val startSquare = startColumn?.let { startRow?.let { it1 -> Square(it, it1) } }
            startSquare?.let { result.add(it) }
            result.add(endSquare)
        }
        else if (query[0] == 'Q')
        {

            val cleanQuery = query.replace("x", "").replace("+", "").replace("#", "")
            val endColumn = columnToDigit(cleanQuery.takeLast(2)[0])
            val endRow = cleanQuery.last().digitToInt() - 1
            val endSquare = Square(endRow, endColumn)

            val queens = BoardState.getQueenThatCanGoTo(endSquare, player)

            when
            {
                cleanQuery.length == 3 ->
                {
                    // Вариант только один.
                    val startColumn = queens[0].col
                    val startRow = queens[0].row
                    val startSquare = Square(startRow, startColumn)
                    result.add(startSquare)
                    result.add(endSquare)
                }
                cleanQuery[1].isLetter() ->
                {
                    // Неоднозначность по столбцу.
                    val startColumn = columnToDigit(cleanQuery[1])
                    val startRow = queens.filter { it.col == startColumn }[0].row
                    val startSquare = Square(startRow, startColumn)
                    result.add(startSquare)
                    result.add(endSquare)
                }
                cleanQuery[1].isDigit() ->
                {
                    // Неоднозначность по строке.
                    val startRow = cleanQuery[1].digitToInt() - 1
                    val startColumn = queens.filter { it.row == startRow }[0].col
                    val startSquare = Square(startRow, startColumn)
                    result.add(startSquare)
                    result.add(endSquare)
                }
                else ->
                {
                    throw Exception("Error pgn parsing: $cleanQuery")
                }
            }
        }
        else if (query.contains("0-0"))
        {
            // Сначала ходит король.
            var startRow = if (player == Player.WHITE) 0 else 7
            var startColumn = 4
            var startSquare = Square(startRow, startColumn)
            var endRow = startRow
            var endColumn = 6
            var endSquare = Square(endRow, endColumn)
            result.add(startSquare)
            result.add(endSquare)

            // Потом ходит ладья.
            startRow = if (player == Player.WHITE) 0 else 7
            startColumn = 7
            startSquare = Square(startRow, startColumn)
            endRow = startRow
            endColumn = 5
            endSquare = Square(endRow, endColumn)
            result.add(startSquare)
            result.add(endSquare)
        }
        else if (query.contains("0-0-0"))
        {
            // Сначала ходит король.
            var startRow = if (player == Player.WHITE) 0 else 7
            var startColumn = 4
            var startSquare = Square(startRow, startColumn)
            var endRow = startRow
            var endColumn = 2
            var endSquare = Square(endRow, endColumn)
            result.add(startSquare)
            result.add(endSquare)

            // Потом ходит ладья.
            startRow = if (player == Player.WHITE) 0 else 7
            startColumn = 0
            startSquare = Square(startRow, startColumn)
            endRow = startRow
            endColumn = 3
            endSquare = Square(endRow, endColumn)
            result.add(startSquare)
            result.add(endSquare)
        }
        else
        {
            throw Exception("Wrong pgn piece parsing: $query")
        }
        return result
    }

    fun columnToDigit(col: Char): Int
    {
        return when (col)
        {
            'a' -> 0
            'b' -> 1
            'c' -> 2
            'd' -> 3
            'e' -> 4
            'f' -> 5
            'g' -> 6
            'h' -> 7
            else ->
            {
                throw InvalidParameterException("No such column's name: $col")
            }
        }
    }

}