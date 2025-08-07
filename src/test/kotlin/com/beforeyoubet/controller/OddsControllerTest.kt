package com.beforeyoubet.controller

import com.beforeyoubet.TestCorsPropsConfig
import com.beforeyoubet.service.OddsService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@Import(TestCorsPropsConfig::class)
@WebMvcTest(OddsController::class)
class OddsControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @MockkBean
    private lateinit var oddsService: OddsService

    @Test
    fun shouldGetTheBets() {
        every { oddsService.fetchAllOdds(any()) } returns listOf()

        val response = mvc.perform(
            get("/api/odds/2142")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        assertThat(response.status).isEqualTo(HttpStatus.OK.value())

        verify { oddsService.fetchAllOdds(any()) }
    }

}