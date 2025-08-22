package match.insights.response

import match.insights.clientData.Goal
import match.insights.clientData.League
import match.insights.clientData.MatchResponse
import match.insights.clientData.Score
import match.insights.clientData.Team
import match.insights.clientData.Venue

data class MatchDetails(
    val id: Int,
    val date: String,
    val league: League,
    val venue: Venue,
    val homeTeam: Team,
    val awayTeam: Team,
    val goals: Goal,
    val score: Score
) {
    companion object {
        fun fromResponseData(
            matchResponse: MatchResponse,

            ): MatchDetails {
            return MatchDetails(
                matchResponse.fixture.id,
                matchResponse.fixture.date,
                matchResponse.league,
                matchResponse.fixture.venue ?: Venue(),
                matchResponse.teams.home ?: Team(),
                matchResponse.teams.away ?: Team(),
                matchResponse.goals ?: Goal(),
                matchResponse.score ?: Score(),

                )
        }
    }
}