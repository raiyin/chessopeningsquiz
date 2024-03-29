package com.publicmaders.android.chessopeningsquiz.models

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.publicmaders.android.chessopeningsquiz.utils.Utils
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileReader
import java.io.FileWriter

object Settings
{
    var TasksCount: Int
        get()
        {
            return setData.taskCount
        }
        set(value)
        {
            setData.taskCount = value
        }
    const val MinTaskCount: Int = 1
    const val MaxTaskCount: Int = 10

    var PieceSpeed: Int
        get()
        {
            return setData.pieceSpeed
        }
        set(value)
        {
            setData.pieceSpeed = value
        }
    const val MinSpeed: Int = 5
    const val MaxSpeed: Int = 20

    var appTheme: Int
        get()
        {
            return setData.appTheme
        }
        set(value)
        {
            setData.appTheme = value
        }

    var CoordMode: CoordinatesMode
        get()
        {
            return setData.coordMode
        }
        set(value)
        {
            setData.coordMode = value
        }

    var Paid: Boolean
        get()
        {
            return setData.paid
        }
        set(value)
        {
            setData.paid = value
        }

    private var setData: SettingsData =
        SettingsData(1, 20, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, CoordinatesMode.NO, false)

    fun load(applicationContext: Context)
    {
        val filename = applicationContext.filesDir.path.toString() + Utils.settingsFileName

        val file = File(filename)
        val notExisted = file.createNewFile()
        if (notExisted)
        {
            val json = Json.encodeToString(setData)
            val fileWriter = FileWriter(filename)
            fileWriter.write(json)
        }
        else
        {
            val fileReader = FileReader(filename)
            var json = fileReader.readText()
            if (json == "")
            {
                json = Json.encodeToString(setData)
                val fileWriter = FileWriter(filename)
                fileWriter.write(json)
            }
            else
            {
                var tempSettings: SettingsData
                try
                {
                    tempSettings = Json.decodeFromString(json)
                }
                catch (e: Exception)
                {
                    tempSettings = SettingsData()
                }

                TasksCount = tempSettings.taskCount
                PieceSpeed = tempSettings.pieceSpeed
                appTheme = tempSettings.appTheme
                CoordMode = tempSettings.coordMode
                Paid = tempSettings.paid

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
        val json = Json.encodeToString(setData)
        val fileWriter = FileWriter(filename)
        fileWriter.write(json)
        fileWriter.flush()
        fileWriter.close()
    }

    val IterationCount: Int
        get() = MaxSpeed + MinSpeed - PieceSpeed
}