package com.beforeyoubet.service

import com.beforeyoubet.client.ApiSportsClient
import com.beforeyoubet.props.SeasonProps
import com.beforeyoubet.response.LeagueStandingInfo
import org.springframework.stereotype.Service


@Service
class LeagueStandingService(private val apiSportsClient: ApiSportsClient, private val seasonProps: SeasonProps) {

    fun fetchStandings(leagueId: Int): List<LeagueStandingInfo> {

        val response = apiSportsClient.fetchLeagueStandings("/standings?league=$leagueId&season=${seasonProps.year}")

        return LeagueStandingInfo.fromApiResponse(response)
    }
}
