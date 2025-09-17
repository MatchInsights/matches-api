package match.insights.service

import match.insights.datamanipulation.DataManipulation
import match.insights.data.client.ClientOddsData
import match.insights.data.response.OddsResponseData
import match.insights.model.Odd
import match.insights.model.OddFeeling
import match.insights.response.OddsWinnerFeeling
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import match.insights.apidata.OddsData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OddsServiceTest {
    val apidata: OddsData = mockk()
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

    @Test
    fun shouldDetectOddsWinnerFeeling() {
        every { apidata.fetchAllOdds(any()) } returns ClientOddsData.mockResponse
        every { dataManipulation.oddsFeeling(any()) } returns mapOf(
            Odd.HOME to OddFeeling.STRONG,
            Odd.DRAW to OddFeeling.WEAK,
            Odd.AWAY to OddFeeling.WEAK,
        )


        val result: OddsWinnerFeeling = underTest.oddsWinnerFeeling(123456)

        assertThat(result).isEqualTo(
            OddsWinnerFeeling(
                OddFeeling.STRONG.value,
                OddFeeling.WEAK.value,
                OddFeeling.WEAK.value
            )
        )

        verify { apidata.fetchAllOdds(any()) }
        verify { dataManipulation.oddsFeeling(any()) }
    }
}