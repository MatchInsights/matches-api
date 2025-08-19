package com.beforeyoubet.controller

import com.beforeyoubet.TestCorsPropsConfig
import com.beforeyoubet.data.client.ClientTeamDetails
import com.beforeyoubet.model.Performance
import com.beforeyoubet.model.TeamRestStatus
import com.beforeyoubet.model.TeamStats
import com.beforeyoubet.response.HomeAwayTeamLastFive
import com.beforeyoubet.response.LastFiveMatchesEvents
import com.beforeyoubet.response.TeamDetails
import com.beforeyoubet.response.TeamPlayer
import com.beforeyoubet.response.TeamPositionsAndPoints
import com.beforeyoubet.response.TeamsRestStatus
import com.beforeyoubet.response.TeamsScorePerformance
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
        every { teamsService.getLast5MatchesResults(any(), any()) } returns HomeAwayTeamLastFive(
            listOf(), listOf()
        )

        val response = mvc.perform(
            get("/api/teams/lastfive/45/23").contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        assertThat(response.status).isEqualTo(HttpStatus.OK.value())

        verify { teamsService.getLast5MatchesResults(any(), any()) }
    }

    @Test
    fun shouldGetH2H() {
        every { teamsService.getHeadToHead(any(), any()) } returns listOf()

        val response = mvc.perform(
            get("/api/teams/h2h/45/23").contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        assertThat(response.status).isEqualTo(HttpStatus.OK.value())

        verify { teamsService.getHeadToHead(any(), any()) }
    }

    @Test
    fun shouldGetH2HStats() {
        every { teamsService.getH2HStats(any(), any()) } returns TwoTeamStats(
            team0 = TeamStats(34, 12, 8, 14, 1),
            team1 = TeamStats(34, 12, 8, 14, 1),
        )

        val response = mvc.perform(
            get("/api/teams/h2h/stats/45/23").contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        assertThat(response.status).isEqualTo(HttpStatus.OK.value())

        verify { teamsService.getH2HStats(any(), any()) }
    }

    @Test
    fun shouldGetSeasonStats() {
        every { teamsService.getTeamsStats(any(), any(), any()) } returns TwoTeamStats(
            team0 = TeamStats(34, 12, 8, 14, 1),
            team1 = TeamStats(34, 12, 8, 14, 1),
        )

        val response = mvc.perform(
            get("/api/teams/season/stats/45/23/1").contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        assertThat(response.status).isEqualTo(HttpStatus.OK.value())

        verify { teamsService.getTeamsStats(any(), any(), any()) }
    }

    @Test
    fun shouldGetLeagueStats() {
        every { teamsService.getTeamsPositionsAndPoints(any(), any(), any()) } returns TeamPositionsAndPoints(
            1, 3, 88, 73
        )

        val response = mvc.perform(
            get("/api/teams/league/stats/45/23/1").contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        assertThat(response.status).isEqualTo(HttpStatus.OK.value())

        verify { teamsService.getTeamsPositionsAndPoints(any(), any(), any()) }
    }

    @Test
    fun shouldGetLastFiveEventsSums() {
        val info = LastFiveMatchesEvents(1, 2, 3, 4, 5, 0, 0, 0, 0, 0)
        every { teamsService.getLast5MatchesEvents(any()) } returns info


        val response = mvc.perform(
            get("/api/teams/matches/events/sum/1234").contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        assertThat(response.status).isEqualTo(HttpStatus.OK.value())

        verify { teamsService.getLast5MatchesEvents(any()) }
    }

    @Test
    fun shouldGetTeamsRestStatuses() {
        every { teamsService.teamRestStatuses(34, 55, "2025-08-04T16:30:00+00:00") } returns
                TeamsRestStatus(TeamRestStatus.GOOD_REST.status, TeamRestStatus.UNKNOWN_STATE.status)


        val response = mvc.perform(
            get("/api/teams/rest/status/34/55/2025-08-04T16:30:00+00:00")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        assertThat(response.status).isEqualTo(HttpStatus.OK.value())

        verify { teamsService.teamRestStatuses(34, 55, "2025-08-04T16:30:00+00:00") }
    }


    @Test
    fun shouldGetTeamsScorePerformance() {
        every { teamsService.teamsScorePerformance(34, 55, 1) } returns
                TeamsScorePerformance(Performance.GOOD.value, Performance.POOR.value)

        val response = mvc.perform(
            get("/api/teams/score/performance/34/55/1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        assertThat(response.status).isEqualTo(HttpStatus.OK.value())

        verify { teamsService.teamsScorePerformance(34, 55, 1) }
    }

    @Test
    fun shouldGetTeamDetails() {
        every { teamsService.teamDetails(55) } returns TeamDetails.fromClientResponse(
            ClientTeamDetails.coach,
            ClientTeamDetails.details
        )


        val response = mvc.perform(
            get("/api/teams/55/details")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        assertThat(response.status).isEqualTo(HttpStatus.OK.value())

        verify { teamsService.teamDetails(55) }
    }

    @Test
    fun shouldGetTeamSquad() {
        every { teamsService.teamPlayers(55) } returns TeamPlayer.fromResponse(
            ClientTeamDetails.squad
        )

        val response = mvc.perform(
            get("/api/teams/55/players")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        assertThat(response.status).isEqualTo(HttpStatus.OK.value())

        verify { teamsService.teamPlayers(55) }
    }
}