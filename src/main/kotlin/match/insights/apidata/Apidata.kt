package match.insights.apidata

import match.insights.client.ApiSportsClient
import match.insights.clientData.Event
import match.insights.clientData.FixtureOdds
import match.insights.clientData.MatchResponse
import match.insights.props.SeasonProps
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import match.insights.clientData.ApiPagingResponse
import match.insights.clientData.LeagueWithStandings
import match.insights.clientData.PlayerResponse
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Component
class Apidata(
    private val apiSportsClient: ApiSportsClient,
    private val seasonProps: SeasonProps
) {

    fun todayMatches(day: String, statusCode: String): List<MatchResponse> =
        apiSportsClient.fetchMatches("/fixtures?date=$day&status=${statusCode}")

    fun matchDetails(matchId: Int): MatchResponse =
        apiSportsClient.fetchMatchDetails("/fixtures?id=${matchId}")

    fun headToHead(homeTeamId: Int, awayTeamId: Int): List<MatchResponse> =
        apiSportsClient.fetchMatches("/fixtures/headtohead?h2h=${homeTeamId}-${awayTeamId}")
            .sortDescendingByDate()


    fun lastFiveMatchesResults(homeTeamId: Int, awayTeamId: Int): Map<Int, List<MatchResponse>> = runBlocking {
        val jobs = mapOf(
            homeTeamId to async { lastFiveMatches(homeTeamId) },
            awayTeamId to async { lastFiveMatches(awayTeamId) }
        )

        jobs.mapValues { (_, job) -> job.await() }

    }

    fun getTeamsLeagueMatches(homeTeamId: Int, awayTeamId: Int, leagueId: Int) = runBlocking {
        val jobs = mapOf(
            homeTeamId to async { teamOnLeagueMatches(homeTeamId, leagueId) },
            awayTeamId to async { teamOnLeagueMatches(awayTeamId, leagueId) }
        )
        jobs.mapValues { (_, job) -> job.await() }
    }

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


    fun leagueStandings(leagueId: Int): LeagueWithStandings? =
        apiSportsClient.fetchLeagueInfo("/standings?league=$leagueId&season=${seasonProps.year}")
            ?: apiSportsClient.fetchLeagueInfo("/standings?league=$leagueId&season=${seasonProps.worldCupYear}")

    fun fetchAllOdds(fixtureId: Int): List<FixtureOdds> =
        apiSportsClient.fetchFixtureOdds("/odds?fixture=$fixtureId")


    fun lastFiveMatchesEvents(teamId: Int): List<Event> = runBlocking {
        val matches = lastFiveMatches(teamId)

        val deferredEvents = matches.map { match ->
            async {
                apiSportsClient.fetchMatchEvents("/fixtures/events?fixture=${match.fixture.id}")
            }
        }

        val eventsList: List<List<Event>> = deferredEvents.awaitAll()
        eventsList.flatten().filter { it.team.id == teamId }
    }

    fun mostRecentPlayedMatches(homeTeamId: Int, awayTeamId: Int) = runBlocking {
        val jobs = mapOf(
            homeTeamId to async { lastFiveMatches(homeTeamId).first() },
            awayTeamId to async { lastFiveMatches(awayTeamId).first() }
        )
        jobs.mapValues { (_, job) -> job.await() }
    }


    private fun lastFiveMatches(teamId: Int): List<MatchResponse> =
        apiSportsClient.fetchMatches("/fixtures?team=${teamId}&season=${seasonProps.year}")
            .let {
                return if (it.isNotEmpty())
                    it.filter { it.fixture.status?.short == "FT" }
                        .sortDescendingByDate()
                        .take(5)
                else
                    apiSportsClient.fetchMatches("/fixtures?team=${teamId}&season=${seasonProps.worldCupYear}")
                        .filter { it.fixture.status?.short == "FT" }
                        .sortDescendingByDate()
                        .take(5)
            }

    private fun teamOnLeagueMatches(teamId: Int, leagueId: Int): List<MatchResponse> =
        apiSportsClient.fetchMatches("/fixtures/?team=${teamId}&season=${seasonProps.year}&league=${leagueId}")
            .let {
                return it.ifEmpty { apiSportsClient.fetchMatches("/fixtures/?team=${teamId}&season=${seasonProps.worldCupYear}&league=${leagueId}") }
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


    private fun List<MatchResponse>.sortDescendingByDate(): List<MatchResponse> {
        return this.sortedByDescending {
            val formatter = DateTimeFormatter.ISO_DATE_TIME
            runCatching { ZonedDateTime.parse(it.fixture.date, formatter) }.getOrNull()
        }
    }

}