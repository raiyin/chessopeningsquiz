package com.publicmaders.android.chessopeningsquiz
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class Settings(val QuizCount: Int = 10,
                    val PieceSpeed: Int = 20,
                    val SetupDarkTheme: Boolean = true,
                    val NextTaskImmediately: Boolean = false)
{
    //private var settingsFileName: String = "settings"
    //public var QuizCount: Int = 0
    //public var PieceSpeed: Int = 20
    //public var SetupDarkTheme: Boolean = true
    //public var NextTaskImmediately: Boolean = false
}