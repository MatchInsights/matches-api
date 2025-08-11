package com.beforeyoubet.service

import com.beforeyoubet.clientData.Standing
import com.beforeyoubet.component.Apidata
import com.beforeyoubet.component.DataManipulation
import com.beforeyoubet.component.EventsDataManipulation
import com.beforeyoubet.response.H2HDetails
import com.beforeyoubet.response.HomeAwayTeamLastFive
import com.beforeyoubet.response.TeamPositionsAndPoints
import com.beforeyoubet.response.TwoTeamStats
import org.springframework.stereotype.Service
import kotlin.collections.map


@Service
class TeamsService(
    private val apiData: Apidata,
    private val dataManipulation: DataManipulation,
    private val eventsDataManipulation: EventsDataManipulation
) {

    fun getLast5MatchesResults(homeTeamId: Int, awayTeamId: Int): HomeAwayTeamLastFive {
        val data = apiData.lastFiveMatchesResults(homeTeamId, awayTeamId)

        return HomeAwayTeamLastFive(
            homeTeamLastFive = dataManipulation.lastFiveResults(homeTeamId, data[homeTeamId] ?: emptyList()),
            awayTeamLastFive = dataManipulation.lastFiveResults(awayTeamId, data[awayTeamId] ?: emptyList())
        )
    }

    fun getHeadToHead(homeTeamId: Int, awayTeamId: Int): List<H2HDetails> = apiData
        .headToHead(homeTeamId, awayTeamId).take(5).map { H2HDetails.fromResponseData(it) }

    fun getH2HStats(homeTeamId: Int, awayTeamId: Int): TwoTeamStats {
        val h2hMatches = apiData.headToHead(homeTeamId, awayTeamId)
        return TwoTeamStats(
            team0 = dataManipulation.seasonTeamStats(homeTeamId, h2hMatches),
            team1 = dataManipulation.seasonTeamStats(awayTeamId, h2hMatches)
        )
    }


    fun getTeamsStats(homeTeamId: Int, awayTeamId: Int, leagueId: Int): TwoTeamStats {
        val data = apiData.getTeamsLeagueMatches(homeTeamId, awayTeamId, leagueId)
        return TwoTeamStats(
            team0 = dataManipulation.seasonTeamStats(homeTeamId, data[homeTeamId] ?: emptyList()),
            team1 = dataManipulation.seasonTeamStats(awayTeamId, data[awayTeamId] ?: emptyList())
        )
    }


    fun getTeamsPositionsAndPoints(homeTeamId: Int, awayTeamId: Int, leagueId: Int): TeamPositionsAndPoints {
        val response: List<Standing> = apiData.leagueStandings(leagueId)

        val homeTeamStanding = response.firstOrNull { standing -> standing.team.id == homeTeamId }
        val awayTeamStanding = response.firstOrNull { standing -> standing.team.id == awayTeamId }

        return TeamPositionsAndPoints.fromApiResponse(homeTeamStanding, awayTeamStanding)
    }

    fun getLast5MatchesEvents(teamId: Int) =
        eventsDataManipulation.fiveMachesEventsSum(apiData.lastFiveMatchesEvents(teamId))


}

