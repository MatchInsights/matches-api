package match.insights.response

import match.insights.data.client.ClientMatchResponseData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TodayMatchTest {

    @Test
    fun shouldMapFromMatchResponse() {
        val response = TodayMatch.fromResponseData(ClientMatchResponseData.matchResponse)

        assertThat(response.date).isEqualTo(ClientMatchResponseData.matchResponse.fixture.date)
        assertThat(response.venue).isEqualTo(ClientMatchResponseData.matchResponse.venue)
        assertThat(response.matchStatus).isEqualTo(ClientMatchResponseData.matchResponse.fixture.status)
        assertThat(response.league).isEqualTo(ClientMatchResponseData.matchResponse.league)
        assertThat(response.homeTeam).isEqualTo(ClientMatchResponseData.matchResponse.teams.home)
        assertThat(response.awayTeam).isEqualTo(ClientMatchResponseData.matchResponse.teams.away)
    }
}