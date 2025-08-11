package com.beforeyoubet.controller

import com.beforeyoubet.TestCorsPropsConfig
import com.beforeyoubet.data.client.ClientMatchResponseData
import com.beforeyoubet.response.MatchDetails
import com.beforeyoubet.service.MatchService
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
@WebMvcTest(MatchController::class)
class MatchControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @MockkBean
    private lateinit var matchService: MatchService

    @Test
    fun shouldGetMatches() {
        every { matchService.getTodayMatches(any()) } returns listOf()

        val response = mvc.perform(
            get("/api/matches/today/LIVE")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        assertThat(response.status).isEqualTo(HttpStatus.OK.value())

        verify { matchService.getTodayMatches(any()) }
    }

    @Test
    fun shouldGetMatcheDetails() {
        every { matchService.getMatchDetails(any()) } returns
                MatchDetails.fromResponseData(
                    ClientMatchResponseData.matchResponse
                )

        val response = mvc.perform(
            get("/api/matches/1234/details")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        assertThat(response.status).isEqualTo(HttpStatus.OK.value())

        verify { matchService.getMatchDetails(any()) }
    }
}