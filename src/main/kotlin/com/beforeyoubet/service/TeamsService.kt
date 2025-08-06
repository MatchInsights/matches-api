package com.beforeyoubet.service

import com.beforeyoubet.client.ApiSportsClient
import com.beforeyoubet.clientData.MatchResponse
import com.beforeyoubet.props.SeasonProps
import com.beforeyoubet.response.H2HDetails
import com.beforeyoubet.response.HomeAwayTeamLastFive
import com.beforeyoubet.response.TwoTeamStats
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@Service
class TeamsService(
    private val apiSportsClient: ApiSportsClient,
    private val statsService: StatsService,
    private val seasonProps: SeasonProps
) {

    fun getLast5Matches(homeTeamId: Int, awayTeamId: Int): HomeAwayTeamLastFive {
        val homeTeamMatches = lastFiveMatches(homeTeamId)
        val awayTeamMatches = lastFiveMatches(awayTeamId)

        return HomeAwayTeamLastFive(
            homeTeamLastFive = lastFiveResults(homeTeamId, homeTeamMatches),
            awayTeamLastFive = lastFiveResults(awayTeamId, awayTeamMatches)
        )
    }

    fun getHeadToHead(homeTeamId: Int, awayTeamId: Int): List<H2HDetails> =
        apiSportsClient.fetchMatches("/fixtures/headtohead?h2h=${homeTeamId}-${awayTeamId}")
            .take(5).map { H2HDetails.fromResponseData(it) }

    fun getTeamsStats(homeTeamId: Int, awayTeamId: Int, leagueId: Int): TwoTeamStats {
        val homeTeamMatches =
            apiSportsClient.fetchMatches("/fixtures/fixtures?team=${homeTeamId}&season=${seasonProps.year}&league=${leagueId}")
        val awayTeamMatches =
            apiSportsClient.fetchMatches("/fixtures/fixtures?team=${awayTeamId}&season=${seasonProps.year}&league=${leagueId}")

        return TwoTeamStats(
            team0 = statsService.seasonTeamStats(homeTeamId, homeTeamMatches),
            team1 = statsService.seasonTeamStats(awayTeamId, awayTeamMatches)
        )


    }

    fun getH2HStats(homeTeamId: Int, awayTeamId: Int): TwoTeamStats {
        val h2hMatches = apiSportsClient.fetchMatches("/fixtures/headtohead?h2h=${homeTeamId}-${awayTeamId}")

        return TwoTeamStats(
            team0 = statsService.seasonTeamStats(homeTeamId, h2hMatches),
            team1 = statsService.seasonTeamStats(awayTeamId, h2hMatches)
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

