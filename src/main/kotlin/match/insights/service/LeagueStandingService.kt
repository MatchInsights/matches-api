package match.insights.service

import match.insights.apidata.Apidata
import match.insights.datamanipulation.LeagueDataManipulation
import match.insights.response.LeagueInfo
import org.springframework.stereotype.Service


@Service
class LeagueStandingService(
    private val apidata: Apidata,
    private val leagueDataManipulation: LeagueDataManipulation
) {
    fun leagueInfo(leagueId: Int): LeagueInfo =
        leagueDataManipulation.extractLeaguesInfo(apidata.leagueStandings(leagueId))
}
