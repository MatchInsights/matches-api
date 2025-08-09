package com.beforeyoubet.service

import com.beforeyoubet.component.Apidata
import com.beforeyoubet.response.LeagueStandingInfo
import org.springframework.stereotype.Service


@Service
class LeagueStandingService(
    private val apidata: Apidata
) {
    fun fetchStandings(leagueId: Int): List<LeagueStandingInfo> =
        LeagueStandingInfo.fromApiResponse(apidata.leagueStandings(leagueId))
}
