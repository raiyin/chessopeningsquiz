package com.publicmaders.android.chessopeningsquiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity()
{
    private lateinit var trainingButton: Button
    private lateinit var quizButton: Button
    private lateinit var settingsButton: Button

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Settings.load(applicationContext)

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
}
