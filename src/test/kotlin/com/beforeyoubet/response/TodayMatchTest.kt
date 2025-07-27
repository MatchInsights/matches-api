package com.beforeyoubet.response

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TodayMatchTest {
    val matchData: Map<String, Any> = mapOf(
        "fixture" to mapOf(
            "id" to 1327475,
            "referee" to "Elijio Arreguin",
            "timezone" to "UTC",
            "date" to "2025-07-27T00:00:00+00:00",
            "timestamp" to 1753574400,
            "periods" to mapOf(
                "first" to 1753574400,
                "second" to 1753578000
            ),
            "venue" to mapOf(
                "id" to 20751,
                "name" to "Protective Stadium",
                "city" to "Birmingham, Alabama"
            ),
            "status" to mapOf(
                "long" to "Match Finished",
                "short" to "FT",
                "elapsed" to 90,
                "extra" to 6
            )
        ),
        "league" to mapOf(
            "id" to 1095,
            "name" to "USL League One Cup",
            "country" to "USA",
            "logo" to "https://media.api-sports.io/football/leagues/1095.png",
            "flag" to "https://media.api-sports.io/flags/us.svg",
            "season" to 2025,
            "round" to "Group Stage - 8",
            "standings" to false
        ),
        "teams" to mapOf(
            "home" to mapOf(
                "id" to 3989,
                "name" to "Birmingham Legion",
                "logo" to "https://media.api-sports.io/football/teams/3989.png",
                "winner" to true
            ),
            "away" to mapOf(
                "id" to 9025,
                "name" to "Forward Madison",
                "logo" to "https://media.api-sports.io/football/teams/9025.png",
                "winner" to false
            )
        ),
        "goals" to mapOf(
            "home" to 2,
            "away" to 1
        ),
        "score" to mapOf(
            "halftime" to mapOf(
                "home" to 1,
                "away" to 0
            ),
            "fulltime" to mapOf(
                "home" to 2,
                "away" to 1
            ),
            "extratime" to mapOf(
                "home" to null,
                "away" to null
            ),
            "penalty" to mapOf(
                "home" to null,
                "away" to null
            )
        )
    )


    @Test
    fun shouldCreateTodayFixturesResponseFromMapData() {
        val response = TodayMatch.fromMapData(matchData)

        assertThat(response.date).isEqualTo("2025-07-27T00:00:00+00:00")
        assertThat(response.timeZone).isEqualTo("UTC")
        assertThat(response.venue?.name).isEqualTo("Protective Stadium")
        assertThat(response.venue?.city).isEqualTo("Birmingham, Alabama")
        assertThat(response.matchStatus?.long).isEqualTo("Match Finished")
        assertThat(response.matchStatus?.short).isEqualTo("FT")
        assertThat(response.matchStatus?.elapsed).isEqualTo(90)
        assertThat(response.league?.name).isEqualTo("USL League One Cup")
        assertThat(response.league?.country).isEqualTo("USA")
        assertThat(response.homeTeam?.name).isEqualTo("Birmingham Legion")
        assertThat(response.awayTeam?.name).isEqualTo("Forward Madison")
    }
}