package match.insights.controller


import match.insights.response.LeagueInfo
import match.insights.service.LeagueStandingService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/league")
class LeagueStandingController(private val leagueStandingService: LeagueStandingService) {

    @GetMapping("/standing/{leagueId}")
    fun getMatches(@PathVariable leagueId: Int): LeagueInfo =
        leagueStandingService.leagueInfo(leagueId)

}