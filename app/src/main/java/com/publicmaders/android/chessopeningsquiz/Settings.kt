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
    public var QuizCount: Int = 10
    public var PieceSpeed: Int = 20
    public var SetupDarkTheme: Boolean = true
    public var NextTaskImmediately: Boolean = false

    public fun load(applicationContext: Context)
    {
        val filename = applicationContext.filesDir.path.toString() + Utils.settingsFileName
        var file = File(filename)
        var notexisted = file.createNewFile()
        if (notexisted)
        {
            val json = Json.encodeToString(Settings)
            var fileWriter = FileWriter(filename)
            fileWriter.write(json)
        }
        else
        {
            var fileReader = FileReader(filename)
            val jsonSettingsString = fileReader.readText()
            if (jsonSettingsString == "")
            {
                val json = Json.encodeToString(Settings)
                var fileWriter = FileWriter(filename)
                fileWriter.write(json)
            }
            else
            {
                var tempSettings = Json.decodeFromString<Settings>(jsonSettingsString)
                QuizCount = tempSettings.QuizCount
                PieceSpeed = tempSettings.PieceSpeed
                SetupDarkTheme = tempSettings.SetupDarkTheme
                NextTaskImmediately = tempSettings.NextTaskImmediately
            }
        }
    }

    public fun save(applicationContext:Context)
    {
        val filename = applicationContext.filesDir.path.toString() + Utils.settingsFileName
        val json = Json.encodeToString(Settings)
        var fileWriter = FileWriter(filename)
        fileWriter.write(json)
    }
}