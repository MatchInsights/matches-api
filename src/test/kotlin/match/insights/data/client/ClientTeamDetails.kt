package match.insights.data.client

import match.insights.clientData.CoachCareer
import match.insights.clientData.CoachResponse
import match.insights.clientData.Player
import match.insights.clientData.SquadResponse
import match.insights.clientData.Team
import match.insights.clientData.TeamResponse
import match.insights.clientData.Venue

class ClientTeamDetails {
    companion object {
        val details = TeamResponse(
            team = Team(
                id = 33,
                name = "Manchester United",
                country = "England",
                founded = 1878,
                logo = "https://media.api-sports.io/football/teams/33.png"
            ),
            venue = Venue(
                id = 556,
                name = "Old Trafford",
                city = "Manchester",
                capacity = 76212
            )
        )

        val squad = SquadResponse(
            team = Team(
                id = 33,
                name = "Manchester United",
                country = "England",
                founded = 1878,
                logo = "https://media.api-sports.io/football/teams/33.png"
            ),
            players = listOf(
                Player(
                    id = 101,
                    name = "David de Gea",
                    age = 32,
                    nationality = "Spain",
                    position = "Goalkeeper"
                ),
                Player(
                    id = 102,
                    name = "Harry Maguire",
                    age = 30,
                    nationality = "England",
                    position = "Defender"
                ),
                Player(
                    id = 103,
                    name = "Marcus Rashford",
                    age = 25,
                    nationality = "England",
                    position = "Attacker"
                )
            )
        )

        val coach = CoachResponse(
            id = 1234,
            name = "Erik ten Hag",
            firstname = "Erik",
            lastname = "ten Hag",
            age = 53,
            nationality = "Unkown",
            team = Team(id = 33),
            career = listOf(
                CoachCareer(
                    Team(id = 33),
                    "22/06/1990",
                    null
                )
            )
        )

    }
}