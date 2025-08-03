package com.beforeyoubet.response

import com.beforeyoubet.data.StandingData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LeagueStandingInfoTest {

    @Test
    fun `should correctly parse data into LeagueStandingInfo`() {
        val result = LeagueStandingInfo.fromApiResponse(listOf(StandingData.standing))

        assertThat(result).hasSize(1)
        val info = result[0]

        assertThat(info.rank).isEqualTo(StandingData.standing.rank)
        assertThat(info.teamName).isEqualTo(StandingData.standing.team.name)
        assertThat(info.logo).isEqualTo(StandingData.standing.team.logo)
        assertThat(info.points).isEqualTo(StandingData.standing.points)
        assertThat(info.played).isEqualTo(StandingData.standing.all?.played)
        assertThat(info.won).isEqualTo(StandingData.standing.all?.win)
        assertThat(info.draw).isEqualTo(StandingData.standing.all?.draw)
        assertThat(info.lost).isEqualTo(StandingData.standing.all?.lose)
        assertThat(info.goalsFor).isEqualTo(StandingData.standing.all?.goals?.`for`)
        assertThat(info.goalsAgainst).isEqualTo(StandingData.standing.all?.goals?.against)
        assertThat(info.form).isEqualTo(StandingData.standing.form)
    }
}
