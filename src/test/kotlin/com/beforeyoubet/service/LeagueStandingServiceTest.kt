package com.beforeyoubet.service

import com.beforeyoubet.client.ApiSportsClient
import com.beforeyoubet.props.SeasonProps
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class LeagueStandingServiceTest {

    val apiSportsClient: ApiSportsClient = mockk()
    val underTest = LeagueStandingService(apiSportsClient, SeasonProps(2022))

    private val mockApiResponse = listOf(
        mapOf(
            "rank" to 1,
            "team" to mapOf("name" to "Arsenal", "logo" to "arsenal.png"),
            "points" to 87,
            "form" to "W,W,D,W,W",
            "all" to mapOf(
                "played" to 38,
                "win" to 27,
                "draw" to 6,
                "lose" to 5,
                "goals" to mapOf(
                    "for" to 88,
                    "against" to 34
                )
            )
        )
    )

    @Test
    fun shouldLeagueStanding() {
        every { apiSportsClient.fetchLeagueStandings(any()) } returns mockApiResponse

        val matches = underTest.fetchStandings(39)

        assertThat(matches).hasSize(1)

        verify { apiSportsClient.fetchLeagueStandings(any()) }

    }

}