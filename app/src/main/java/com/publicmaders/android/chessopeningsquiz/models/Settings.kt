package com.publicmaders.android.chessopeningsquiz.models

import android.content.Context
import com.publicmaders.android.chessopeningsquiz.utils.Utils
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import kotlin.math.max

@Serializable
object Settings
{
    var TasksCount: Int = 10
    val MinTaskCount: Int = 1
    val MaxTaskCount: Int = 10

    var PieceSpeed: Int = 10
    val MinSpeed: Int = 5
    val MaxSpeed: Int = 20

    var SetupDarkTheme: Boolean = true
    var NextTaskImmediately: Boolean = false

    fun load(applicationContext: Context)
    {
        val filename = applicationContext.filesDir.path.toString() + Utils.settingsFileName
        val file = File(filename)
        val notExisted = file.createNewFile()
        if (notExisted)
        {
            val json = Json.encodeToString(Settings)
            val fileWriter = FileWriter(filename)
            fileWriter.write(json)
        }
        else
        {
            val fileReader = FileReader(filename)
            val jsonSettingsString = fileReader.readText()
            if (jsonSettingsString == "")
            {
                val json = Json.encodeToString(Settings)
                val fileWriter = FileWriter(filename)
                fileWriter.write(json)
            }
            else
            {
                val tempSettings = Json.decodeFromString<Settings>(jsonSettingsString)
                TasksCount = tempSettings.TasksCount
                PieceSpeed = tempSettings.PieceSpeed
                SetupDarkTheme = tempSettings.SetupDarkTheme
                NextTaskImmediately = tempSettings.NextTaskImmediately

                if (PieceSpeed < MinSpeed)
                {
                    PieceSpeed = MinSpeed
                }
                else if (PieceSpeed > MaxSpeed)
                {
                    PieceSpeed = MaxSpeed
                }

                if (TasksCount < MinTaskCount)
                {
                    TasksCount = MinTaskCount
                }
                else if (TasksCount > MaxTaskCount)
                {
                    TasksCount = MaxTaskCount
                }
            }
        }
    }

    fun save(applicationContext: Context)
    {
        val filename = applicationContext.filesDir.path.toString() + Utils.settingsFileName
        val json = Json.encodeToString(Settings)
        val fileWriter = FileWriter(filename)
        fileWriter.write(json)
    }

    var IterationCount: Int = 0
        get() = MaxSpeed + MinSpeed - PieceSpeed
}