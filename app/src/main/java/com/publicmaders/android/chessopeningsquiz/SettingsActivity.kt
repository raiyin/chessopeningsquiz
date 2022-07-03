package com.publicmaders.android.chessopeningsquiz

import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat


class SettingsActivity : AppCompatActivity()
{
    private lateinit var quizCountSeekBar: SeekBar
    private lateinit var pieceSpeedSeekBar: SeekBar
    private lateinit var darkThemeSwitchCompat: SwitchCompat
    private lateinit var nextTaskNowSwitchCompat: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        quizCountSeekBar = findViewById(R.id.sb_quizCount)
        pieceSpeedSeekBar = findViewById(R.id.sb_pieceSpeed)
        darkThemeSwitchCompat = findViewById(R.id.sc_darkTheme)
        nextTaskNowSwitchCompat = findViewById(R.id.sc_nextMoveNow)

        Settings.load(applicationContext)
        quizCountSeekBar.progress = Settings.QuizCount
        pieceSpeedSeekBar.progress = Settings.PieceSpeed
        darkThemeSwitchCompat.isChecked = Settings.SetupDarkTheme
        nextTaskNowSwitchCompat.isChecked = Settings.NextTaskImmediately

        quizCountSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener
        {
            override fun onStopTrackingTouch(seekBar: SeekBar)
            {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar)
            {
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean)
            {
                Toast.makeText(applicationContext, progress.toString(), Toast.LENGTH_LONG).show()
                Settings.QuizCount = progress
                Settings.save(applicationContext)
            }
        })

        pieceSpeedSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener
        {
            override fun onStopTrackingTouch(seekBar: SeekBar)
            {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar)
            {
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean)
            {
                Toast.makeText(applicationContext, progress.toString(), Toast.LENGTH_LONG).show()
                Settings.PieceSpeed = pieceSpeedSeekBar.progress
                Settings.save(applicationContext)
            }
        })

        darkThemeSwitchCompat.setOnClickListener {
            Settings.SetupDarkTheme = darkThemeSwitchCompat.isChecked
            Settings.save(applicationContext)
        }

        nextTaskNowSwitchCompat.setOnClickListener {
            Settings.NextTaskImmediately = nextTaskNowSwitchCompat.isChecked
            Settings.save(applicationContext)
        }
    }
}