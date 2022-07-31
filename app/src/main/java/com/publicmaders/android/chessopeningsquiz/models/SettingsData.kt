package com.publicmaders.android.chessopeningsquiz.models

import kotlinx.serialization.Serializable

@Serializable
data class SettingsData(var taskCount: Int,
                        var pieceSpeed: Int,
                        var appTheme: Int,
                        var immediately: Boolean)
