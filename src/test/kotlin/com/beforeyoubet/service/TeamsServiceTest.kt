package com.beforeyoubet.service

import com.beforeyoubet.client.ApiSportsClient
import com.beforeyoubet.data.MatchResponseData
import com.beforeyoubet.model.TeamStats

import com.beforeyoubet.props.SeasonProps
import com.beforeyoubet.response.TwoTeamStats
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TeamsServiceTest {

    val apiSportsClient: ApiSportsClient = mockk()
    val statsService: StatsService = mockk()
    val underTest = TeamsService(apiSportsClient, statsService, SeasonProps(2023))

    @Test
    fun shouldGetLastFiveMatches() {
        every { apiSportsClient.fetchMatches(any()) } returns listOf(MatchResponseData.matchResponse)

        val matches = underTest.getLast5Matches(34, 44)

        assertThat(matches.homeTeamLastFive).hasSize(1)
        assertThat(matches.homeTeamLastFive).hasSize(1)

        verify { apiSportsClient.fetchMatches(any()) }

    }

    @Test
    fun shouldGetHead2HeadInfo() {
        every { apiSportsClient.fetchMatches(any()) } returns listOf(MatchResponseData.matchResponse)

        val h2hs = underTest.getHeadToHead(34, 44)

        assertThat(h2hs[0].winner).isNotNull
        assertThat(h2hs[0].date).isNotNull

        verify { apiSportsClient.fetchMatches(any()) }

    }

    @Test
    fun shouldGetTeamsStats() {
        every { apiSportsClient.fetchMatches(any()) } returns listOf(MatchResponseData.matchResponse)
        every { statsService.seasonTeamStats(any(), any()) } returns TeamStats(2.0f, 1.5f, 50.0f, 60.0f, 40.0f)

        val result = underTest.getTeamsStats(34, 44, 1)

        assertThat(result).isInstanceOfAny(TwoTeamStats::class.java)

        verify { apiSportsClient.fetchMatches(any()) }
        verify { statsService.seasonTeamStats(any(), any()) }
    }

    @Test
    fun shouldH2HTeamsStats() {
        every { apiSportsClient.fetchMatches(any()) } returns listOf(MatchResponseData.matchResponse)
        every { statsService.seasonTeamStats(any(), any()) } returns TeamStats(2.0f, 1.5f, 50.0f, 60.0f, 40.0f)

        val result = underTest.getH2HStats(34, 44)

        assertThat(result).isInstanceOfAny(TwoTeamStats::class.java)

        verify { apiSportsClient.fetchMatches(any()) }
        verify { statsService.seasonTeamStats(any(), any()) }
    }
}