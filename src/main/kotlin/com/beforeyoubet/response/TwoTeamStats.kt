package com.beforeyoubet.response

import com.beforeyoubet.model.TeamStats

data class TwoTeamStats(
    val team0: TeamStats,
    val team1: TeamStats
)