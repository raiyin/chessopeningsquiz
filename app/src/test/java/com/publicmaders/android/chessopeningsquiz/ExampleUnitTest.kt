package com.publicmaders.android.chessopeningsquiz

import android.util.Log
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.*
import org.junit.Test
import java.util.logging.Logger

class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun pgn_parser_isCorrect_one() {
        val parser = PgnParser()
        val actualResult = parser.parse("1. e4")
        val expectedResult: List<List<Square>> = listOf(listOf(Square(1, 4), Square(3, 4)))
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun pgn_parser_isCorrect() {
        val parser = PgnParser()
        val actualResult = parser.parse("1. e4 e5 2. Nf3 Nc6 3. Bb5")
        val expectedResult: List<List<Square>> =
            listOf(
                listOf(Square(1, 4), Square(3, 4)),
                listOf(Square(6, 4), Square(4, 4)),
                listOf(Square(0, 6), Square(2, 5)),
                listOf(Square(7, 1), Square(5, 2)),
                listOf(Square(0, 5), Square(4, 1))
            )
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun pgn_parser_isPetrovCorrect() {
        val parser = PgnParser()
        val actualResult = parser.parse("1. e4 e5 2. Nf3 Nf6 3. Nxe5 d6 4. Nf3 Nxe4 5. Bd3")
        val expectedResult: List<List<Square>> =
            listOf(
                listOf(Square(1, 4), Square(3, 4)),
                listOf(Square(6, 4), Square(4, 4)),
                listOf(Square(0, 6), Square(2, 5)),
                listOf(Square(7, 6), Square(5, 5)),
                listOf(Square(2, 5), Square(4, 4)),
                listOf(Square(6, 3), Square(5, 3)),
                listOf(Square(4, 4), Square(2, 5)),
                listOf(Square(5, 5), Square(3, 4)),
                listOf(Square(0, 5), Square(2, 3))
            )
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun pgn_parser_isBenkoAcceptedCorrect() {
        val parser = PgnParser()
        val actualResult = parser.parse("1.d4 Nf6 2.c4 c5 3.d5 b5 4.cxb5")
        val expectedResult: List<List<Square>> =
            listOf(
                listOf(Square(1, 3), Square(3, 3)),
                listOf(Square(7, 6), Square(5, 5)),
                listOf(Square(1, 2), Square(3, 2)),
                listOf(Square(6, 2), Square(4, 2)),
                listOf(Square(3, 3), Square(4, 3)),
                listOf(Square(6, 1), Square(4, 1)),
                listOf(Square(3, 2), Square(4, 1))
            )
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun pgn_parser_isCatalanOpeningOpenDefenseCorrect() {
        val parser = PgnParser()
        val actualResult = parser.parse("1.d4 Nf6 2.c4 e6 3.g3 e6 4.Nc3 dxc4 5.Bg2")
        val expectedResult: List<List<Square>> =
            listOf(
                listOf(Square(1, 3), Square(3, 3)),
                listOf(Square(7, 6), Square(5, 5)),
                listOf(Square(1, 2), Square(3, 2)),
                listOf(Square(6, 4), Square(4, 5)),
                listOf(Square(1, 6), Square(2, 6)),
                listOf(Square(6, 1), Square(4, 1)),
                listOf(Square(3, 2), Square(4, 1))
            )
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun pgn_parser_isLondonSystemCorrect() {
        val parser = PgnParser()
        val actualResult = parser.parse("1.d4 Nf6 2.Nf3 d5 3.Bf4")
        val expectedResult: List<List<Square>> =
            listOf(
                listOf(Square(1, 3), Square(3, 3)),
                listOf(Square(7, 6), Square(5, 5)),
                listOf(Square(0, 6), Square(2, 5)),
                listOf(Square(6, 3), Square(4, 3)),
                listOf(Square(0, 2), Square(3, 5))
            )
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun pgn_parser_isAntiBerlinCorrect() {
        val parser = PgnParser()
        val actualResult = parser.parse("1. e4 e5 2. Nf3 Nc6 3. Bb5 Nf6 4. d3")
        val expectedResult: List<List<Square>> =
            listOf(
                listOf(Square(1, 4), Square(3, 4)),
                listOf(Square(6, 4), Square(4, 4)),
                listOf(Square(0, 6), Square(2, 5)),
                listOf(Square(7, 1), Square(5, 2)),
                listOf(Square(0, 5), Square(4, 1)),
                listOf(Square(7, 6), Square(5, 5)),
                listOf(Square(1, 3), Square(2, 3))
            )
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun pgn_parser_isBogoIndianCorrect() {
        val parser = PgnParser()
        val actualResult = parser.parse("1. d4 Nf6 2. c4 e6 3. Nf3 Bb4+")
        val expectedResult: List<List<Square>> =
            listOf(
                listOf(Square(1, 3), Square(3, 3)),
                listOf(Square(7, 6), Square(5, 5)),
                listOf(Square(1, 2), Square(3, 2)),
                listOf(Square(6, 4), Square(4, 5)),
                listOf(Square(0, 5), Square(2, 4)),
                listOf(Square(7, 5), Square(3, 1))
            )
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

    @Test
    fun check_no_exception() {
        val om = OpeningManager()
        val openingList = om.stringOpenings.split(",\n")
        for (temp in openingList) {
            BoardState.reset()
            try {
                val jsonObject = Json.parseToJsonElement(temp).jsonObject
                val opening = Opening(
                    jsonObject["ru_type"]!!.jsonPrimitive.content,
                    jsonObject["en_type"]!!.jsonPrimitive.content,
                    jsonObject["ru_name"]!!.jsonPrimitive.content,
                    jsonObject["en_name"]!!.jsonPrimitive.content,
                    jsonObject["ru_pgn"]!!.jsonPrimitive.content,
                    jsonObject["en_pgn"]!!.jsonPrimitive.content,
                    jsonObject["eco"]!!.jsonPrimitive.content
                )

                val parser = PgnParser()
                parser.parse(opening.en_pgn)
            } catch (e: Exception) {
                println(temp + "\n" + e.message)
            }
        }
    }
}