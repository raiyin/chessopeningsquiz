package com.publicmaders.android.chessopeningsquiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class MainActivity : AppCompatActivity()
{
    private lateinit var trainingButton: Button
    private lateinit var quizButton: Button
    private lateinit var settingsButton: Button
    private var settingsFileName: String = "settings.json"
    private lateinit var appSettings: Settings

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LoadSettings()

        trainingButton = findViewById(R.id.button_training)
        quizButton = findViewById(R.id.button_quiz)
        settingsButton = findViewById(R.id.button_settings)

        trainingButton.setOnClickListener {
            val intent = Intent(this, TrainActivity::class.java)
            startActivity(intent)
        }

        quizButton.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }

        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun LoadSettings()
    {
        val filename = applicationContext.filesDir.path.toString() + settingsFileName
        var file = File(filename)
        var notexisted = file.createNewFile()
        if (notexisted)
        {
            appSettings = Settings(20, 30, false, true)
            val json = Json.encodeToString(appSettings)
            var fileWriter = FileWriter(filename)
            fileWriter.write(json)
        }
        else
        {
            var fileReader = FileReader(filename)
            val jsonSettingsString = fileReader.readText()
            if (jsonSettingsString == "")
            {
                appSettings = Settings(10, 40, true, false)
                val json = Json.encodeToString(appSettings)
                var fileWriter = FileWriter(filename)
                fileWriter.write(json)
            }
            else
            {
                appSettings = Json.decodeFromString(jsonSettingsString)
            }
        }
    }
}
