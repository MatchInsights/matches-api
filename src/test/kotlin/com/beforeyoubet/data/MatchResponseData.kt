package com.beforeyoubet.data

import com.beforeyoubet.clientData.Fixture
import com.beforeyoubet.clientData.Goal
import com.beforeyoubet.clientData.League
import com.beforeyoubet.clientData.MatchResponse
import com.beforeyoubet.clientData.MatchStatus
import com.beforeyoubet.clientData.Score
import com.beforeyoubet.clientData.Team
import com.beforeyoubet.clientData.Teams
import com.beforeyoubet.clientData.Venue

class MatchResponseData {
    companion object {
        val matchResponse = MatchResponse(
            fixture = Fixture(
                id = 123456,
                date = "2025-08-02T16:30:00+00:00",
                status = MatchStatus(
                    long = "Match Finished",
                    short = "FT",
                    elapsed = 90,
                    extra = null
                ),
                venue = Venue(
                    name = "Old Trafford",
                    city = "Manchester"
                )
            ),
            league = League(
                id = 39,
                name = "Premier League",
                country = "England",
                logo = "https://media.api-sports.io/football/leagues/39.png",
                flag = "https://media.api-sports.io/flags/gb.svg",
                season = 2025,
                round = "Regular Season - 1"
            ),
            teams = Teams(
                home = Team(
                    id = 33,
                    name = "Manchester United",
                    logo = "https://media.api-sports.io/football/teams/33.png",
                    winner = true,
                    goals = 3
                ),
                away = Team(
                    id = 40,
                    name = "Liverpool",
                    logo = "https://media.api-sports.io/football/teams/40.png",
                    winner = false,
                    goals = 1
                )
            ),
            goals = Goal(
                home = 3,
                away = 1
            ),
            score = Score(
                halftime = Goal(home = 2, away = 1),
                fulltime = Goal(home = 3, away = 1)
            ),
            venue = Venue(
                name = "Old Trafford",
                city = "Manchester"
            )
        )

    }
}