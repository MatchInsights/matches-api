package match.insights.controller

import match.insights.TestCorsPropsConfig
import match.insights.service.LeagueService
import com.ninjasquad.springmockk.MockkBean

import io.mockk.every
import io.mockk.verify
import match.insights.response.LeagueInfo
import match.insights.response.LeaguesGroups

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@WebMvcTest(LeagueController::class)
@Import(TestCorsPropsConfig::class)
class LeagueControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @MockkBean
    private lateinit var leagueStandingService: LeagueService


    @Test
    fun shouldGetLeagueInfo() {
        every { leagueStandingService.leagueInfo(any()) } returns
                LeagueInfo(id = 1, season = 2025, group = emptyList())

        val response = mvc.perform(
            get("/api/league/standing/1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        assertThat(response.status).isEqualTo(HttpStatus.OK.value())

        verify { leagueStandingService.leagueInfo(any()) }
    }

    @Test
    fun shouldGetAllLeagues() {
        every { leagueStandingService.allLeagues() } returns
                LeaguesGroups(listOf(), listOf(), listOf())

        val response = mvc.perform(
            get("/api/league/all")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        assertThat(response.status).isEqualTo(HttpStatus.OK.value())

        verify { leagueStandingService.allLeagues() }
    }

}