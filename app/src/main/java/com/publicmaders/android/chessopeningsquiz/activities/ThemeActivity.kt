package com.publicmaders.android.chessopeningsquiz.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.*
import com.publicmaders.android.chessopeningsquiz.R
import com.publicmaders.android.chessopeningsquiz.models.Settings

class ThemeActivity : AppCompatActivity()
{
    private lateinit var rbDefaultTheme: RadioButton
    private lateinit var rbLightTheme: RadioButton
    private lateinit var rbDarkTheme: RadioButton

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme)

        rbDefaultTheme = findViewById(R.id.rbDefaultTheme)
        rbLightTheme = findViewById(R.id.rbLightTheme)
        rbDarkTheme = findViewById(R.id.rbDarkTheme)

        Settings.load(applicationContext)
        when (Settings.appTheme)
        {
            MODE_NIGHT_FOLLOW_SYSTEM -> rbDefaultTheme.isChecked = true
            MODE_NIGHT_NO -> rbLightTheme.isChecked = true
            else -> rbDarkTheme.isChecked = true
        }

        rbDefaultTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
            {
                Settings.appTheme = MODE_NIGHT_FOLLOW_SYSTEM
                setDefaultNightMode(Settings.appTheme)
                Settings.save(applicationContext)
            }
        }

        rbLightTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
            {
                Settings.appTheme = MODE_NIGHT_NO
                setDefaultNightMode(Settings.appTheme)
                Settings.save(applicationContext)
            }
        }

        rbDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
            {
                Settings.appTheme = MODE_NIGHT_YES
                setDefaultNightMode(Settings.appTheme)
                Settings.save(applicationContext)
            }
        }
    }
}