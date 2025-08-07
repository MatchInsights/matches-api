package com.beforeyoubet.service

import com.beforeyoubet.client.ApiSportsClient
import com.beforeyoubet.data.OddsData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OddsServiceTest {
    val apiSportsClient: ApiSportsClient = mockk()
    val underTest = OddsService(apiSportsClient)

    @Test
    fun shouldGetTheBets() {

        every { apiSportsClient.fetchFixtureOdds(any()) } returns OddsData.mockResponse
        val result = underTest.fetchAllOdds(12345)

        assertThat(result).isNotEmpty
        assertThat(result[0].betName).isEqualTo("Match Winner")
        assertThat(result[0].values).hasSize(3)
        assertThat(result[1].betName).isEqualTo("Odd/Even - First Half")
        assertThat(result[1].values).hasSize(2)

        verify { apiSportsClient.fetchFixtureOdds(any()) }

    }
}