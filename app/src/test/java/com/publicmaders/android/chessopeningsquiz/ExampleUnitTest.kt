package com.publicmaders.android.chessopeningsquiz

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun pgn_parser_isCorrect_one() {
        val parser = PgnParser()
        val actualResult = parser.parse("1. e4")
        val expectedResult: List<List<Square>> = listOf(listOf(Square(1, 4),Square(3, 4)))
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun pgn_parser_isCorrect() {
        val parser = PgnParser()
        val actualResult = parser.parse("1. e4 e5 2. Nf3 Nc6 3. Bb5")
        val expectedResult: List<List<Square>> =
            listOf(
                listOf(Square(1, 4),Square(3, 4)),
                listOf(Square(6, 4),Square(4, 4)),
                listOf(Square(0, 6),Square(2, 5)),
                listOf(Square(7, 1),Square(5, 2)),
                listOf(Square(0, 5),Square(4, 1)))
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun pgn_parser_isPetrovCorrect() {
        val parser = PgnParser()
        val actualResult = parser.parse("1. e4 e5 2. Nf3 Nf6 3. Nxe5 d6 4. Nf3 Nxe4 5. Bd3")
        val expectedResult: List<List<Square>> =
            listOf(
                listOf(Square(1, 4),Square(3, 4)),
                listOf(Square(6, 4),Square(4, 4)),
                listOf(Square(0, 6),Square(2, 5)),
                listOf(Square(7, 6),Square(5, 5)),
                listOf(Square(2, 5),Square(4, 4)),
                listOf(Square(6, 3),Square(5, 3)),
                listOf(Square(4, 4),Square(2, 5)),
                listOf(Square(5, 5),Square(3, 4)),
                listOf(Square(0, 5),Square(2, 3)))
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun column_to_digit_isCorrect() {
        val parser = PgnParser()
        var actualResult = parser.columnToDigit('a').toString()
        actualResult += parser.columnToDigit('b').toString()
        actualResult += parser.columnToDigit('c').toString()
        actualResult += parser.columnToDigit('d').toString()
        actualResult += parser.columnToDigit('e').toString()
        actualResult += parser.columnToDigit('f').toString()
        actualResult += parser.columnToDigit('g').toString()
        actualResult += parser.columnToDigit('h').toString()
        val expectedResult = "01234567"
        assertEquals(expectedResult, actualResult)
    }
}