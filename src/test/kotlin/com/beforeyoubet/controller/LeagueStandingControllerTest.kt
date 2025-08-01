package com.beforeyoubet.controller

import com.beforeyoubet.TestCorsPropsConfig
import com.beforeyoubet.response.LeagueStandingInfo
import com.beforeyoubet.service.LeagueStandingService
import com.ninjasquad.springmockk.MockkBean

import io.mockk.every
import io.mockk.verify

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@WebMvcTest(LeagueStandingController::class)
@Import(TestCorsPropsConfig::class)
class LeagueStandingControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @MockkBean
    private lateinit var leagueStandingService: LeagueStandingService

    val rawData = listOf(
        mapOf(
            "rank" to 1,
            "team" to mapOf("name" to "Arsenal", "logo" to "arsenal.png"),
            "points" to 87,
            "form" to "W,W,D,W,W",
            "all" to mapOf(
                "played" to 38,
                "win" to 27,
                "draw" to 6,
                "lose" to 5,
                "goals" to mapOf(
                    "for" to 88,
                    "against" to 34
                )
            )
        )
    )

    @Test
    fun shouldGetLeagueInfo() {
        every { leagueStandingService.fetchStandings(any()) } returns
                LeagueStandingInfo.fromRawData(rawData)

        val response = mvc.perform(
            get("/api/league/standing/39")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        assertThat(response.status).isEqualTo(HttpStatus.OK.value())

        verify { leagueStandingService.fetchStandings(any()) }
    }
}