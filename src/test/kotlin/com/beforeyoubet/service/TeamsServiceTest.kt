package com.beforeyoubet.service

import com.beforeyoubet.component.Apidata
import com.beforeyoubet.component.DataManipulation
import com.beforeyoubet.component.EventsDataManipulation
import com.beforeyoubet.data.client.ClientEventsData
import com.beforeyoubet.data.client.ClientMatchResponseData
import com.beforeyoubet.data.client.ClientStandingData
import com.beforeyoubet.model.TeamStats
import com.beforeyoubet.response.LastFiveMatchesEvents

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
    val underTest = TeamsService(apidata, dataManitupulation, eventsDataManitupulation)

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

        every { dataManitupulation.seasonTeamStats(any(), any()) } returns TeamStats(2.0f, 1.5f, 50.0f, 60.0f, 40.0f)

        val result = underTest.getTeamsStats(34, 44, 1)

        assertThat(result).isInstanceOfAny(TwoTeamStats::class.java)

        verify { apidata.getTeamsLeagueMatches(34, 44, 1) }
        verify { dataManitupulation.seasonTeamStats(any(), any()) }
    }

    @Test
    fun shouldH2HTeamsStats() {
        every { apidata.headToHead(34, 44) } returns listOf(ClientMatchResponseData.matchResponse)
        every { dataManitupulation.seasonTeamStats(any(), any()) } returns TeamStats(2.0f, 1.5f, 50.0f, 60.0f, 40.0f)

        val result = underTest.getH2HStats(34, 44)

        assertThat(result).isInstanceOfAny(TwoTeamStats::class.java)

        verify { apidata.headToHead(34, 44) }
        verify { dataManitupulation.seasonTeamStats(any(), any()) }
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
}


