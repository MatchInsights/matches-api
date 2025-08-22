package match.insights.controller

import match.insights.TestCorsPropsConfig
import match.insights.model.OddFeeling
import match.insights.response.OddsWinnerFeeling
import match.insights.service.OddsService
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

    @Test
    fun shouldGetWinnerFeeling() {
        every { oddsService.oddsWinnerFeeling(any()) } returns
                OddsWinnerFeeling(
                    OddFeeling.STRONG.value,
                    OddFeeling.WEAK.value,
                    OddFeeling.WEAK.value
                )

        val response = mvc.perform(
            get("/api/odds/feeling/winner/2142")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        assertThat(response.status).isEqualTo(HttpStatus.OK.value())

        verify { oddsService.oddsWinnerFeeling(any()) }
    }
}