package com.publicmaders.android.chessopeningsquiz.controllers

import android.util.Log
import com.publicmaders.android.chessopeningsquiz.models.Opening
import com.publicmaders.android.chessopeningsquiz.models.Settings
import java.sql.Time
import java.util.*
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class QuizManager(openingManager: OpeningManager) {
    // Quiz - все, например, 10 вопросов вместе.
    // Task - один из, например, 10 вопросов.
    // MutableMap<Opening, Boolean>, так как содержит верный и неверные варианты.
    private var quiz = mutableListOf<MutableMap<Opening, Boolean>>()
    var rightAnswersCount: Int = 0
    var currentTaskNumber: Int = 0
    private var openingManager: OpeningManager
    private val random: Random = Random(Date().time.milliseconds.inWholeMilliseconds)

    init {
        this.openingManager = openingManager
        for (taskIndex in 0 until Settings.TasksCount) {

            val task: MutableMap<Opening, Boolean> = mutableMapOf()
            val rightIndex: Int = random.nextInt(0, 4)
            val indexList: MutableList<Int> = mutableListOf()

            for (optionIndex in 0 until 4) {
                var openingIndex: Int = random.nextInt(0, openingManager.count())
                while (indexList.contains(openingIndex)) {
                    openingIndex = random.nextInt(0, openingManager.count())
                }
                indexList.add(openingIndex)
                val opening = openingManager[openingIndex]
                task[opening] = optionIndex == rightIndex
            }
            quiz.add(task)
        }
    }

    fun resetQuiz() {
        currentTaskNumber = 0
        rightAnswersCount = 0
        quiz.clear()
        for (taskIndex in 0 until Settings.TasksCount) {

            val task: MutableMap<Opening, Boolean> = mutableMapOf()
            val rightIndex: Int = random.nextInt(0, 4)

            for (optionIndex in 0 until 4) {
                val indexList: MutableList<Int> = mutableListOf()
                var openingIndex: Int = random.nextInt(0, openingManager.count())
                while (indexList.contains(openingIndex)) {
                    openingIndex = random.nextInt(0, openingManager.count())
                }
                indexList.add(openingIndex)
                val opening = openingManager[openingIndex]
                task[opening] = optionIndex == rightIndex
            }
            quiz.add(task)
        }
    }

    fun incrementCurrentTaskNumber() {
        currentTaskNumber += 1
    }

    fun getCurrentTaskRightPgn(): String {
        var result = ""
        quiz[currentTaskNumber].keys.forEach {
            if (quiz[currentTaskNumber][it] == true)
                result = it.en_pgn
        }
        return result
    }

    fun getCurrentTaskOptions(): List<String> {
        return quiz[currentTaskNumber].keys.map { it.toString() }
    }

    fun getCurrentTaskRightIndex(): Int {
        return quiz[currentTaskNumber].values.indexOf(true)
    }
}