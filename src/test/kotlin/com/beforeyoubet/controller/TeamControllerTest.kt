package com.beforeyoubet.controller

import com.beforeyoubet.TestCorsPropsConfig
import com.beforeyoubet.data.MatchResponseData
import com.beforeyoubet.response.HomeAwayTeamLastFive
import com.beforeyoubet.response.MatchDetails
import com.beforeyoubet.service.MatchService
import com.beforeyoubet.service.TeamsService
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

@Import(TestCorsPropsConfig::class)
@WebMvcTest(TeamController::class)
class TeamControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @MockkBean
    private lateinit var teamsService: TeamsService

    @Test
    fun shouldGetLast5Matches() {
        every { teamsService.getLast5Matches(any(), any()) } returns HomeAwayTeamLastFive(
            listOf(), listOf()
        )

        val response = mvc.perform(
            get("/api/teams/lastfive/45/23")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        assertThat(response.status).isEqualTo(HttpStatus.OK.value())

        verify { teamsService.getLast5Matches(any(), any()) }
    }


}