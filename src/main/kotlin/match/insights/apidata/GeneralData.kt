package match.insights.apidata

import match.insights.client.ApiSportsClient
import match.insights.props.SeasonProps
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import match.insights.clientData.PlayerResponse
import org.springframework.stereotype.Component

@Component
class GeneralData(
    private val apiSportsClient: ApiSportsClient,
    private val seasonProps: SeasonProps
) {

    fun getTeamsDetails(teamId: Int) = runBlocking {
        val jobs = mapOf(
            "details" to async { apiSportsClient.fetchTeamDetails("/teams?id=$teamId") },
            "coach" to async {
                apiSportsClient.fetchCoachDetails("/coachs?team=$teamId")
                    .firstOrNull { coach ->
                        coach.career?.any { it.team?.id == teamId && it.end == null } == true
                    }
            }
        )
        jobs.mapValues { (_, job) -> job.await() }
    }

    fun teamSquad(teamId: Int): Map<Int, List<PlayerResponse>> =
        apiSportsClient.fetchPlayers("/players?team=$teamId&season=${seasonProps.year}&page=1")
            .let { res ->
                if (res.response.isNotEmpty())
                    mapOf(1 to res.response) + teamSquadPages(teamId, 2, res.paging.total, false)
                else
                    apiSportsClient.fetchPlayers("/players?team=$teamId&season=${seasonProps.worldCupYear}&page=1")
                        .let { wcRes ->
                            mapOf(1 to wcRes.response) + teamSquadPages(teamId, 2, wcRes.paging.total, true)

                        }
            }


    private fun teamSquadPages(
        teamId: Int,
        nextPage: Int,
        totalPages: Int,
        userWC: Boolean
    ): Map<Int, List<PlayerResponse>> = runBlocking {

        val jobs = (nextPage..totalPages).associateWith { page ->
            async {
                apiSportsClient.fetchPlayers(
                    "/players?team=$teamId&season=${if (userWC) seasonProps.worldCupYear else seasonProps.year}&page=$page"
                ).response

            }
        }

        jobs.mapValues { it.value.await() }
    }
}