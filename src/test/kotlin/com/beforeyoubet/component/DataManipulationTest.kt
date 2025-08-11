package com.beforeyoubet.component

import com.beforeyoubet.data.client.ClientMatchResponseData
import com.beforeyoubet.data.client.ClientOddsData
import com.beforeyoubet.data.response.OddsResponseData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DataManipulationTest {
    val underTest = DataManipulation()

    @Test
    fun shouldCalculateSeasonStats() {

        val result = underTest.seasonTeamStats(
            teamId = 33,
            ClientMatchResponseData.matchResponseList
        )

        assertThat(result.avgGoalsFor).isEqualTo(3.33f)
        assertThat(result.avgGoalsAgainst).isEqualTo(2.0f)
        assertThat(result.scoredInPercent).isEqualTo(100.0f)
        assertThat(result.concededInPercent).isEqualTo(100.0f)
        assertThat(result.cleanSheetPercent).isEqualTo(0.0f)
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
}