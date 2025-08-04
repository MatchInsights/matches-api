package com.beforeyoubet.service

import com.beforeyoubet.client.ApiSportsClient
import com.beforeyoubet.data.MatchResponseData
import com.beforeyoubet.model.MatchStatus
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MatchServiceTest {

    val apiSportsClient: ApiSportsClient = mockk()
    val underTest = MatchService(apiSportsClient)

    @Test
    fun shouldGetTodayMatches() {
        every { apiSportsClient.fetchMatches(any()) } returns listOf(MatchResponseData.matchResponse)

        val matches = underTest.getTodayMatches(MatchStatus.LIVE)

        assertThat(matches).hasSize(1)

        verify { apiSportsClient.fetchMatches(any()) }

    }

    @Test
    fun shouldGetMatchDetails() {
        every { apiSportsClient.fetchMatchDetails(any()) } returns MatchResponseData.matchResponse

        val match = underTest.getMatchDetails(1234)

        assertThat(match.id).isNotNull
        assertThat(match.score).isNotNull
        assertThat(match.goals).isNotNull

        verify { apiSportsClient.fetchMatchDetails(any()) }

    }

}