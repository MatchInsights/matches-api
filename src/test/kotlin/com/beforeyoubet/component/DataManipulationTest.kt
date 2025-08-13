package com.beforeyoubet.component

import com.beforeyoubet.data.client.ClientMatchResponseData
import com.beforeyoubet.data.client.ClientOddsData
import com.beforeyoubet.data.response.OddsResponseData
import com.beforeyoubet.model.TeamRestStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DataManipulationTest {
    val underTest = DataManipulation()

    @Test
    fun shouldCalculateSeasonStats() {

        val result = underTest.teamStats(
            teamId = 33,
            ClientMatchResponseData.matchResponseList
        )

        assertThat(result.goalsFor).isEqualTo(10)
        assertThat(result.goalsAgainst).isEqualTo(6)
        assertThat(result.scoredIn).isEqualTo(3)
        assertThat(result.concededIn).isEqualTo(3)
        assertThat(result.cleanSheet).isEqualTo(0)
    }

    @Test
    fun willReturnLastFiveResults() {
        val result = underTest.lastFiveResults(
            teamId = 33,
            ClientMatchResponseData.matchResponseList
        )

        assertThat(result).containsExactly("W", "W", "D")
    }

    @Test
    fun willExtractFixtureOdds() {
        val odds = ClientOddsData.mockResponse
        val result = underTest.extractBets(odds)

        assertThat(result).isNotNull
        assertThat(result[0].betName).isEqualTo(OddsResponseData.bets[0].betName)
        assertThat(result[1].betName).isEqualTo(OddsResponseData.bets[1].betName)
    }

    @Test
    fun willCheckDaysDifferenceBetweenTwoDates() {

        assertThat(underTest.daysBetween(null, "2025-08-04T16:30:00+00:00"))
            .isNull()
        assertThat(underTest.daysBetween("2025-08-04T16:30:00+00:00", null))
            .isNull()
        assertThat(underTest.daysBetween("", "2025-08-04T16:30:00+00:00"))
            .isNull()
        assertThat(underTest.daysBetween("2025-08-04T16:30:00+00:00", ""))
            .isNull()
        assertThat(underTest.daysBetween("2025-08-02T16:30:00+00:00", "2025-08-04T16:30:00+00:00"))
            .isEqualTo(2)

    }

    @Test
    fun willGetTheRightStatus() {
        assertThat(underTest.teamRestStatus(7)).isEqualTo(TeamRestStatus.GOOD_REST.status)
        assertThat(underTest.teamRestStatus(3)).isEqualTo(TeamRestStatus.MODERATE_CONGESTION.status)
        assertThat(underTest.teamRestStatus(1)).isEqualTo(TeamRestStatus.SEVERE_CONGESTION.status)
        assertThat(underTest.teamRestStatus(-1)).isEqualTo(TeamRestStatus.UNKNOWN_STATE.status)
    }
}