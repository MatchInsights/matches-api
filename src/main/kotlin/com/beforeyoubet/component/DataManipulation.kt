package com.beforeyoubet.component


import com.beforeyoubet.clientData.FixtureOdds
import com.beforeyoubet.clientData.MatchResponse

import com.beforeyoubet.model.TeamStats
import com.beforeyoubet.response.Bet
import com.beforeyoubet.response.SingleOdd
import org.springframework.stereotype.Component
import kotlin.math.roundToInt

@Component
class DataManipulation {
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
            if (played > 0) ((totalGoalsFor.toFloat() / played) * 100).roundToInt() / 100f else 0f,
            if (played > 0) ((totalGoalsAgainst.toFloat() / played) * 100).roundToInt() / 100f else 0f,
            if (played > 0) ((100f * cleanSheets / played) * 10).roundToInt() / 10f else 0f,
            if (played > 0) ((100f * scoredIn / played) * 10).roundToInt() / 10f else 0f,
            if (played > 0) ((100f * concededIn / played) * 10).roundToInt() / 10f else 0f
        )

    }

    fun lastFiveResults(teamId: Int, matches: List<MatchResponse>): List<String> {
        return matches.map { match ->
            val homeGoals = match.goals?.home ?: 0
            val awayGoals = match.goals?.away ?: 0

            when {
                match.teams.home?.id == teamId && homeGoals > awayGoals -> "W"
                match.teams.home?.id == teamId && homeGoals < awayGoals -> "L"
                else -> "D"
            }
        }
    }

    fun extractBets(data: List<FixtureOdds>): List<Bet> {
        val allBets = mutableListOf<Bet>()

        data
            .flatMap { it.bookmakers }
            .firstOrNull()
            ?.bets
            ?.forEach { bet ->
                val odds = bet.values.mapNotNull { value ->
                    val oddDouble = value.odd.toDoubleOrNull()
                    if (oddDouble != null) SingleOdd(label = value.value, odd = oddDouble) else null
                }

                if (odds.isNotEmpty()) {
                    allBets.add(
                        Bet(
                            betName = bet.name,
                            values = odds
                        )
                    )
                }
            }

        return allBets
    }
}