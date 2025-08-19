package com.beforeyoubet.service

import com.beforeyoubet.apidata.Apidata
import com.beforeyoubet.datamanipulation.DataManipulation
import com.beforeyoubet.datamanipulation.EventsDataManipulation
import com.beforeyoubet.data.client.ClientEventsData
import com.beforeyoubet.data.client.ClientMatchResponseData
import com.beforeyoubet.data.client.ClientStandingData
import com.beforeyoubet.data.client.ClientTeamDetails
import com.beforeyoubet.datamanipulation.PerformanceDataManipulation
import com.beforeyoubet.model.Performance
import com.beforeyoubet.model.TeamRestStatus
import com.beforeyoubet.model.TeamStats
import com.beforeyoubet.response.LastFiveMatchesEvents
import com.beforeyoubet.response.TeamDetails
import com.beforeyoubet.response.TeamPlayer
import com.beforeyoubet.response.TeamsScorePerformance

import com.beforeyoubet.response.TwoTeamStats
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.collections.mapOf

class TeamsServiceTest {

    val apidata: Apidata = mockk()

    val dataManitupulation: DataManipulation = mockk()
    val eventsDataManitupulation: EventsDataManipulation = mockk()
    val performanceDataManipulation: PerformanceDataManipulation = mockk()
    val underTest = TeamsService(apidata, dataManitupulation, eventsDataManitupulation, performanceDataManipulation)

    @Test
    fun shouldGetLastFiveMatches() {
        every { apidata.lastFiveMatchesResults(34, 44) } returns mapOf(
            34 to listOf(ClientMatchResponseData.matchResponse), 44 to listOf(ClientMatchResponseData.matchResponse)
        )

        every { dataManitupulation.lastFiveResults(any(), any()) } returns listOf("W", "L", "D", "W", "L")

        val matches = underTest.getLast5MatchesResults(34, 44)

        assertThat(matches.awayTeamLastFive[0]).isEqualTo("W")
        assertThat(matches.awayTeamLastFive[1]).isEqualTo("L")
        assertThat(matches.awayTeamLastFive[2]).isEqualTo("D")
        assertThat(matches.awayTeamLastFive[3]).isEqualTo("W")
        assertThat(matches.awayTeamLastFive[4]).isEqualTo("L")

        verify { apidata.lastFiveMatchesResults(34, 44) }

    }

    @Test
    fun shouldGetHead2HeadInfo() {
        every { apidata.headToHead(34, 44) } returns listOf(ClientMatchResponseData.matchResponse)

        val h2hs = underTest.getHeadToHead(34, 44)

        assertThat(h2hs[0].winner).isNotNull
        assertThat(h2hs[0].date).isNotNull

        verify { apidata.headToHead(34, 44) }

    }

    @Test
    fun shouldGetTeamsStats() {
        every { apidata.getTeamsLeagueMatches(34, 44, 1) } returns mapOf(
            34 to listOf(ClientMatchResponseData.matchResponse), 44 to listOf(ClientMatchResponseData.matchResponse)
        )

        every { dataManitupulation.teamStats(any(), any()) } returns TeamStats(2, 1, 50, 60, 40)

        val result = underTest.getTeamsStats(34, 44, 1)

        assertThat(result).isInstanceOfAny(TwoTeamStats::class.java)

        verify { apidata.getTeamsLeagueMatches(34, 44, 1) }
        verify { dataManitupulation.teamStats(any(), any()) }
    }

    @Test
    fun shouldH2HTeamsStats() {
        every { apidata.headToHead(34, 44) } returns listOf(ClientMatchResponseData.matchResponse)
        every { dataManitupulation.teamStats(any(), any()) } returns TeamStats(2, 1, 50, 60, 40)

        val result = underTest.getH2HStats(34, 44)

        assertThat(result).isInstanceOfAny(TwoTeamStats::class.java)

        verify { apidata.headToHead(34, 44) }
        verify { dataManitupulation.teamStats(any(), any()) }
    }

    @Test
    fun shouldGetTeamsPositionsAndPoints() {
        every { apidata.leagueStandings(1) } returns listOf(ClientStandingData.standing)


        val result = underTest.getTeamsPositionsAndPoints(33, 44, 1)

        assertThat(result.awayTeamPoints).isNull()
        assertThat(result.homeTeamPoints).isEqualTo(89)
        assertThat(result.awayTeamPosition).isNull()
        assertThat(result.homeTeamPosition).isEqualTo(1)

        verify { apidata.leagueStandings(1) }
    }

    @Test
    fun shouldGetTheSumOfTheLastFiveMatchesEvents() {
        val info = LastFiveMatchesEvents(1, 2, 3, 4, 5, 0, 0, 0, 0, 0)
        every {
            apidata.lastFiveMatchesEvents(1234)
        } returns ClientEventsData.mockEvents
        every {
            eventsDataManitupulation.fiveMachesEventsSum(ClientEventsData.mockEvents)
        } returns info


        val result = underTest.getLast5MatchesEvents(1234)

        assertThat(result).isEqualTo(info)

        verify { apidata.lastFiveMatchesEvents(1234) }
        verify { eventsDataManitupulation.fiveMachesEventsSum(ClientEventsData.mockEvents) }
    }

    @Test
    fun shouldGetTeamRestStatuses() {
        every { apidata.mostRecentPlayedMatches(55, 33) } returns mapOf(
            55 to ClientMatchResponseData.matchResponse, 33 to ClientMatchResponseData.matchResponse
        )

        every { dataManitupulation.teamRestStatus(any()) } returns TeamRestStatus.GOOD_REST.status
        every { dataManitupulation.daysBetween(any(), any()) } returns 5

        val result = underTest.teamRestStatuses(55, 33, "2025-09-22T16:30:00+00:00")

        assertThat(result.homeTeamStatus).isEqualTo(TeamRestStatus.GOOD_REST.status)
        assertThat(result.awayTeamStatus).isEqualTo(TeamRestStatus.GOOD_REST.status)

        verify { apidata.mostRecentPlayedMatches(55, 33) }
        verify { dataManitupulation.teamRestStatus(any()) }
        verify { dataManitupulation.daysBetween(any(), any()) }
    }

    @Test
    fun shouldGetTeamsScorePerformance() {
        every { apidata.getTeamsLeagueMatches(34, 43, 1) } returns mapOf(
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

        verify { apidata.getTeamsLeagueMatches(34, 43, 1) }
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
        every { apidata.squad(22) } returns ClientTeamDetails.squad

        val result: List<TeamPlayer> = underTest.teamPlayers(22)

        assertThat(result.size).isEqualTo(3)

        verify { apidata.squad(22) }
    }

}


