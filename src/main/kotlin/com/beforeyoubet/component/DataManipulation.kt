package com.beforeyoubet.component


import com.beforeyoubet.clientData.FixtureOdds
import com.beforeyoubet.clientData.MatchResponse
import com.beforeyoubet.model.TeamRestStatus

import com.beforeyoubet.model.TeamStats
import com.beforeyoubet.response.Bet
import com.beforeyoubet.response.SingleOdd
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


@Component
class DataManipulation {
    fun teamStats(teamId: Int, matches: List<MatchResponse>): TeamStats {
        var totalGoalsFor = 0
        var totalGoalsAgainst = 0

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


            if (goalsAgainst == 0) cleanSheets++
            if (goalsFor > 0) scoredIn++
            if (goalsAgainst > 0) concededIn++
        }

        return TeamStats(
            totalGoalsFor,
            totalGoalsAgainst,
            cleanSheets,
            scoredIn,
            concededIn
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

    fun daysBetween(date1: String?, date2: String?): Long? {
        if (date1.isNullOrBlank() || date1 == "Unknown Date") return null
        if (date2.isNullOrBlank() || date2 == "Unknown Date") return null

        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        val d1 = runCatching { ZonedDateTime.parse(date1, formatter) }.getOrNull()
        val d2 = runCatching { ZonedDateTime.parse(date2, formatter) }.getOrNull()

        if (d1 != null && d2 != null) {
            return ChronoUnit.DAYS.between(d1, d2)
        }

        return null
    }

    fun teamRestStatus(daysSinceLastMatch: Long): String {
        if (daysSinceLastMatch >= 5) return TeamRestStatus.GOOD_REST.status
        if (daysSinceLastMatch in 3..4) return TeamRestStatus.MODERATE_CONGESTION.status
        if (daysSinceLastMatch in 0..2) return TeamRestStatus.SEVERE_CONGESTION.status
        return TeamRestStatus.UNKNOWN_STATE.status
    }

}