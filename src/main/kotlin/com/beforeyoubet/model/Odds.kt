package com.beforeyoubet.model

data class Odds(
    val matchId: String,
    val homeWin: Double,
    val draw: Double,
    val awayWin: Double,
    val over25: Double,
    val btts: Double
)
