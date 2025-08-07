package com.beforeyoubet.client

import com.beforeyoubet.clientData.*
import com.beforeyoubet.errors.ApiFailedException
import com.beforeyoubet.errors.ErrorMessage
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class ApiSportsClient(private val restClient: RestClient) {

    fun fetchMatches(uri: String): List<MatchResponse> {
        val response = fetch<ApiResponse<List<MatchResponse>>>(uri)
        return response.response
    }

    fun fetchLeagueStandings(uri: String): List<Standing> {
        val response = fetch<ApiResponse<List<StandingResponse>>>(uri)
        return response.response
            .firstOrNull()
            ?.league
            ?.standings
            ?.firstOrNull()
            ?: emptyList()
    }

    fun fetchFixtureOdds(uri: String): List<FixtureOdds> {
        val response = fetch<ApiResponse<List<FixtureOdds>>>(uri)
        return response.response
    }

    fun fetchMatchDetails(uri: String): MatchResponse {
        val response = fetch<ApiResponse<List<MatchResponse>>>(uri)
        return response.response.firstOrNull() ?: throw ApiFailedException(ErrorMessage.CLIENT_FAILED)
    }


    private inline fun <reified T> fetch(uri: String): T {
        return restClient.get()
            .uri(uri)
            .retrieve()
            .body(object : ParameterizedTypeReference<T>() {})
            ?: throw ApiFailedException(ErrorMessage.CLIENT_FAILED)
    }
}
