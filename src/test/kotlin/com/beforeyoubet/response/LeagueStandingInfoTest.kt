package com.beforeyoubet.response

import com.beforeyoubet.data.client.ClientStandingData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LeagueStandingInfoTest {

    @Test
    fun `should correctly parse data into LeagueStandingInfo`() {
        val result = LeagueStandingInfo.fromApiResponse(listOf(ClientStandingData.standing))

        assertThat(result).hasSize(1)
        val info = result[0]

        assertThat(info.rank).isEqualTo(ClientStandingData.standing.rank)
        assertThat(info.teamName).isEqualTo(ClientStandingData.standing.team.name)
        assertThat(info.logo).isEqualTo(ClientStandingData.standing.team.logo)
        assertThat(info.points).isEqualTo(ClientStandingData.standing.points)
        assertThat(info.played).isEqualTo(ClientStandingData.standing.all?.played)
        assertThat(info.won).isEqualTo(ClientStandingData.standing.all?.win)
        assertThat(info.draw).isEqualTo(ClientStandingData.standing.all?.draw)
        assertThat(info.lost).isEqualTo(ClientStandingData.standing.all?.lose)
        assertThat(info.goalsFor).isEqualTo(ClientStandingData.standing.all?.goals?.`for`)
        assertThat(info.goalsAgainst).isEqualTo(ClientStandingData.standing.all?.goals?.against)
        assertThat(info.form).isEqualTo(ClientStandingData.standing.form)
    }
}
