package match.insights.data.client

import match.insights.clientData.Goals
import match.insights.clientData.RecordStats
import match.insights.clientData.Standing
import match.insights.clientData.Team

class ClientStandingData {
    companion object {

        val standing = Standing(
            rank = 1,
            team = Team(
                id = 33,
                name = "Manchester United",
                logo = "https://media.api-sports.io/football/teams/33.png",
                winner = null,
                goals = null
            ),
            points = 89,
            goalsDiff = 45,
            form = "WWDWW",
            status = "same",
            description = "Champions League",
            group = "Premier League",
            all = RecordStats(
                played = 38,
                win = 28,
                draw = 5,
                lose = 5,
                goals = Goals(
                    `for` = 85,
                    against = 40
                )
            ),
            home = RecordStats(
                played = 19,
                win = 15,
                draw = 3,
                lose = 1,
                goals = Goals(
                    `for` = 50,
                    against = 15
                )
            ),
            away = RecordStats(
                played = 19,
                win = 13,
                draw = 2,
                lose = 4,
                goals = Goals(
                    `for` = 35,
                    against = 25
                )
            ),
            update = "2025-05-20T18:00:00+00:00"
        )

    }


}