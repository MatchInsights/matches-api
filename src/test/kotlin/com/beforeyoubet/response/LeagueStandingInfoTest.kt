package com.beforeyoubet.response

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LeagueStandingInfoTest {

    @Test
    fun `should correctly parse raw data into LeagueStandingInfo list`() {
        val rawData = listOf(
            mapOf(
                "rank" to 1,
                "team" to mapOf(
                    "name" to "Manchester City",
                    "logo" to "https://logo.com/mancity.png"
                ),
                "points" to 89,
                "form" to "W,W,D,W,L",
                "all" to mapOf(
                    "played" to 38,
                    "win" to 28,
                    "draw" to 5,
                    "lose" to 5,
                    "goals" to mapOf(
                        "for" to 94,
                        "against" to 33
                    )
                )
            )
        )

        val result = LeagueStandingInfo.fromRawData(rawData)

        assertThat(result).hasSize(1)
        val info = result[0]

        assertThat(info.rank).isEqualTo(1)
        assertThat(info.teamName).isEqualTo("Manchester City")
        assertThat(info.logo).isEqualTo("https://logo.com/mancity.png")
        assertThat(info.points).isEqualTo(89)
        assertThat(info.played).isEqualTo(38)
        assertThat(info.won).isEqualTo(28)
        assertThat(info.draw).isEqualTo(5)
        assertThat(info.lost).isEqualTo(5)
        assertThat(info.goalsFor).isEqualTo(94)
        assertThat(info.goalsAgainst).isEqualTo(33)
        assertThat(info.form).isEqualTo("W,W,D,W,L")
    }
}
