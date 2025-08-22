package match.insights.response

import match.insights.model.TeamStats

data class TwoTeamStats(
    val team0: TeamStats,
    val team1: TeamStats
)