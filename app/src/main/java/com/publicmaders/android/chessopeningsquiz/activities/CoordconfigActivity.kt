package com.publicmaders.android.chessopeningsquiz.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatDelegate
import com.publicmaders.android.chessopeningsquiz.R
import com.publicmaders.android.chessopeningsquiz.models.CoordinatesMode
import com.publicmaders.android.chessopeningsquiz.models.Settings

class CoordconfigActivity : AppCompatActivity()
{
    private lateinit var rbNoCoord: RadioButton
    private lateinit var rbCoordInside: RadioButton

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordconfig)

        rbNoCoord = findViewById(R.id.rbDontShowCoord)
        rbCoordInside = findViewById(R.id.rbShowCoordInside)

        Settings.load(applicationContext)
        when (Settings.CoordMode)
        {
            CoordinatesMode.NO -> rbNoCoord.isChecked = true
            else -> rbCoordInside.isChecked = true
        }

        rbNoCoord.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
            {
                Settings.CoordMode = CoordinatesMode.NO
                Settings.save(applicationContext)
            }
        }

        rbCoordInside.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
            {
                Settings.CoordMode = CoordinatesMode.INSIDE
                Settings.save(applicationContext)
            }
        }
    }
}