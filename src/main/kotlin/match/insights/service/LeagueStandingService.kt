package match.insights.service

import match.insights.apidata.Apidata
import match.insights.response.LeagueStandingInfo
import org.springframework.stereotype.Service


@Service
class LeagueStandingService(
    private val apidata: Apidata
) {
    fun fetchStandings(leagueId: Int): List<LeagueStandingInfo> =
        LeagueStandingInfo.fromApiResponse(apidata.leagueStandings(leagueId))
}
