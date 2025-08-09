package com.beforeyoubet.response

import com.beforeyoubet.data.MatchClientResponseData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MatchDetailsTest {

    @Test
    fun shouldMapFromMatchResponse() {
        val response = MatchDetails.fromResponseData(MatchClientResponseData.matchResponse)

        assertThat(response.date).isEqualTo(MatchClientResponseData.matchResponse.fixture.date)
        assertThat(response.venue).isEqualTo(MatchClientResponseData.matchResponse.venue)
        assertThat(response.league).isEqualTo(MatchClientResponseData.matchResponse.league)
        assertThat(response.homeTeam).isEqualTo(MatchClientResponseData.matchResponse.teams.home)
        assertThat(response.awayTeam).isEqualTo(MatchClientResponseData.matchResponse.teams.away)
        assertThat(response.goals).isEqualTo(MatchClientResponseData.matchResponse.goals)
        assertThat(response.score).isEqualTo(MatchClientResponseData.matchResponse.score)
    }
}