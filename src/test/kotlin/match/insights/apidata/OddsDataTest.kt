package match.insights.apidata

import match.insights.client.ApiSportsClient
import match.insights.data.client.ClientOddsData

import match.insights.props.SeasonProps
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OddsDataTest {

    val apiSportsClient: ApiSportsClient = mockk()
    val props = SeasonProps()
    val underTest = OddsData(apiSportsClient, SeasonProps())

    @Test
    fun `fetch all odds`() {

        every { apiSportsClient.fetchFixtureOdds("/odds?fixture=1234") } returns ClientOddsData.mockResponse

        val result = underTest.fetchAllOdds(1234)

        assertThat(result).isNotEmpty()

        verify { apiSportsClient.fetchFixtureOdds("/odds?fixture=1234") }
    }
}