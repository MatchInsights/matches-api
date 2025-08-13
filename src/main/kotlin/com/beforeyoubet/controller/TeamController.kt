package com.beforeyoubet.controller

import com.beforeyoubet.response.H2HDetails
import com.beforeyoubet.response.HomeAwayTeamLastFive
import com.beforeyoubet.response.LastFiveMatchesEvents
import com.beforeyoubet.response.TeamPositionsAndPoints
import com.beforeyoubet.response.TeamsRestStatus
import com.beforeyoubet.service.TeamsService
import com.beforeyoubet.response.TwoTeamStats
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
        print(fixtureDate)
        return teamsService.teamRestStatuses(homeTeamId, awayTeamId, fixtureDate.trim())
    }

}