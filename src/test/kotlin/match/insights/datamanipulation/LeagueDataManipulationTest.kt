package match.insights.datamanipulation

import match.insights.data.client.ClientLeagueData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LeagueDataManipulationTest {
    val underTest = LeagueDataManipulation()

    @Test
    fun shouldReturnLeagueInfo() {
        val result = underTest.extractLeaguesInfo(ClientLeagueData.leagueStandings)

        assertThat(result.id).isEqualTo(1)
        assertThat(result.season).isEqualTo(2022)
        assertThat(result.group[0].label).isEqualTo("default")
        assertThat(result.group[0].teams[0].teamId).isEqualTo(33)
        assertThat(result.group[0].teams[0].rank).isEqualTo(1)
        assertThat(result.group[0].teams[0].points).isEqualTo(89)
        assertThat(result.group[0].teams[0].form).isEqualTo("WWDWW")
        assertThat(result.group[0].teams[0].played).isEqualTo(38)
        assertThat(result.group[0].teams[0].won).isEqualTo(28)
    }

    @Test
    fun shouldRanksAndPointsForBothTeams() {
        val result = underTest.positionAndPoints(33, 33, ClientLeagueData.leagueStandings)

        assertThat(result.homeTeam[0].points).isEqualTo(89)
        assertThat(result.awayTeam[0].points).isEqualTo(89)
        assertThat(result.homeTeam[0].position).isEqualTo(1)
        assertThat(result.awayTeam[0].position).isEqualTo(1)
        assertThat(result.awayTeam[0].description).isEqualTo("default")
        assertThat(result.homeTeam[0].description).isEqualTo("default")
    }


    @Test
    fun shouldGroupLeagues() {
        val result = underTest.groupLeagues(ClientLeagueData.allLeagues)

        assertThat(result.internationals.size).isEqualTo(1)
        assertThat(result.countryLeagues[0].country).isEqualTo("England")
        assertThat(result.countryLeagues[0].leagues[0].name).isEqualTo("Premier League")
        assertThat(result.countryLeagues[1].country).isEqualTo("Spain")
        assertThat(result.countryLeagues[1].leagues[0].name).isEqualTo("La Liga")

        assertThat(result.others.size).isEqualTo(1)

    }
}