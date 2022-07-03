package com.publicmaders.android.chessopeningsquiz

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileReader
import java.io.FileWriter

object Utils
{
    public var settingsFileName: String = "settings.json"
}