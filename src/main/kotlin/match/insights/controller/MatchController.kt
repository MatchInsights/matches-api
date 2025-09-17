package match.insights.controller

import match.insights.model.MatchStatus
import match.insights.response.MatchDetails
import match.insights.response.TodayMatch
import match.insights.service.MatchService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/matches")
class MatchController(private val matchService: MatchService) {

    @GetMapping("/today/{status}")
    fun getMatches(
        @PathVariable status: MatchStatus?,
        @RequestParam(required = false) leagueId: Int?
    ): List<TodayMatch> =
        matchService.getTodayMatches(status ?: MatchStatus.NOT_STARTED, leagueId)

    @GetMapping("/{matchId}/details")
    fun getMatches(@PathVariable matchId: Int): MatchDetails =
        matchService.getMatchDetails(matchId)

}