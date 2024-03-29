package com.publicmaders.android.chessopeningsquiz.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
    private lateinit var bTheme: Button
    private lateinit var bCoords: Button

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportActionBar?.hide()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tasksCountSeekBar = findViewById(R.id.sb_tasksCount)
        pieceSpeedSeekBar = findViewById(R.id.sb_pieceSpeed)
        bTheme = findViewById(R.id.bTheme)
        bCoords = findViewById(R.id.bCoord)

        tasksCountSeekBar.min = Settings.MinTaskCount
        tasksCountSeekBar.max = Settings.MaxTaskCount
        pieceSpeedSeekBar.min = Settings.MinSpeed
        pieceSpeedSeekBar.max = Settings.MaxSpeed

        Settings.load(applicationContext)
        tasksCountSeekBar.progress = Settings.TasksCount
        pieceSpeedSeekBar.progress = Settings.PieceSpeed

        tasksCountSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener
        {
            override fun onStopTrackingTouch(seekBar: SeekBar)
            {
                Toast.makeText(applicationContext, tasksCountSeekBar.progress.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar)
            {
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean)
            {
                Settings.TasksCount = progress
                Settings.save(applicationContext)
            }
        })

        pieceSpeedSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener
        {
            override fun onStopTrackingTouch(seekBar: SeekBar)
            {
                Toast.makeText(applicationContext, pieceSpeedSeekBar.progress.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar)
            {
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean)
            {
                Settings.PieceSpeed = pieceSpeedSeekBar.progress
                Settings.save(applicationContext)
            }
        })

        bTheme.setOnClickListener {
            val intent = Intent(this, ThemeActivity::class.java)
            startActivity(intent)
        }

        bCoords.setOnClickListener {
            val intent = Intent(this, CoordconfigActivity::class.java)
            startActivity(intent)
        }
    }
}