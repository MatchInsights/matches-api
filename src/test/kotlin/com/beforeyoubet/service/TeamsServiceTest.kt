package com.beforeyoubet.service

import com.beforeyoubet.client.ApiSportsClient
import com.beforeyoubet.data.MatchResponseData

import com.beforeyoubet.props.SeasonProps
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TeamsServiceTest {

    val apiSportsClient: ApiSportsClient = mockk()
    val underTest = TeamsService(apiSportsClient, SeasonProps(2023))

    @Test
    fun shouldGetLastFiveMatches() {
        every { apiSportsClient.fetchMatches(any()) } returns listOf(MatchResponseData.matchResponse)

        val matches = underTest.getLast5Matches(34, 44)

        assertThat(matches.homeTeamLastFive).hasSize(1)
        assertThat(matches.homeTeamLastFive).hasSize(1)

        verify { apiSportsClient.fetchMatches(any()) }

    }
}