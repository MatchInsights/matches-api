package match.insights.client

import match.insights.clientData.CoachResponse
import match.insights.clientData.SquadResponse
import match.insights.clientData.TeamResponse
import match.insights.data.client.raw.ClientRawData
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.client.RestClient

class ApiSportsClientTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var underTest: ApiSportsClient

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val baseUrl = mockWebServer.url("/").toString()

        val restClient = RestClient.builder().baseUrl(baseUrl).build()

        underTest = ApiSportsClient(restClient)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should fetch Today Matches`() {

        val mockJson = ClientRawData.todayMatches

        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setBody(mockJson).addHeader("Content-Type", "application/json")
        )

        val result = underTest.fetchMatches("/fixtures?date=2025-07-27")

        assertThat(result.first().teams).isNotNull
        assertThat(result.first().fixture).isNotNull
        assertThat(result.first().league).isNotNull
        assertThat(result.first().venue).isNotNull
        assertThat(result.first().goals).isNotNull
        assertThat(result.first().score).isNotNull

    }

    @Test
    fun `should fetch league standings`() {
        val mockJson = ClientRawData.leagueStandings

        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setBody(mockJson).addHeader("Content-Type", "application/json")
        )

        val result = underTest.fetchLeagueStandings("/standings?league=39&season=2025")

        assertThat(result[0].rank).isEqualTo(1)
        assertThat(result[0].team.name).isEqualTo("Manchester United")
        assertThat(result[0].points).isEqualTo(86)
        assertThat(result[0].all?.played).isEqualTo(0)
    }

    @Test
    fun `should fetch match details`() {
        val mockJson = ClientRawData.matchDetails

        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setBody(mockJson).addHeader("Content-Type", "application/json")
        )

        val result = underTest.fetchMatchDetails("/fixture?id=2025")

        assertThat(result.teams).isNotNull
        assertThat(result.fixture).isNotNull
        assertThat(result.league).isNotNull
        assertThat(result.venue).isNotNull
    }


    @Test
    fun `should fetch the odds`() {
        val mockJson = ClientRawData.oddsResponse

        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setBody(mockJson).addHeader("Content-Type", "application/json")
        )

        val result = underTest.fetchFixtureOdds("/odds?fixture=1326874")

        assertThat(result[0].bookmakers[0].bets[0].name).isEqualTo("Match Winner")
        assertThat(result[0].bookmakers[0].bets[0].values).isNotEmpty
        assertThat(result[0].bookmakers[0].bets[1].name).isEqualTo("Odd/Even - First Half")
        assertThat(result[0].bookmakers[0].bets[1].values).isNotEmpty
    }

    @Test
    fun `should fetch match Events`() {
        val mockJson = ClientRawData.matchEvents

        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setBody(mockJson).addHeader("Content-Type", "application/json")
        )

        val result = underTest.fetchMatchEvents("/fixtures/events?fixture=${12124}")

        assertThat(result).isNotEmpty

    }

    @Test
    fun `should fetch Team Details`() {
        val mockJson = ClientRawData.teamDetailsRaw

        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setBody(mockJson).addHeader("Content-Type", "application/json")
        )

        val result: TeamResponse = underTest.fetchTeamDetails("/teams?id=${2431}")

        assertThat(result.team).isNotNull
        assertThat(result.venue).isNotNull

    }

    @Test
    fun `should fetch Coach Details`() {
        val mockJson = ClientRawData.coachResponse

        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setBody(mockJson).addHeader("Content-Type", "application/json")
        )

        val result: List<CoachResponse> = underTest.fetchCoachDetails("/coachs?team=33")

        assertThat(result[0].name).isEqualTo("Erik ten Hag")

    }

    @Test
    fun `should fetch handle it when the response has not data`() {
        val mockJson = ClientRawData.coachResponse

        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setBody(mockJson).addHeader("Content-Type", "application/json")
        )

        val result: List<CoachResponse> = underTest.fetchCoachDetails("/coachs?team=33")

        assertThat(result[0].name).isEqualTo("Erik ten Hag")

    }


    @Test
    fun `should fetch Squad`() {
        val mockJson = ClientRawData.squadsResponse


        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setBody(mockJson).addHeader("Content-Type", "application/json")
        )

        val result: SquadResponse = underTest.fetchSquad("/players/squads?team=33")

        assertThat(result.players.size).isEqualTo(3)


    }
}