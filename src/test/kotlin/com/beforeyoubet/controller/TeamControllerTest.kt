package com.beforeyoubet.controller

import com.beforeyoubet.TestCorsPropsConfig
import com.beforeyoubet.model.TeamStats
import com.beforeyoubet.response.HomeAwayTeamLastFive
import com.beforeyoubet.service.TeamsService
import com.beforeyoubet.response.TwoTeamStats
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

    @Test
    fun shouldGetH2H() {
        every { teamsService.getHeadToHead(any(), any()) } returns listOf()

        val response = mvc.perform(
            get("/api/teams/h2h/45/23")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        assertThat(response.status).isEqualTo(HttpStatus.OK.value())

        verify { teamsService.getHeadToHead(any(), any()) }
    }

    @Test
    fun shouldGetH2HStats() {
        every { teamsService.getH2HStats(any(), any()) } returns TwoTeamStats(
            team0 = TeamStats(34.0f, 12.0f, 8.0f, 14.0f, 1.0f),
            team1 = TeamStats(34.0f, 12.0f, 8.0f, 14.0f, 1.0f),
        )

        val response = mvc.perform(
            get("/api/teams/h2h/stats/45/23")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        assertThat(response.status).isEqualTo(HttpStatus.OK.value())

        verify { teamsService.getH2HStats(any(), any()) }
    }

    @Test
    fun shouldGetSeasonStats() {
        every { teamsService.getTeamsStats(any(), any(), any()) } returns TwoTeamStats(
            team0 = TeamStats(34.0f, 12.0f, 8.0f, 14.0f, 1.0f),
            team1 = TeamStats(34.0f, 12.0f, 8.0f, 14.0f, 1.0f),
        )

        val response = mvc.perform(
            get("/api/teams/season/stats/45/23/1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        assertThat(response.status).isEqualTo(HttpStatus.OK.value())

        verify { teamsService.getTeamsStats(any(), any(), any()) }
    }
}