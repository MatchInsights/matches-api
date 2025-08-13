package com.beforeyoubet.service

import com.beforeyoubet.apidata.Apidata
import com.beforeyoubet.datamanipulation.DataManipulation
import com.beforeyoubet.data.client.ClientOddsData
import com.beforeyoubet.data.response.OddsResponseData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OddsServiceTest {
    val apidata: Apidata = mockk()
    val dataManipulation: DataManipulation = mockk()
    val underTest = OddsService(apidata, dataManipulation)

    @Test
    fun shouldGetTheBets() {

        every { apidata.fetchAllOdds(any()) } returns ClientOddsData.mockResponse
        every { dataManipulation.extractBets(any()) } returns OddsResponseData.bets

        val result = underTest.fetchAllOdds(12345)

        assertThat(result).isNotEmpty
        assertThat(result[0].betName).isEqualTo("Match Winner")
        assertThat(result[0].values[0].odd).isEqualTo(1.95)
        assertThat(result[1].betName).isEqualTo("Odd/Even - First Half")
        assertThat(result[1].values[0].odd).isEqualTo(2.05)

        verify { apidata.fetchAllOdds(any()) }
        verify { dataManipulation.extractBets(any()) }

    }
}