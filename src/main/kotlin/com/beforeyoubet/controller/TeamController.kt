package com.beforeyoubet.controller

import com.beforeyoubet.response.H2HDetails
import com.beforeyoubet.response.HomeAwayTeamLastFive
import com.beforeyoubet.service.TeamsService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/teams")
class TeamController(private val teamsService: TeamsService) {

    @GetMapping("/lastfive/{homeTeamId}/{awayTeamId}")
    fun getMatches(@PathVariable homeTeamId: Int, @PathVariable awayTeamId: Int): HomeAwayTeamLastFive =
        teamsService.getLast5Matches(homeTeamId, awayTeamId)

    @GetMapping("/h2h/{homeTeamId}/{awayTeamId}")
    fun getH2H(@PathVariable homeTeamId: Int, @PathVariable awayTeamId: Int): List<H2HDetails> =
        teamsService.getHeadToHead(homeTeamId, awayTeamId)

}