package com.publicmaders.android.chessopeningsquiz.activities

import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.publicmaders.android.chessopeningsquiz.R
import com.publicmaders.android.chessopeningsquiz.models.Settings


class SettingsActivity : AppCompatActivity()
{
    private lateinit var tasksCountSeekBar: SeekBar
    private lateinit var pieceSpeedSeekBar: SeekBar
    private lateinit var darkThemeSwitchCompat: SwitchCompat
    private lateinit var nextTaskNowSwitchCompat: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tasksCountSeekBar = findViewById(R.id.sb_tasksCount)
        pieceSpeedSeekBar = findViewById(R.id.sb_pieceSpeed)
        darkThemeSwitchCompat = findViewById(R.id.sc_darkTheme)
        nextTaskNowSwitchCompat = findViewById(R.id.sc_nextMoveNow)

        tasksCountSeekBar.min = Settings.MinTaskCount
        tasksCountSeekBar.max = Settings.MaxTaskCount
        pieceSpeedSeekBar.min = Settings.MinSpeed
        pieceSpeedSeekBar.max = Settings.MaxSpeed

        Settings.load(applicationContext)
        tasksCountSeekBar.progress = Settings.TasksCount
        pieceSpeedSeekBar.progress = Settings.PieceSpeed
        darkThemeSwitchCompat.isChecked = Settings.SetupDarkTheme
        nextTaskNowSwitchCompat.isChecked = Settings.NextTaskImmediately

        tasksCountSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener
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
                Settings.TasksCount = progress
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