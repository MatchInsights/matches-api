package match.insights.apidata

import match.insights.client.ApiSportsClient
import match.insights.data.client.ClientEventsData
import match.insights.data.client.ClientMatchResponseData
import match.insights.model.MatchStatus
import match.insights.props.SeasonProps
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MatchesDataTest {

    val apiSportsClient: ApiSportsClient = mockk()
    val props = SeasonProps()
    val underTest = MatchesData(apiSportsClient, SeasonProps())

    @Test
    fun `fetch todayMatches`() {
        val day = "2023-10-01"
        every { apiSportsClient.fetchMatches("/fixtures?date=$day&status=${MatchStatus.NOT_STARTED.code}&league=${39}") } returns ClientMatchResponseData.matchResponseList

        val result = underTest.todayMatches(day, MatchStatus.NOT_STARTED.code, 39)

        assertThat(result).isNotEmpty()

        verify { apiSportsClient.fetchMatches("/fixtures?date=$day&status=${MatchStatus.NOT_STARTED.code}&league=${39}") }
    }

    @Test
    fun `fetch match details`() {

        every { apiSportsClient.fetchMatchDetails("/fixtures?id=1234") } returns ClientMatchResponseData.matchResponse

        val result = underTest.matchDetails(1234)

        assertThat(result).isNotNull()

        verify { apiSportsClient.fetchMatchDetails("/fixtures?id=1234") }
    }

    @Test
    fun `fetch head to head`() {

        every { apiSportsClient.fetchMatches("/fixtures/headtohead?h2h=${12}-${22}") } returns ClientMatchResponseData.matchResponseList

        val result = underTest.headToHead(12, 22)

        assertThat(result).isNotEmpty

        verify { apiSportsClient.fetchMatches("/fixtures/headtohead?h2h=${12}-${22}") }
    }

    @Test
    fun `fetch last five matches results`() {

        every { apiSportsClient.fetchMatches("/fixtures?team=${1}&season=${props.year}") } returns ClientMatchResponseData.matchResponseList
        every { apiSportsClient.fetchMatches("/fixtures?team=${2}&season=${props.year}") } returns ClientMatchResponseData.matchResponseList

        val result = underTest.lastFiveMatchesResults(1, 2)

        assertThat(result[1]).isNotEmpty
        assertThat(result[2]).isNotEmpty

        verify { apiSportsClient.fetchMatches("/fixtures?team=${1}&season=${props.year}") }
        verify { apiSportsClient.fetchMatches("/fixtures?team=${2}&season=${props.year}") }
    }

    @Test
    fun `fetch teams leagues results`() {

        every { apiSportsClient.fetchMatches("/fixtures/?team=${1}&season=${props.year}&league=${1}") } returns ClientMatchResponseData.matchResponseList
        every { apiSportsClient.fetchMatches("/fixtures/?team=${2}&season=${props.year}&league=${1}") } returns ClientMatchResponseData.matchResponseList

        val result = underTest.getTeamsLeagueMatches(1, 2, 1)

        assertThat(result[1]).isNotEmpty
        assertThat(result[2]).isNotEmpty

        verify { apiSportsClient.fetchMatches("/fixtures/?team=${1}&season=${props.year}&league=${1}") }
        verify { apiSportsClient.fetchMatches("/fixtures/?team=${2}&season=${props.year}&league=${1}") }
    }


    @Test
    fun `fetch last five matches events`() {

        every { apiSportsClient.fetchMatches("/fixtures?team=${33}&season=${props.year}") } returns ClientMatchResponseData.matchResponseList
        every { apiSportsClient.fetchMatchEvents(any()) } returns ClientEventsData.mockEvents

        val result = underTest.lastFiveMatchesEvents(33)

        assertThat(result.size).isEqualTo(12)

        verify { apiSportsClient.fetchMatches("/fixtures?team=${33}&season=${props.year}") }
        verify { apiSportsClient.fetchMatchEvents(any()) }
    }

    @Test
    fun `fetch most recent played matches`() {

        every { apiSportsClient.fetchMatches("/fixtures?team=${33}&season=${props.year}") } returns ClientMatchResponseData.matchResponseList
        every { apiSportsClient.fetchMatches("/fixtures?team=${44}&season=${props.year}") } returns ClientMatchResponseData.matchResponseList

        val result = underTest.mostRecentPlayedMatches(33, 44)

        assertThat(result[33]?.fixture?.date).isNotNull
        assertThat(result[44]?.fixture?.date).isNotNull

        verify { apiSportsClient.fetchMatches("/fixtures?team=${33}&season=${props.year}") }

    }

}