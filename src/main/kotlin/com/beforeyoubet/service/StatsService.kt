package com.beforeyoubet.service

import com.beforeyoubet.clientData.MatchResponse
import com.beforeyoubet.model.TeamStats
import org.springframework.stereotype.Service
import kotlin.math.pow
import kotlin.math.roundToInt

@Service
class StatsService {
    fun seasonTeamStats(teamId: Int, matches: List<MatchResponse>): TeamStats {
        var totalGoalsFor = 0
        var totalGoalsAgainst = 0
        var played = 0
        var cleanSheets = 0
        var scoredIn = 0
        var concededIn = 0

        for (match in matches) {
            val isHome = match.teams.home?.id == teamId
            val isAway = match.teams.away?.id == teamId

            if (!isHome && !isAway) continue
            if (match.fixture.status?.short != "FT") continue

            val goalsFor = if (isHome) match.goals?.home ?: 0 else match.goals?.away ?: 0
            val goalsAgainst = if (isHome) match.goals?.away ?: 0 else match.goals?.home ?: 0

            totalGoalsFor += goalsFor
            totalGoalsAgainst += goalsAgainst
            played++

            if (goalsAgainst == 0) cleanSheets++
            if (goalsFor > 0) scoredIn++
            if (goalsAgainst > 0) concededIn++
        }

        return TeamStats(
            (totalGoalsFor.toFloat() / played).roundTo(2),
            (totalGoalsAgainst.toFloat() / played).roundTo(2),
            ((100f * cleanSheets / played)).roundTo(1),
            ((100f * scoredIn / played)).roundTo(1),
            ((100f * concededIn / played)).roundTo(1)
        )
    }
}

private fun Float.roundTo(decimals: Int): Float {
    val factor = 10.0.pow(decimals).toFloat()
    return (this * factor).roundToInt() / factor
}
