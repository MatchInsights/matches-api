package com.beforeyoubet.component

import com.beforeyoubet.client.ApiSportsClient
import com.beforeyoubet.data.MatchClientResponseData
import com.beforeyoubet.data.OddsData
import com.beforeyoubet.data.StandingData
import com.beforeyoubet.model.MatchStatus
import com.beforeyoubet.props.SeasonProps
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ApiDataTest {

    val apiSportsClient: ApiSportsClient = mockk()
    val props = SeasonProps()
    val underTest = Apidata(apiSportsClient, SeasonProps())

    @Test
    fun `fetch todayMatches`() {
        val day = "2023-10-01"
        every { apiSportsClient.fetchMatches("/fixtures?date=$day&status=${MatchStatus.NOT_STARTED.code}") } returns MatchClientResponseData.matchResponseList

        val result = underTest.todayMatches(day, MatchStatus.NOT_STARTED.code)

        assertThat(result).isNotEmpty()

        verify { apiSportsClient.fetchMatches("/fixtures?date=$day&status=${MatchStatus.NOT_STARTED.code}") }
    }

    @Test
    fun `fetch match details`() {

        every { apiSportsClient.fetchMatchDetails("/fixtures?id=1234") } returns MatchClientResponseData.matchResponse

        val result = underTest.matchDetails(1234)

        assertThat(result).isNotNull()

        verify { apiSportsClient.fetchMatchDetails("/fixtures?id=1234") }
    }

    @Test
    fun `fetch head to head`() {

        every { apiSportsClient.fetchMatches("/fixtures/headtohead?h2h=${12}-${22}") } returns MatchClientResponseData.matchResponseList

        val result = underTest.headToHead(12, 22)

        assertThat(result).isNotEmpty

        verify { apiSportsClient.fetchMatches("/fixtures/headtohead?h2h=${12}-${22}") }
    }

    @Test
    fun `fetch last five matches results`() {

        every { apiSportsClient.fetchMatches("/fixtures?team=${1}&season=${props.year}") } returns MatchClientResponseData.matchResponseList
        every { apiSportsClient.fetchMatches("/fixtures?team=${2}&season=${props.year}") } returns MatchClientResponseData.matchResponseList

        val result = underTest.lastFiveMatchesResults(1, 2)

        assertThat(result[1]).isNotEmpty
        assertThat(result[2]).isNotEmpty

        verify { apiSportsClient.fetchMatches("/fixtures?team=${1}&season=${props.year}") }
        verify { apiSportsClient.fetchMatches("/fixtures?team=${2}&season=${props.year}") }
    }

    @Test
    fun `fetch teams leagues results`() {

        every { apiSportsClient.fetchMatches("/fixtures/?team=${1}&season=${props.year}&league=${1}") } returns MatchClientResponseData.matchResponseList
        every { apiSportsClient.fetchMatches("/fixtures/?team=${2}&season=${props.year}&league=${1}") } returns MatchClientResponseData.matchResponseList

        val result = underTest.getTeamsLeagueMatches(1, 2, 1)

        assertThat(result[1]).isNotEmpty
        assertThat(result[2]).isNotEmpty

        verify { apiSportsClient.fetchMatches("/fixtures/?team=${1}&season=${props.year}&league=${1}") }
        verify { apiSportsClient.fetchMatches("/fixtures/?team=${2}&season=${props.year}&league=${1}") }
    }

    @Test
    fun `fetch league standings`() {

        every { apiSportsClient.fetchLeagueStandings("/standings?league=1&season=${props.year}") } returns listOf(
            StandingData.standing
        )

        val result = underTest.leagueStandings(1)

        assertThat(result).isNotEmpty()

        verify { apiSportsClient.fetchLeagueStandings("/standings?league=1&season=${props.year}") }
    }

    @Test
    fun `fetch all odds`() {

        every { apiSportsClient.fetchFixtureOdds("/odds?fixture=1234") } returns OddsData.mockResponse

        val result = underTest.fetchAllOdds(1234)

        assertThat(result).isNotEmpty()

        verify { apiSportsClient.fetchFixtureOdds("/odds?fixture=1234") }
    }
}