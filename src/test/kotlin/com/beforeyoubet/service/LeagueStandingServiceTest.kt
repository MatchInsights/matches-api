package com.beforeyoubet.service

import com.beforeyoubet.component.Apidata
import com.beforeyoubet.data.StandingData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class LeagueStandingServiceTest {

    val apidata: Apidata = mockk()
    val underTest = LeagueStandingService(apidata)

    @Test
    fun shouldLeagueStanding() {
        every { apidata.leagueStandings(any()) } returns listOf(StandingData.standing)

        val matches = underTest.fetchStandings(39)

        assertThat(matches).hasSize(1)

        verify { apidata.leagueStandings(any()) }

    }
}