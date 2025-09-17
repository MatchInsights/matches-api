package match.insights.service

import match.insights.apidata.GeneralData
import match.insights.datamanipulation.DataManipulation
import match.insights.datamanipulation.EventsDataManipulation
import match.insights.data.client.ClientEventsData
import match.insights.data.client.ClientMatchResponseData
import match.insights.data.client.ClientLeagueData
import match.insights.data.client.ClientTeamDetails
import match.insights.datamanipulation.PerformanceDataManipulation
import match.insights.model.Performance
import match.insights.model.TeamRestStatus
import match.insights.model.TeamStats
import match.insights.response.LastFiveMatchesEvents
import match.insights.response.TeamDetails
import match.insights.response.TeamsScorePerformance

import match.insights.response.TwoTeamStats
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import match.insights.apidata.LeaguesData
import match.insights.apidata.MatchesData
import match.insights.datamanipulation.LeagueDataManipulation
import match.insights.datamanipulation.TeamSquadManipulation
import match.insights.response.PlayerSummary
import match.insights.response.PositionAndPoints
import match.insights.response.TeamPositionsAndPoints
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.collections.mapOf

class TeamsServiceTest {

    val apidata: GeneralData = mockk()
    val matchesData: MatchesData = mockk()
    val leaguesData: LeaguesData = mockk()
    val dataManitupulation: DataManipulation = mockk()
    val eventsDataManitupulation: EventsDataManipulation = mockk()
    val performanceDataManipulation: PerformanceDataManipulation = mockk()
    val teamSquadManipulation: TeamSquadManipulation = mockk()
    val leagueDataManipulation: LeagueDataManipulation = mockk()

    val underTest = TeamsService(
        apidata,
        matchesData,
        leaguesData,
        dataManitupulation,
        eventsDataManitupulation,
        performanceDataManipulation,
        teamSquadManipulation,
        leagueDataManipulation
    )

    @Test
    fun shouldGetLastFiveMatches() {
        every { matchesData.lastFiveMatchesResults(34, 44) } returns mapOf(
            34 to listOf(ClientMatchResponseData.matchResponse), 44 to listOf(ClientMatchResponseData.matchResponse)
        )

        every { dataManitupulation.lastFiveResults(any(), any()) } returns listOf("W", "L", "D", "W", "L")

        val matches = underTest.getLast5MatchesResults(34, 44)

        assertThat(matches.awayTeamLastFive[0]).isEqualTo("W")
        assertThat(matches.awayTeamLastFive[1]).isEqualTo("L")
        assertThat(matches.awayTeamLastFive[2]).isEqualTo("D")
        assertThat(matches.awayTeamLastFive[3]).isEqualTo("W")
        assertThat(matches.awayTeamLastFive[4]).isEqualTo("L")

        verify { matchesData.lastFiveMatchesResults(34, 44) }

    }

    @Test
    fun shouldGetHead2HeadInfo() {
        every { matchesData.headToHead(34, 44) } returns listOf(ClientMatchResponseData.matchResponse)

        val h2hs = underTest.getHeadToHead(34, 44)

        assertThat(h2hs[0].winner).isNotNull
        assertThat(h2hs[0].date).isNotNull

        verify { matchesData.headToHead(34, 44) }

    }

    @Test
    fun shouldGetTeamsStats() {
        every { matchesData.getTeamsLeagueMatches(34, 44, 1) } returns mapOf(
            34 to listOf(ClientMatchResponseData.matchResponse), 44 to listOf(ClientMatchResponseData.matchResponse)
        )

        every { dataManitupulation.teamStats(any(), any()) } returns TeamStats(2, 1, 50, 60, 40)

        val result = underTest.getTeamsStats(34, 44, 1)

        assertThat(result).isInstanceOfAny(TwoTeamStats::class.java)

        verify { matchesData.getTeamsLeagueMatches(34, 44, 1) }
        verify { dataManitupulation.teamStats(any(), any()) }
    }

    @Test
    fun shouldH2HTeamsStats() {
        every { matchesData.headToHead(34, 44) } returns listOf(ClientMatchResponseData.matchResponse)
        every { dataManitupulation.teamStats(any(), any()) } returns TeamStats(2, 1, 50, 60, 40)

        val result = underTest.getH2HStats(34, 44)

        assertThat(result).isInstanceOfAny(TwoTeamStats::class.java)

        verify { matchesData.headToHead(34, 44) }
        verify { dataManitupulation.teamStats(any(), any()) }
    }

    @Test
    fun shouldGetTeamsPositionsAndPoints() {
        every { leaguesData.leagueStandings(1) } returns ClientLeagueData.leagueStandings
        every {
            leagueDataManipulation.positionAndPoints(
                33,
                44,
                any()
            )
        } returns TeamPositionsAndPoints(
            listOf(PositionAndPoints(1, 15, "")),
            listOf(PositionAndPoints(2, 11, ""))
        )

        val result = underTest.getTeamsPositionsAndPoints(33, 44, 1)

        assertThat(result.awayTeam).isEqualTo(listOf(PositionAndPoints(2, 11, "")))
        assertThat(result.homeTeam).isEqualTo(listOf(PositionAndPoints(1, 15, "")))


        verify { leaguesData.leagueStandings(1) }
        verify { leagueDataManipulation.positionAndPoints(33, 44, any()) }
    }

    @Test
    fun shouldGetTheSumOfTheLastFiveMatchesEvents() {
        val info = LastFiveMatchesEvents(1, 2, 3, 4, 5, 0, 0, 0, 0, 0)
        every {
            matchesData.lastFiveMatchesEvents(1234)
        } returns ClientEventsData.mockEvents
        every {
            eventsDataManitupulation.fiveMachesEventsSum(ClientEventsData.mockEvents)
        } returns info


        val result = underTest.getLast5MatchesEvents(1234)

        assertThat(result).isEqualTo(info)

        verify { matchesData.lastFiveMatchesEvents(1234) }
        verify { eventsDataManitupulation.fiveMachesEventsSum(ClientEventsData.mockEvents) }
    }

    @Test
    fun shouldGetTeamRestStatuses() {
        every { matchesData.mostRecentPlayedMatches(55, 33) } returns mapOf(
            55 to ClientMatchResponseData.matchResponse, 33 to ClientMatchResponseData.matchResponse
        )

        every { dataManitupulation.teamRestStatus(any()) } returns TeamRestStatus.GOOD_REST.status
        every { dataManitupulation.daysBetween(any(), any()) } returns 5

        val result = underTest.teamRestStatuses(55, 33, "2025-09-22T16:30:00+00:00")

        assertThat(result.homeTeamStatus).isEqualTo(TeamRestStatus.GOOD_REST.status)
        assertThat(result.awayTeamStatus).isEqualTo(TeamRestStatus.GOOD_REST.status)

        verify { matchesData.mostRecentPlayedMatches(55, 33) }
        verify { dataManitupulation.teamRestStatus(any()) }
        verify { dataManitupulation.daysBetween(any(), any()) }
    }

    @Test
    fun shouldGetTeamsScorePerformance() {
        every { matchesData.getTeamsLeagueMatches(34, 43, 1) } returns mapOf(
            34 to listOf(ClientMatchResponseData.matchResponse), 43 to listOf(ClientMatchResponseData.matchResponse)
        )

        every {
            performanceDataManipulation.calculateScorePerformance(
                any(), any()
            )
        } returns Performance.GOOD.value

        val result: TeamsScorePerformance = underTest.teamsScorePerformance(34, 43, 1)

        assertThat(result.homeTeamPerformance).isEqualTo(Performance.GOOD.value)
        assertThat(result.awayTeamPerformance).isEqualTo(Performance.GOOD.value)

        verify { matchesData.getTeamsLeagueMatches(34, 43, 1) }
        verify {
            performanceDataManipulation.calculateScorePerformance(
                any(), any()
            )
        }

    }

    @Test
    fun shouldGiveMeTheTeamDetails() {
        every { apidata.getTeamsDetails(22) } returns mapOf(
            "details" to ClientTeamDetails.details, "coach" to ClientTeamDetails.coach
        )

        val result: TeamDetails = underTest.teamDetails(22)

        assertThat(result.coachName).isEqualTo(ClientTeamDetails.coach.name)
        assertThat(result.coachAge).isEqualTo(ClientTeamDetails.coach.age)
        assertThat(result.venueCapacity).isEqualTo(ClientTeamDetails.details.venue.capacity)
        assertThat(result.venueCity).isEqualTo(ClientTeamDetails.details.venue.city)
        assertThat(result.venueName).isEqualTo(ClientTeamDetails.details.venue.name)
        assertThat(result.teamCountry).isEqualTo(ClientTeamDetails.details.team.country)
        assertThat(result.teamName).isEqualTo(ClientTeamDetails.details.team.name)
        assertThat(result.teamLogo).isEqualTo(ClientTeamDetails.details.team.logo)

        verify { apidata.getTeamsDetails(22) }
    }

    @Test
    fun shouldGiveMeTheTeamPlayers() {
        val player = PlayerSummary(
            "player-x",
            22,
            "1.80",
            "78",
            "Goalkeeper",
            0,
            1,
            0,
            3,
            1
        )

        every { apidata.teamSquad(33) } returns mapOf(1 to ClientTeamDetails.mockPlayersResponse)
        every { teamSquadManipulation.teamSquadSummary(mapOf(1 to ClientTeamDetails.mockPlayersResponse)) } returns
                listOf(player)

        val result: List<PlayerSummary> = underTest.teamPlayers(33)

        assertThat(result[0]).isEqualTo(player)

        verify { apidata.teamSquad(33) }
        verify { teamSquadManipulation.teamSquadSummary(mapOf(1 to ClientTeamDetails.mockPlayersResponse)) }
    }

}


