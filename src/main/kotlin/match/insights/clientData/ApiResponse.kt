package match.insights.clientData

import java.io.Serializable

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
) : Serializable

data class StandingResponse(
    val league: LeagueWithStandings
) : Serializable

data class TeamResponse(
    val team: Team,
    val venue: Venue
) : Serializable

data class SquadResponse(
    val team: Team,
    val players: List<Player>
) : Serializable

data class CoachResponse(
    val id: Int,
    val name: String,
    val firstname: String?,
    val lastname: String?,
    val age: Int?,
    val nationality: String?,
    val team: Team?,
    val career: List<CoachCareer>?
) : Serializable
