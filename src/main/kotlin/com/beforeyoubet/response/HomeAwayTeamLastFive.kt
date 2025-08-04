package com.beforeyoubet.response

data class HomeAwayTeamLastFive(
    val homeTeamLastFive: List<String> = emptyList(),
    val awayTeamLastFive: List<String> = emptyList()
)