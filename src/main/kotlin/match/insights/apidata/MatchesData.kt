package match.insights.apidata


import match.insights.client.ApiSportsClient
import match.insights.clientData.Event
import match.insights.clientData.MatchResponse
import match.insights.props.SeasonProps
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Component
class MatchesData(
    private val apiSportsClient: ApiSportsClient,
    private val seasonProps: SeasonProps
) {

    fun todayMatches(day: String, statusCode: String, leagueId: Int? = null): List<MatchResponse> =
        if (leagueId != null) apiSportsClient.fetchMatches("/fixtures?date=$day&status=$statusCode&league=$leagueId&season=${seasonProps.year}")
            .ifEmpty {
                apiSportsClient.fetchMatches("/fixtures?date=$day&status=$statusCode&league=$leagueId&season=${seasonProps.worldCupYear}")
            }
        else apiSportsClient.fetchMatches("/fixtures?date=$day&status=$statusCode")


    fun matchDetails(matchId: Int): MatchResponse =
        apiSportsClient.fetchMatchDetails("/fixtures?id=${matchId}")


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


    fun headToHead(homeTeamId: Int, awayTeamId: Int): List<MatchResponse> =
        apiSportsClient.fetchMatches("/fixtures/headtohead?h2h=${homeTeamId}-${awayTeamId}")
            .sortDescendingByDate()


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


    private fun List<MatchResponse>.sortDescendingByDate(): List<MatchResponse> {
        return this.sortedByDescending {
            val formatter = DateTimeFormatter.ISO_DATE_TIME
            runCatching { ZonedDateTime.parse(it.fixture.date, formatter) }.getOrNull()
        }
    }

}