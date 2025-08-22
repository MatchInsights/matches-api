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
    val country: String? = "Unknown Country",
    val logo: String? = "",
    val flag: String? = "",
    val season: Int,
    val round: String? = ""
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

data class LeagueWithStandings(
    val id: Int,
    val name: String? = "Unknown League",
    val country: String? = "Unknown Country",
    val logo: String? = "",
    val flag: String? = "",
    val season: Int,
    val standings: List<List<Standing>>
) : Serializable

data class RecordStats(
    val played: Int? = 0,
    val win: Int? = 0,
    val draw: Int? = 0,
    val lose: Int? = 0,
    val goals: Goals? = Goals()
) : Serializable

data class Goals(
    val `for`: Int? = 0,
    val against: Int? = 0
) : Serializable

data class Standing(
    val rank: Int,
    val team: Team,
    val points: Int,
    val goalsDiff: Int,
    val form: String? = "",
    val status: String? = "",
    val description: String? = "",
    val group: String? = "",
    val all: RecordStats? = RecordStats(),
    val home: RecordStats? = RecordStats(),
    val away: RecordStats? = RecordStats(),
    val update: String? = ""
) : Serializable

data class FixtureOdds(val bookmakers: List<Bookmaker>) : Serializable

data class Bookmaker(val name: String, val bets: List<Bet>) : Serializable

data class Bet(val name: String, val values: List<OddValue>) : Serializable

data class OddValue(val value: String, val odd: String) : Serializable

data class Event(
    val time: Time,
    val team: Team,
    val player: Player,
    val type: String,       // e.g. "Card", "Goal", "Penalty", etc.
    val detail: String,     // e.g. "Yellow Card", "Red Card", "Penalty"
    val comments: String? = null
) : Serializable

data class Time(
    val elapsed: Int,
    val extra: Int? = null
) : Serializable

data class Player(
    val id: Int,
    val name: String,
    val age: Int? = -1,
    val nationality: String? = "Unknown Nationality",
    val position: String? = "Unknown Position"
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

