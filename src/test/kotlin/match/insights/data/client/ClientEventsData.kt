package match.insights.data.client

import match.insights.clientData.Event
import match.insights.clientData.Player
import match.insights.clientData.Team
import match.insights.clientData.Time

class ClientEventsData {
    companion object {
        val mockEvents = listOf(
            Event(
                time = Time(elapsed = 12),
                team = Team(id = 33, name = "Team A", logo = "https://media.api-sports.io/football/teams/33.png"),
                player = Player(id = 101, name = "John Doe"),
                type = "Goal",
                detail = "Normal Goal",
                comments = null
            ),
            Event(
                time = Time(elapsed = 30),
                team = Team(id = 44, name = "Team B", logo = "https://media.api-sports.io/football/teams/44.png"),
                player = Player(id = 102, name = "Michael Johnson"),
                type = "Card",
                detail = "Yellow Card",
                comments = "Foul on player"
            ),
            Event(
                time = Time(elapsed = 45, extra = 2),
                team = Team(id = 33, name = "Team A", logo = "https://media.api-sports.io/football/teams/33.png"),
                player = Player(id = 103, name = "Robert Brown"),
                type = "Goal",
                detail = "Penalty",
                comments = "Penalty scored in injury time"
            ),
            Event(
                time = Time(elapsed = 78),
                team = Team(id = 44, name = "Team B", logo = "https://media.api-sports.io/football/teams/44.png"),
                player = Player(id = 104, name = "Chris Lee"),
                type = "Substitution",
                detail = "Player Substitution",
                comments = "Replaced injured player"
            ),
            Event(
                time = Time(elapsed = 90, extra = 4),
                team = Team(id = 33, name = "Team A", logo = "https://media.api-sports.io/football/teams/33.png"),
                player = Player(id = 105, name = "David Wilson"),
                type = "Card",
                detail = "Red Card",
                comments = "Serious foul play"
            )
        )

    }
}