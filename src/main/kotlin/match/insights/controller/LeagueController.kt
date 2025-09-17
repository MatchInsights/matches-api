package match.insights.controller


import match.insights.response.LeagueInfo
import match.insights.response.LeaguesGroups
import match.insights.service.LeagueService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/league")
class LeagueController(private val leagueService: LeagueService) {

    @GetMapping("/standing/{leagueId}")
    fun leagueStandings(@PathVariable leagueId: Int): LeagueInfo =
        leagueService.leagueInfo(leagueId)


    @GetMapping("/all")
    fun leagues(): LeaguesGroups = leagueService.allLeagues()

}