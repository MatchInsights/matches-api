package com.beforeyoubet.client

import com.beforeyoubet.data.RawData
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.client.RestClient
import kotlin.collections.first

class ApiSportsClientTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var underTest: ApiSportsClient

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val baseUrl = mockWebServer.url("/").toString()

        val restClient = RestClient.builder()
            .baseUrl(baseUrl)
            .build()

        underTest = ApiSportsClient(restClient)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should fetch Today Matches`() {

        val mockJson = RawData.todayMatches

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockJson)
                .addHeader("Content-Type", "application/json")
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
        val mockJson = RawData.leagueStandings

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockJson)
                .addHeader("Content-Type", "application/json")
        )

        val result = underTest.fetchLeagueStandings("/standings?league=39&season=2025")

        assertThat(result[0].rank).isEqualTo(1)
        assertThat(result[0].team.name).isEqualTo("Manchester United")
        assertThat(result[0].points).isEqualTo(86)
        assertThat(result[0].all?.played).isEqualTo(0)
    }

    @Test
    fun `should fetch match details`() {
        val mockJson = RawData.matchDetails

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockJson)
                .addHeader("Content-Type", "application/json")
        )

        val result = underTest.fetchMatchDetails("/fixture?id=2025")

        assertThat(result.teams).isNotNull
        assertThat(result.fixture).isNotNull
        assertThat(result.league).isNotNull
        assertThat(result.venue).isNotNull
    }

}