package com.beforeyoubet.service

import com.beforeyoubet.apidata.Apidata
import com.beforeyoubet.data.client.ClientMatchResponseData
import com.beforeyoubet.model.MatchStatus
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MatchServiceTest {

    val apidata: Apidata = mockk()
    val underTest = MatchService(apidata)

    @Test
    fun shouldGetTodayMatches() {
        every { apidata.todayMatches(any(), any()) } returns listOf(ClientMatchResponseData.matchResponse)

        val matches = underTest.getTodayMatches(MatchStatus.LIVE)

        assertThat(matches).hasSize(1)

        verify { apidata.todayMatches(any(), any()) }

    }

    @Test
    fun shouldGetMatchDetails() {
        every { apidata.matchDetails(any()) } returns ClientMatchResponseData.matchResponse

        val match = underTest.getMatchDetails(1234)

        assertThat(match.id).isNotNull
        assertThat(match.score).isNotNull
        assertThat(match.goals).isNotNull

        verify { apidata.matchDetails(any()) }

    }

}