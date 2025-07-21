package com.beforeyoubet.model

data class Match(
    val id: String,
    val utcDate: String,
    val homeTeam: Team,
    val awayTeam: Team,
    val venue: String,
    val competition: String
)
