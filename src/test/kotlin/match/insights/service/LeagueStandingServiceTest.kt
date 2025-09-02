package match.insights.service

import match.insights.apidata.Apidata
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import match.insights.data.client.ClientLeagueData
import match.insights.datamanipulation.LeagueDataManipulation
import match.insights.response.LeagueInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class LeagueStandingServiceTest {

    val apidata: Apidata = mockk()
    val leagueDataManipulation: LeagueDataManipulation = mockk()
    val underTest = LeagueStandingService(apidata, leagueDataManipulation)

    @Test
    fun shouldLeagueStanding() {
        every { apidata.leagueStandings(any()) } returns ClientLeagueData.leagueStandings
        every { leagueDataManipulation.extractLeaguesInfo(any()) } returns LeagueInfo(
            id = 1,
            season = 2025,
            group = emptyList()
        )

        val result = underTest.leagueInfo(39)

        assertThat(result.id).isEqualTo(1)
        assertThat(result.season).isEqualTo(2025)
        assertThat(result.group).isEmpty()

        verify { apidata.leagueStandings(any()) }
        verify { leagueDataManipulation.extractLeaguesInfo(any()) }
    }
}