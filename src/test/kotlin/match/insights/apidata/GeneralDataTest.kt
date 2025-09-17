package match.insights.apidata

import match.insights.client.ApiSportsClient
import match.insights.data.client.ClientTeamDetails
import match.insights.props.SeasonProps
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import match.insights.clientData.ApiPagingResponse
import match.insights.clientData.Paging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GeneralDataTest {

    val apiSportsClient: ApiSportsClient = mockk()
    val underTest = GeneralData(apiSportsClient, SeasonProps())


    @Test
    fun `fetch team details and coach map`() {
        every { apiSportsClient.fetchTeamDetails("/teams?id=${33}") } returns ClientTeamDetails.details
        every { apiSportsClient.fetchCoachDetails("/coachs?team=${33}") } returns listOf(ClientTeamDetails.coach)

        val result = underTest.getTeamsDetails(33)

        assertThat(result["details"]).isEqualTo(ClientTeamDetails.details)
        assertThat(result["coach"]).isEqualTo(ClientTeamDetails.coach)

        verify { apiSportsClient.fetchTeamDetails("/teams?id=${33}") }
        verify { apiSportsClient.fetchCoachDetails("/coachs?team=${33}") }
    }

    @Test
    fun `fetch team squad`() {
        every { apiSportsClient.fetchPlayers(any()) } returns ApiPagingResponse(
            ClientTeamDetails.mockPlayersResponse, Paging(1, 1)
        )

        val result = underTest.teamSquad(33)

        assertThat(result).isEqualTo(mapOf(1 to ClientTeamDetails.mockPlayersResponse))

        verify { apiSportsClient.fetchPlayers(any()) }
    }


}