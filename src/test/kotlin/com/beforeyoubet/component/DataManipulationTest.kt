package com.beforeyoubet.component

import com.beforeyoubet.data.MatchResponseData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DataManipulationTest {
    val underTest = DataManipulation()

    @Test
    fun shouldCalculateSeasonStats() {

        val result = underTest.seasonTeamStats(
            teamId = 33,
            MatchResponseData.Companion.matchResponseList
        )

        assertThat(result.avgGoalsFor).isEqualTo(3.33f)
        assertThat(result.avgGoalsAgainst).isEqualTo(2.0f)
        assertThat(result.scoredInPercent).isEqualTo(100.0f)
        assertThat(result.concededInPercent).isEqualTo(100.0f)
        assertThat(result.cleanSheetPercent).isEqualTo(0.0f)
    }
}