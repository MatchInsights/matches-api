package com.beforeyoubet.response

import com.beforeyoubet.clientData.Goal
import com.beforeyoubet.clientData.League
import com.beforeyoubet.clientData.MatchResponse
import com.beforeyoubet.clientData.Score
import com.beforeyoubet.clientData.Team
import com.beforeyoubet.clientData.Venue

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
        fun fromResponseData(matchResponse: MatchResponse): MatchDetails {
            return MatchDetails(
                matchResponse.fixture.id,
                matchResponse.fixture.date,
                matchResponse.league,
                matchResponse.fixture.venue ?: Venue(),
                matchResponse.teams.home ?: Team(),
                matchResponse.teams.away ?: Team(),
                matchResponse.goals ?: Goal(),
                matchResponse.score ?: Score()

            )
        }
    }
}