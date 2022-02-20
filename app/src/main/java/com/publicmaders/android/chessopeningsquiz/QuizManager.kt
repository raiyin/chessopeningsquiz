package com.publicmaders.android.chessopeningsquiz

import kotlin.random.Random

class QuizManager(taskCount: Int, openingManager: OpeningManager) {
    var tasks: MutableList<MutableMap<Opening, Boolean>> = mutableListOf()
    var rightAnswersCount: Int = 0

    init {
        for (taskIndex in 0..taskCount) {

            var task: MutableMap<Opening, Boolean> = mutableMapOf()
            val rightIndex: Int = Random.nextInt(0, 4)

            for (optionIndex in 0 until 4) {
                var indexList: MutableList<Int> = mutableListOf()
                var openingIndex: Int = Random.nextInt(0, openingManager.count())
                while (indexList.contains(openingIndex)) {
                    openingIndex = Random.nextInt(0, openingManager.count())
                }
                indexList.add(openingIndex)
                val opening = openingManager[openingIndex]
                task[opening] = optionIndex == rightIndex
            }
            tasks.add(task)
        }
    }
}