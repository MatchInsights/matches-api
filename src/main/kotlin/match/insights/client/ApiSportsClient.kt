package match.insights.client

import match.insights.clientData.ApiPagingResponse
import match.insights.clientData.MatchResponse
import match.insights.clientData.ApiResponse
import match.insights.clientData.CoachResponse
import match.insights.clientData.Event
import match.insights.clientData.StandingResponse
import match.insights.clientData.FixtureOdds
import match.insights.clientData.LeagueAndCountry
import match.insights.clientData.LeagueWithStandings
import match.insights.clientData.PlayerResponse
import match.insights.clientData.TeamResponse
import match.insights.errors.ApiFailedException
import match.insights.errors.ErrorMessage
import org.springframework.cache.annotation.Cacheable
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class ApiSportsClient(private val restClient: RestClient) {

    @Cacheable(value = ["fetchMatches"], key = "#uri")
    fun fetchMatches(uri: String): List<MatchResponse> {
        val result = fetch<ApiResponse<List<MatchResponse>>>(uri)
        return result.response
    }

    @Cacheable(value = ["fetchLeagueInfo"], key = "#uri")
    fun fetchLeagueInfo(uri: String): LeagueWithStandings? {
        val result = fetch<ApiResponse<List<StandingResponse>>>(uri)
        return runCatching {
            result.response
                .firstOrNull()
                ?.league
        }.getOrElse {
            throw ApiFailedException(ErrorMessage.CLIENT_FAILED)
        }
    }

    @Cacheable(value = ["fetchAllLeagues"], key = "#uri")
    fun fetchAllLeagues(uri: String): List<LeagueAndCountry> {
        val result = fetch<ApiResponse<List<LeagueAndCountry>>>(uri)
        return result.response
    }

    @Cacheable(value = ["fetchFixtureOdds"], key = "#uri")
    fun fetchFixtureOdds(uri: String): List<FixtureOdds> {
        val result = fetch<ApiResponse<List<FixtureOdds>>>(uri)
        return result.response
    }

    @Cacheable(value = ["fetchMatchDetails"], key = "#uri")
    fun fetchMatchDetails(uri: String): MatchResponse {
        val result = fetch<ApiResponse<List<MatchResponse>>>(uri)
        return runCatching { result.response.first() }.getOrElse {
            throw ApiFailedException(ErrorMessage.CLIENT_FAILED)
        }
    }

    @Cacheable(value = ["fetchMatchEvents"], key = "#uri")
    fun fetchMatchEvents(uri: String): List<Event> {
        val result = fetch<ApiResponse<List<Event>>>(uri)
        return result.response
    }

    @Cacheable(value = ["fetchTeamDetails"], key = "#uri")
    fun fetchTeamDetails(uri: String): TeamResponse {
        val result = fetch<ApiResponse<List<TeamResponse>>>(uri)
        return runCatching { result.response.first() }.getOrElse {
            throw ApiFailedException(ErrorMessage.CLIENT_FAILED)
        }
    }


    @Cacheable(value = ["fetchCoachDetails"], key = "#uri")
    fun fetchCoachDetails(uri: String): List<CoachResponse> {
        val result = fetch<ApiResponse<List<CoachResponse>>>(uri)
        return runCatching {
            result.response
        }.getOrElse {
            throw ApiFailedException(ErrorMessage.CLIENT_FAILED)
        }
    }

    @Cacheable(value = ["fetchPlayers"], key = "#uri")
    fun fetchPlayers(uri: String): ApiPagingResponse<List<PlayerResponse>> {
        return fetch<ApiPagingResponse<List<PlayerResponse>>>(uri)
    }

    private inline fun <reified T> fetch(uri: String): T {
        return restClient.get()
            .uri(uri)
            .retrieve()
            .body(object : ParameterizedTypeReference<T>() {})
            ?: throw ApiFailedException(ErrorMessage.CLIENT_FAILED)
    }
}
