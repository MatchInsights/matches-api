package match.insights.datamanipulation

import match.insights.clientData.FixtureOdds
import match.insights.clientData.MatchResponse
import match.insights.errors.ApiFailedException
import match.insights.errors.ErrorMessage
import match.insights.model.Odd
import match.insights.model.OddFeeling
import match.insights.model.TeamRestStatus
import match.insights.model.TeamStats
import match.insights.response.Bet
import match.insights.response.SingleOdd
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
                match.teams.away?.id == teamId && homeGoals > awayGoals -> "L"
                match.teams.away?.id == teamId && homeGoals < awayGoals -> "W"
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


    fun oddsFeeling(data: List<FixtureOdds>): Map<Odd, OddFeeling> {
        val bookmaker = data
            .flatMap { it.bookmakers }
            .firstOrNull()
            ?: return emptyMap()

        val matchWinner = bookmaker.bets.firstOrNull { it.name.contains("Match Winner", true) }
            ?: return emptyMap()

        val homeOdds = matchWinner.values.first { it.value.contains(Odd.HOME.value, true) }.odd.toDouble()
        val drawOdds = matchWinner.values.first { it.value.contains(Odd.DRAW.value, true) }.odd.toDouble()
        val awayOdds = matchWinner.values.first { it.value.contains(Odd.AWAY.value, true) }.odd.toDouble()

        val (homeProb, drawProb, awayProb) = calculateOddsProbabilities(homeOdds, drawOdds, awayOdds)

        return mapOf(
            Odd.HOME to if (homeProb >= 0.5) OddFeeling.STRONG else OddFeeling.WEAK,
            Odd.DRAW to if (drawProb >= 0.5) OddFeeling.STRONG else OddFeeling.WEAK,
            Odd.AWAY to if (awayProb >= 0.5) OddFeeling.STRONG else OddFeeling.WEAK
        )
    }


    fun calculateOddsProbabilities(
        homeOdds: Double,
        drawOdds: Double,
        awayOdds: Double
    ): Triple<Double, Double, Double> {
        if (!(homeOdds > 0) || !(drawOdds > 0) || !(awayOdds > 0))
            throw ApiFailedException(ErrorMessage.CALCULATION_FAILED)

        val impliedHome = 1.0 / homeOdds
        val impliedDraw = 1.0 / drawOdds
        val impliedAway = 1.0 / awayOdds

        val total = impliedHome + impliedDraw + impliedAway

        val normalizedHome = impliedHome / total
        val normalizedDraw = impliedDraw / total
        val normalizedAway = impliedAway / total

        return Triple(normalizedHome, normalizedDraw, normalizedAway)
    }

}