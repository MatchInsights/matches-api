package match.insights.controller

import match.insights.response.H2HDetails
import match.insights.response.HomeAwayTeamLastFive
import match.insights.response.LastFiveMatchesEvents
import match.insights.response.TeamDetails
import match.insights.response.TeamPlayer
import match.insights.response.TeamPositionsAndPoints
import match.insights.response.TeamsRestStatus
import match.insights.response.TeamsScorePerformance
import match.insights.service.TeamsService
import match.insights.response.TwoTeamStats
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/teams")
class TeamController(private val teamsService: TeamsService) {

    @GetMapping("/lastfive/{homeTeamId}/{awayTeamId}")
    fun getMatches(@PathVariable homeTeamId: Int, @PathVariable awayTeamId: Int): HomeAwayTeamLastFive =
        teamsService.getLast5MatchesResults(homeTeamId, awayTeamId)

    @GetMapping("/h2h/{homeTeamId}/{awayTeamId}")
    fun getH2H(@PathVariable homeTeamId: Int, @PathVariable awayTeamId: Int): List<H2HDetails> =
        teamsService.getHeadToHead(homeTeamId, awayTeamId)

    @GetMapping("/h2h/stats/{homeTeamId}/{awayTeamId}")
    fun getH2HStats(@PathVariable homeTeamId: Int, @PathVariable awayTeamId: Int): TwoTeamStats =
        teamsService.getH2HStats(homeTeamId, awayTeamId)

    @GetMapping("/season/stats/{homeTeamId}/{awayTeamId}/{leagueId}")
    fun getH2HStats(
        @PathVariable homeTeamId: Int,
        @PathVariable awayTeamId: Int,
        @PathVariable leagueId: Int
    ): TwoTeamStats = teamsService.getTeamsStats(homeTeamId, awayTeamId, leagueId)

    @GetMapping("/league/stats/{homeTeamId}/{awayTeamId}/{leagueId}")
    fun getTeamsLeagueStats(
        @PathVariable homeTeamId: Int,
        @PathVariable awayTeamId: Int,
        @PathVariable leagueId: Int
    ): TeamPositionsAndPoints = teamsService.getTeamsPositionsAndPoints(homeTeamId, awayTeamId, leagueId)

    @GetMapping("/matches/events/sum/{teamId}")
    fun getLastFiveMatchesEvents(
        @PathVariable teamId: Int,
    ): LastFiveMatchesEvents = teamsService.getLast5MatchesEvents(teamId)

    @GetMapping("/rest/status/{homeTeamId}/{awayTeamId}/{fixtureDate}")
    fun getTeamsRestStatuses(
        @PathVariable homeTeamId: Int,
        @PathVariable awayTeamId: Int,
        @PathVariable fixtureDate: String,
    ): TeamsRestStatus {
        return teamsService.teamRestStatuses(homeTeamId, awayTeamId, fixtureDate.trim())
    }


    @GetMapping("/score/performance/{homeTeamId}/{awayTeamId}/{leagueId}")
    fun getScorePerformances(
        @PathVariable homeTeamId: Int,
        @PathVariable awayTeamId: Int,
        @PathVariable leagueId: Int
    ): TeamsScorePerformance = teamsService.teamsScorePerformance(homeTeamId, awayTeamId, leagueId)

    @GetMapping("/{teamId}/details")
    fun getTeamDetails(
        @PathVariable teamId: Int,
    ): TeamDetails = teamsService.teamDetails(teamId)

    @GetMapping("/{teamId}/players")
    fun getTeamPlayers(
        @PathVariable teamId: Int,
    ): List<TeamPlayer> = teamsService.teamPlayers(teamId)
}