package match.insights.service

import match.insights.clientData.Standing
import match.insights.apidata.Apidata
import match.insights.clientData.CoachResponse
import match.insights.clientData.TeamResponse
import match.insights.datamanipulation.DataManipulation
import match.insights.datamanipulation.EventsDataManipulation
import match.insights.datamanipulation.PerformanceDataManipulation
import match.insights.response.H2HDetails
import match.insights.response.HomeAwayTeamLastFive
import match.insights.response.TeamDetails
import match.insights.response.TeamPlayer
import match.insights.response.TeamPositionsAndPoints
import match.insights.response.TeamsRestStatus
import match.insights.response.TeamsScorePerformance
import match.insights.response.TwoTeamStats
import org.springframework.stereotype.Service
import kotlin.collections.map


@Service
class TeamsService(
    private val apiData: Apidata,
    private val dataManipulation: DataManipulation,
    private val eventsDataManipulation: EventsDataManipulation,
    private val performanceDataManipulation: PerformanceDataManipulation
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
            team0 = dataManipulation.teamStats(homeTeamId, h2hMatches),
            team1 = dataManipulation.teamStats(awayTeamId, h2hMatches)
        )
    }


    fun getTeamsStats(homeTeamId: Int, awayTeamId: Int, leagueId: Int): TwoTeamStats {
        val data = apiData.getTeamsLeagueMatches(homeTeamId, awayTeamId, leagueId)
        return TwoTeamStats(
            team0 = dataManipulation.teamStats(homeTeamId, data[homeTeamId] ?: emptyList()),
            team1 = dataManipulation.teamStats(awayTeamId, data[awayTeamId] ?: emptyList())
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

    fun teamRestStatuses(homeTeamId: Int, awayTeamId: Int, fixtureDate: String): TeamsRestStatus {
        val matches = apiData.mostRecentPlayedMatches(homeTeamId, awayTeamId)

        return TeamsRestStatus(
            dataManipulation.teamRestStatus(
                dataManipulation.daysBetween(
                    matches[homeTeamId]?.fixture?.date,
                    fixtureDate
                ) ?: -1
            ),
            dataManipulation.teamRestStatus(
                dataManipulation.daysBetween(
                    matches[awayTeamId]?.fixture?.date,
                    fixtureDate
                ) ?: -1
            )
        )
    }

    fun teamsScorePerformance(homeTeamId: Int, awayTeamId: Int, leagueId: Int): TeamsScorePerformance {
        val matches = apiData.getTeamsLeagueMatches(homeTeamId, awayTeamId, leagueId)

        return TeamsScorePerformance(
            performanceDataManipulation.calculateScorePerformance(homeTeamId, matches[homeTeamId] ?: emptyList()),
            performanceDataManipulation.calculateScorePerformance(homeTeamId, matches[awayTeamId] ?: emptyList())
        )
    }

    fun teamDetails(teamId: Int): TeamDetails {
        return apiData.getTeamsDetails(teamId).let {
            TeamDetails.fromClientResponse(
                it["coach"] as CoachResponse?,
                it["details"] as TeamResponse
            )
        }
    }

    fun teamPlayers(teamId: Int): List<TeamPlayer> = apiData.squad(teamId).let { TeamPlayer.fromResponse(it) }

}

