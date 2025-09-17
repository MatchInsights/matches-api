package match.insights.service

import match.insights.data.client.ClientMatchResponseData
import match.insights.model.MatchStatus
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import match.insights.apidata.MatchesData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MatchServiceTest {

    val apidata: MatchesData = mockk()
    val underTest = MatchService(apidata)

    @Test
    fun shouldGetTodayMatches() {
        every { apidata.todayMatches(any(), any(), any()) } returns listOf(ClientMatchResponseData.matchResponse)

        val matches = underTest.getTodayMatches(MatchStatus.LIVE, 39)

        assertThat(matches).hasSize(1)

        verify { apidata.todayMatches(any(), any(), any()) }

    }

    @Test
    fun shouldGetMatchDetails() {
        every { apidata.matchDetails(any()) } returns ClientMatchResponseData.matchResponse

        val match = underTest.getMatchDetails(1234)

        assertThat(match.id).isNotNull
        assertThat(match.score).isNotNull
        assertThat(match.goals).isNotNull

        verify { apidata.matchDetails(any()) }

    }
}