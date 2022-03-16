package com.publicmaders.android.chessopeningsquiz

import kotlin.random.Random

class QuizManager(taskCount: Int, openingManager: OpeningManager) {
    // Quiz - все, например, 10 вопросов вместе.
    // Task - один из, например, 10 вопросов.
    // MutableMap<Opening, Boolean>, так как содержит верный и неверные варианты.
    var quiz: MutableList<MutableMap<Opening, Boolean>> = mutableListOf()
    var rightAnswersCount: Int = 0
    var currentTaskNumber: Int = 0
        private set
    var totalTaskCount: Int = 0
    private lateinit var openingManager: OpeningManager

    init {
        totalTaskCount = taskCount
        this.openingManager = openingManager
        for (taskIndex in 0 until totalTaskCount) {

            val task: MutableMap<Opening, Boolean> = mutableMapOf()
            val rightIndex: Int = Random.nextInt(0, 4)

            for (optionIndex in 0 until 4) {
                val indexList: MutableList<Int> = mutableListOf()
                var openingIndex: Int = Random.nextInt(0, openingManager.count())
                while (indexList.contains(openingIndex)) {
                    openingIndex = Random.nextInt(0, openingManager.count())
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
        for (taskIndex in 0 until totalTaskCount) {

            val task: MutableMap<Opening, Boolean> = mutableMapOf()
            val rightIndex: Int = Random.nextInt(0, 4)

            for (optionIndex in 0 until 4) {
                val indexList: MutableList<Int> = mutableListOf()
                var openingIndex: Int = Random.nextInt(0, openingManager.count())
                while (indexList.contains(openingIndex)) {
                    openingIndex = Random.nextInt(0, openingManager.count())
                }
                indexList.add(openingIndex)
                val opening = openingManager[openingIndex]
                task[opening] = optionIndex == rightIndex
            }
            quiz.add(task)
        }
    }

    fun inrementCurrentTaskNumber() {
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
        return quiz[currentTaskNumber].keys.map { it.en_name }
    }

    fun getCurrentTaskRightIndex(): Int {
        return quiz[currentTaskNumber].values.indexOf(true)
    }
}