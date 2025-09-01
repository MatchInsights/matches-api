package match.insights.apidata

import match.insights.client.ApiSportsClient
import match.insights.clientData.Event
import match.insights.clientData.FixtureOdds
import match.insights.clientData.MatchResponse
import match.insights.clientData.Standing
import match.insights.props.SeasonProps
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import match.insights.clientData.ApiPagingResponse
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

    fun teamSquad(teamId: Int): Map<Int, List<PlayerResponse>> = runBlocking {
        val page1: ApiPagingResponse<List<PlayerResponse>> =
            apiSportsClient.fetchPlayers("/players?team=$teamId&season=${seasonProps.year}&page=1")
        val totalPages = page1.paging.total;
        val nextPage = 2;

        val jobs = (nextPage..totalPages).associateWith { page ->
            async {
                apiSportsClient.fetchPlayers(
                    "/players?team=$teamId&season=${seasonProps.year}&page=$page"
                ).response

            }
        }

        mapOf(1 to page1.response) + jobs.mapValues { it.value.await() }
    }


    fun leagueStandings(leagueId: Int): List<Standing> =
        apiSportsClient.fetchLeagueStandings("/standings?league=$leagueId&season=${seasonProps.year}")

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
            .filter { it.fixture.status?.short == "FT" }
            .sortDescendingByDate()
            .take(5)

    private fun teamOnLeagueMatches(teamId: Int, leagueId: Int): List<MatchResponse> =
        apiSportsClient.fetchMatches("/fixtures/?team=${teamId}&season=${seasonProps.year}&league=${leagueId}")


    fun List<MatchResponse>.sortDescendingByDate(): List<MatchResponse> {
        return this.sortedByDescending {
            val formatter = DateTimeFormatter.ISO_DATE_TIME
            runCatching { ZonedDateTime.parse(it.fixture.date, formatter) }.getOrNull()
        }
    }

}