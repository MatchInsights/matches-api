package com.beforeyoubet.data.client

import com.beforeyoubet.clientData.CoachResponse
import com.beforeyoubet.clientData.Player
import com.beforeyoubet.clientData.SquadResponse
import com.beforeyoubet.clientData.Team
import com.beforeyoubet.clientData.TeamResponse
import com.beforeyoubet.clientData.TrophyResponse
import com.beforeyoubet.clientData.Venue

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
            age = 53
        )

        val trophies = listOf(
            TrophyResponse(
                league = "Premier League",
                country = "England",
                season = "2022/2023",
                place = "3rd Place"
            ),
            TrophyResponse(
                league = "EFL Cup",
                country = "England",
                season = "2022/2023",
                place = "Winner"
            ),
            TrophyResponse(
                league = "FA Cup",
                country = "England",
                season = "2022/2023",
                place = "Finalist"
            )
        )
    }
}