package com.beforeyoubet.response

import com.beforeyoubet.data.MatchResponseData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MatchDetailsTest {

    @Test
    fun shouldMapFromMatchResponse() {
        val response = MatchDetails.fromResponseData(MatchResponseData.matchResponse)

        assertThat(response.date).isEqualTo(MatchResponseData.matchResponse.fixture.date)
        assertThat(response.venue).isEqualTo(MatchResponseData.matchResponse.venue)
        assertThat(response.league).isEqualTo(MatchResponseData.matchResponse.league)
        assertThat(response.homeTeam).isEqualTo(MatchResponseData.matchResponse.teams.home)
        assertThat(response.awayTeam).isEqualTo(MatchResponseData.matchResponse.teams.away)
        assertThat(response.goals).isEqualTo(MatchResponseData.matchResponse.goals)
        assertThat(response.score).isEqualTo(MatchResponseData.matchResponse.score)
    }
}