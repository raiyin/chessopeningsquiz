package com.publicmaders.android.chessopeningsquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    private lateinit var trainingButton: Button
    private lateinit var quizButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trainingButton = findViewById(R.id.button_training)
        quizButton = findViewById(R.id.button_quiz)

        trainingButton.setOnClickListener{view: View ->
            val intent = Intent(this, TrainActivity::class.java)
            startActivity(intent)
        }

        quizButton.setOnClickListener{view: View ->
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }
    }
}