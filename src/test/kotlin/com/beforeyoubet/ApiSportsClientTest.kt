package com.beforeyoubet

import com.beforeyoubet.client.ApiSportsClient
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

        val mockJson = """
            {"response":[{"fixture":{"id":1327475,"referee":"Elijio Arreguin","timezone":"UTC","date":"2025-07-27T00:00:00+00:00","timestamp":1753574400,"periods":{"first":1753574400,"second":1753578000},"venue":{"id":20751,"name":"Protective Stadium","city":"Birmingham, Alabama"},"status":{"long":"Match Finished","short":"FT","elapsed":90,"extra":6}},"league":{"id":1095,"name":"USL League One Cup","country":"USA","logo":"https://media.api-sports.io/football/leagues/1095.png","flag":"https://media.api-sports.io/flags/us.svg","season":2025,"round":"Group Stage - 8","standings":false},"teams":{"home":{"id":3989,"name":"Birmingham Legion","logo":"https://media.api-sports.io/football/teams/3989.png","winner":true},"away":{"id":9025,"name":"Forward Madison","logo":"https://media.api-sports.io/football/teams/9025.png","winner":false}},"goals":{"home":2,"away":1},"score":{"halftime":{"home":1,"away":0},"fulltime":{"home":2,"away":1},"extratime":{"home":null,"away":null},"penalty":{"home":null,"away":null}}}]}
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockJson)
                .addHeader("Content-Type", "application/json")
        )

        val result = underTest.fethTodayMatches("/fixtures?date=2025-07-27")

        assertThat(result).isNotEmpty

    }
}