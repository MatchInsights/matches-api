package com.beforeyoubet.service

import com.beforeyoubet.client.ApiSportsClient
import com.beforeyoubet.clientData.MatchResponse
import com.beforeyoubet.props.SeasonProps
import com.beforeyoubet.response.HomeAwayTeamLastFive
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@Service
class TeamsService(private val apiSportsClient: ApiSportsClient, private val seasonProps: SeasonProps) {

    fun getLast5Matches(homeTeamId: Int, awayTeamId: Int): HomeAwayTeamLastFive {

        val homeTeamMatches = lastFiveMatches(homeTeamId)
        val awayTeamMatches = lastFiveMatches(awayTeamId)

        return HomeAwayTeamLastFive(
            homeTeamLastFive = lastFiveResults(homeTeamId, homeTeamMatches),
            awayTeamLastFive = lastFiveResults(awayTeamId, awayTeamMatches)
        )
    }

    private fun lastFiveMatches(teamId: Int): List<MatchResponse> =
        apiSportsClient.fetchMatches("/fixtures?team=${teamId}&season=${seasonProps.year}")
            .filter { it.fixture.status?.short == "FT" }
            .sortedByDescending {
                val formatter = DateTimeFormatter.ISO_DATE_TIME
                runCatching { ZonedDateTime.parse(it.fixture.date, formatter) }.getOrNull()
            }
            .take(5)


    private fun lastFiveResults(teamId: Int, matches: List<MatchResponse>): List<String> {
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
}

