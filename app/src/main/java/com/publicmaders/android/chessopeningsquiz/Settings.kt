package com.publicmaders.android.chessopeningsquiz
import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileReader
import java.io.FileWriter

@Serializable
object Settings
{
    var TasksCount: Int = 10
    var PieceSpeed: Int = 20
    var SetupDarkTheme: Boolean = true
    var NextTaskImmediately: Boolean = false

    fun load(applicationContext: Context)
    {
        val filename = applicationContext.filesDir.path.toString() + Utils.settingsFileName
        val file = File(filename)
        val notexisted = file.createNewFile()
        if (notexisted)
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
            }
        }
    }

    fun save(applicationContext:Context)
    {
        val filename = applicationContext.filesDir.path.toString() + Utils.settingsFileName
        val json = Json.encodeToString(Settings)
        val fileWriter = FileWriter(filename)
        fileWriter.write(json)
    }
}