package com.beforeyoubet.service

import com.beforeyoubet.client.ApiSportsClient
import com.beforeyoubet.data.StandingData
import com.beforeyoubet.props.SeasonProps
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class LeagueStandingServiceTest {

    val apiSportsClient: ApiSportsClient = mockk()
    val underTest = LeagueStandingService(apiSportsClient, SeasonProps(2022))


    @Test
    fun shouldLeagueStanding() {
        every { apiSportsClient.fetchLeagueStandings(any()) } returns listOf(StandingData.standing)

        val matches = underTest.fetchStandings(39)

        assertThat(matches).hasSize(1)

        verify { apiSportsClient.fetchLeagueStandings(any()) }

    }

}