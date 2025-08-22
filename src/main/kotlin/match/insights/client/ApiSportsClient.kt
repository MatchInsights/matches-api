package match.insights.client

import match.insights.clientData.MatchResponse
import match.insights.clientData.ApiResponse
import match.insights.clientData.CoachResponse
import match.insights.clientData.Event
import match.insights.clientData.Standing
import match.insights.clientData.StandingResponse
import match.insights.clientData.FixtureOdds
import match.insights.clientData.SquadResponse
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

    @Cacheable(value = ["fetchLeagueStandings"], key = "#uri")
    fun fetchLeagueStandings(uri: String): List<Standing> {
        val result = fetch<ApiResponse<List<StandingResponse>>>(uri)
        return runCatching {
            result.response
                .first()
                .league
                .standings
                .first()
        }.getOrElse {
            emptyList()
        }
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
    fun fetchCoachDetails(uri: String): CoachResponse {
        val result = fetch<ApiResponse<List<CoachResponse>>>(uri)
        return runCatching { result.response.first() }.getOrElse {
            throw ApiFailedException(ErrorMessage.CLIENT_FAILED)
        }

    }

    @Cacheable(value = ["fetchSquad"], key = "#uri")
    fun fetchSquad(uri: String): SquadResponse {
        val result = fetch<ApiResponse<List<SquadResponse>>>(uri)
        return runCatching { result.response.first() }.getOrElse {
            throw ApiFailedException(ErrorMessage.CLIENT_FAILED)
        }
    }

    private inline fun <reified T> fetch(uri: String): T {
        return restClient.get()
            .uri(uri)
            .retrieve()
            .body(object : ParameterizedTypeReference<T>() {})
            ?: throw ApiFailedException(ErrorMessage.CLIENT_FAILED)
    }
}
