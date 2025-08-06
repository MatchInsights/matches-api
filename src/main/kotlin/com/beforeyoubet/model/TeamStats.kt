package com.beforeyoubet.model

data class TeamStats(
    val avgGoalsFor: Float,
    val avgGoalsAgainst: Float,
    val cleanSheetPercent: Float,
    val scoredInPercent: Float,
    val concededInPercent: Float
)