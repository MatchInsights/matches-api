package match.insights.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import match.insights.apidata.LeaguesData
import match.insights.data.client.ClientLeagueData
import match.insights.datamanipulation.LeagueDataManipulation
import match.insights.response.LeagueInfo
import match.insights.response.LeaguesGroups
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class LeagueServiceTest {

    val apidata: LeaguesData = mockk()

    val leagueDataManipulation: LeagueDataManipulation = mockk()

    val underTest = LeagueService(apidata, leagueDataManipulation)

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


    @Test
    fun shouldFetchAllLeagues() {
        every { apidata.leagues() } returns ClientLeagueData.allLeagues
        every { leagueDataManipulation.groupLeagues(any()) } returns LeaguesGroups(
            internationals = listOf(),
            countryLeagues = listOf(),
            others = listOf()
        )

        assertThat(underTest.allLeagues()).isNotNull


        verify { apidata.leagues() }
        verify { leagueDataManipulation.groupLeagues(any()) }
    }

}