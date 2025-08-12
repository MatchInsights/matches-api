package com.beforeyoubet.clientData

data class ApiResponse<T>(
    val response: T
)

data class MatchResponse(
    val fixture: Fixture,
    val league: League,
    val teams: Teams,
    val goals: Goal? = Goal(),
    val score: Score? = Score(),
    val venue: Venue? = Venue()
)

data class StandingResponse(
    val league: LeagueWithStandings
)

