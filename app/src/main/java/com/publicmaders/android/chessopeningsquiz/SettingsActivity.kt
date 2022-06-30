package com.publicmaders.android.chessopeningsquiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat


class SettingsActivity : AppCompatActivity() {

    private lateinit var quizCountSeekBar: SeekBar
    private lateinit var pieceSpeedSeekBar: SeekBar
    private lateinit var darkThemeSwitchCompat: SwitchCompat
    private lateinit var nextTaskNowSwitchCompat: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        quizCountSeekBar = findViewById(R.id.sb_quizCount)
        pieceSpeedSeekBar= findViewById(R.id.sb_pieceSpeed)
        darkThemeSwitchCompat = findViewById(R.id.sc_darkTheme)
        nextTaskNowSwitchCompat =findViewById(R.id.sc_nextMoveNow)

        quizCountSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener
        {
            override fun onStopTrackingTouch(seekBar: SeekBar)
            {
                // TODO Auto-generated method stub
            }

            override fun onStartTrackingTouch(seekBar: SeekBar)
            {
                // TODO Auto-generated method stub
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean)
            {
                // TODO Auto-generated method stub
                Toast.makeText(applicationContext, progress.toString(), Toast.LENGTH_LONG).show()
            }
        })

        pieceSpeedSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener
        {
            override fun onStopTrackingTouch(seekBar: SeekBar)
            {
                // TODO Auto-generated method stub
            }

            override fun onStartTrackingTouch(seekBar: SeekBar)
            {
                // TODO Auto-generated method stub
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean)
            {
                // TODO Auto-generated method stub
                Toast.makeText(applicationContext, progress.toString(), Toast.LENGTH_LONG).show()
            }
        })

        darkThemeSwitchCompat.setOnClickListener{
            //val intent = Intent(this, TrainActivity::class.java)
            //startActivity(intent)
        }

        nextTaskNowSwitchCompat.setOnClickListener{

        }
    }
}