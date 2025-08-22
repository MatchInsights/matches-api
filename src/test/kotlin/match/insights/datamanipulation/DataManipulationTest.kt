package match.insights.datamanipulation

import match.insights.data.client.ClientMatchResponseData
import match.insights.data.client.ClientOddsData
import match.insights.data.response.OddsResponseData
import match.insights.model.Odd
import match.insights.model.OddFeeling

import match.insights.model.TeamRestStatus
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

        assertThat(result.goalsFor).isEqualTo(12)
        assertThat(result.goalsAgainst).isEqualTo(8)
        assertThat(result.scoredIn).isEqualTo(4)
        assertThat(result.concededIn).isEqualTo(4)
        assertThat(result.cleanSheet).isEqualTo(0)
    }

    @Test
    fun willReturnLastFiveResults() {
        val result = underTest.lastFiveResults(
            teamId = 33,
            ClientMatchResponseData.matchResponseList
        )

        assertThat(result).containsExactly("W", "W", "L", "D")
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


    @Test
    fun shouldCaptureOddsFeeling() {
        val result = underTest.oddsFeeling(
            ClientOddsData.mockResponse
        )

        assertThat(result).isEqualTo(
            mapOf
                (
                Odd.HOME to OddFeeling.STRONG,
                Odd.DRAW to OddFeeling.WEAK,
                Odd.AWAY to OddFeeling.WEAK
            )
        )

    }
}