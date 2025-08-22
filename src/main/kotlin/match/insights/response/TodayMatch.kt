package match.insights.response

import match.insights.clientData.League
import match.insights.clientData.MatchResponse
import match.insights.clientData.MatchStatus
import match.insights.clientData.Team
import match.insights.clientData.Venue

data class TodayMatch(
    val id: Int,
    val date: String,
    val venue: Venue,
    val matchStatus: MatchStatus,
    val league: League,
    val homeTeam: Team,
    val awayTeam: Team
) {
    companion object {

        fun fromResponseData(matchResponse: MatchResponse): TodayMatch {

            return TodayMatch(
                matchResponse.fixture.id,
                matchResponse.fixture.date,
                matchResponse.fixture.venue ?: Venue(),
                matchResponse.fixture.status ?: MatchStatus(),
                matchResponse.league,
                matchResponse.teams.home ?: Team(),
                matchResponse.teams.away ?: Team()
            )
        }
    }
}