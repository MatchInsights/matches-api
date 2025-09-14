package match.insights.clientData

import java.io.Serializable

data class Venue(
    val name: String? = "Unknown Venue",
    val city: String? = "Unknown City",
    val id: Int? = -1,
    val capacity: Int? = -1
) : Serializable

data class MatchStatus(
    val long: String = "",
    val short: String = "",
    val elapsed: Int? = 0,
    val extra: Int? = 0
) : Serializable

data class League(
    val id: Int = -1,
    val name: String = "Unknown League",
    val type: String = "Unknown Type",
    val country: String? = "Unknown Country",
    val logo: String? = "",
    val flag: String? = "",
    val season: Int,
    val round: String? = ""
) : Serializable

data class LeagueCountry(
    val name: String,
    val code: String?,
    val flag: String?
) : Serializable

data class LeagueAndCountry(
    val league: League,
    val country: LeagueCountry
) : Serializable

data class Team(
    val id: Int = -1,
    val name: String = "Unknown Team",
    val logo: String? = "",
    val winner: Boolean? = false,
    val goals: Int? = 0,
    val country: String? = "Unkown Country",
    val founded: Int? = -1
) : Serializable

data class Fixture(
    val id: Int,
    val date: String = "Unknown Date",
    val status: MatchStatus? = MatchStatus(),
    val venue: Venue? = Venue()
) : Serializable

data class Teams(
    val home: Team? = Team(),
    val away: Team? = Team()

) : Serializable

data class Goal(
    val home: Int? = 0,
    val away: Int? = 0
) : Serializable

data class Score(
    val halftime: Goal? = Goal(),
    val fulltime: Goal? = Goal(),
    val extratime: Goal? = Goal(),
    val penalty: Goal? = Goal()
) : Serializable


data class Event(
    val time: Time,
    val team: Team,
    val player: Player,
    val type: String,
    val detail: String,
    val comments: String? = null
) : Serializable

data class Time(
    val elapsed: Int,
    val extra: Int? = null
) : Serializable


enum class EventTypes(val typeName: String) {
    GOAL("Goal"),
    CARD("Card"),
    PENALTY("Penalty");
}

enum class EventTypesDetail(val detail: String) {
    YELLOW_CARD("Yellow Card"),
    RED_CARD("Red Card"),
    PENALTY("Penalty");
}

data class CoachCareer(
    val team: Team?,
    val start: String?,
    val end: String?
) : Serializable