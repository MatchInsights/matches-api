package match.insights.apidata

import match.insights.client.ApiSportsClient
import match.insights.props.SeasonProps
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import match.insights.data.client.ClientLeagueData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LeaguesDataTest {

    val apiSportsClient: ApiSportsClient = mockk()
    val props = SeasonProps()
    val underTest = LeaguesData(apiSportsClient, SeasonProps())

    @Test
    fun `fetch league standings`() {

        every { apiSportsClient.fetchLeagueInfo("/standings?league=1&season=${props.year}") } returns
                ClientLeagueData.leagueStandings

        val result = underTest.leagueStandings(1)

        assertThat(result?.id).isEqualTo(1)
        assertThat(result?.season).isEqualTo(2022)
        assertThat(result?.standings?.first()?.first()?.rank).isEqualTo(1)
        assertThat(result?.standings?.first()?.first()?.team?.id).isEqualTo(33)

        verify { apiSportsClient.fetchLeagueInfo("/standings?league=1&season=${props.year}") }
    }


    @Test
    fun `fetch available leagues`() {
        every { apiSportsClient.fetchAllLeagues("/leagues") } returns ClientLeagueData.allLeagues

        val result = underTest.leagues()

        assertThat(result[1].league.name).isEqualTo("Premier League")
        assertThat(result[1].country.name).isEqualTo("England")
        assertThat(result[2].league.name).isEqualTo("La Liga")
        assertThat(result[2].country.name).isEqualTo("Spain")

        verify { apiSportsClient.fetchAllLeagues("/leagues") }
    }
}