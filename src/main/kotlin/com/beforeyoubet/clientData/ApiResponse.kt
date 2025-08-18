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

data class TeamResponse(
    val team: Team,
    val venue: Venue
)

data class SquadResponse(
    val team: Team,
    val players: List<Player>
)

data class CoachResponse(
    val id: Int,
    val name: String,
    val firstname: String?,
    val lastname: String?,
    val age: Int?
)

data class TrophyResponse(
    val league: String,
    val country: String,
    val season: String,
    val place: String
)

