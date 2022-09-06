package com.publicmaders.android.chessopeningsquiz.models

import kotlinx.serialization.Serializable

@Serializable
data class SettingsData(var taskCount: Int = 5,
                        var pieceSpeed: Int = 20,
                        var appTheme: Int = 0,
                        var coordMode: CoordinatesMode = CoordinatesMode.NO)
