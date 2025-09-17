package match.insights.service

import match.insights.apidata.LeaguesData
import match.insights.datamanipulation.LeagueDataManipulation
import match.insights.response.LeagueInfo
import match.insights.response.LeaguesGroups
import org.springframework.stereotype.Service


@Service
class LeagueService(
    private val apidata: LeaguesData,
    private val leagueDataManipulation: LeagueDataManipulation
) {
    fun leagueInfo(leagueId: Int): LeagueInfo =
        leagueDataManipulation.extractLeaguesInfo(apidata.leagueStandings(leagueId))


    fun allLeagues(): LeaguesGroups =
        leagueDataManipulation.groupLeagues(apidata.leagues())
}
