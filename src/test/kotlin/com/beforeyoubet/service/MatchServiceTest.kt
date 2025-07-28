package com.beforeyoubet.service

import com.beforeyoubet.client.ApiSportsClient
import com.beforeyoubet.model.MatchStatus
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.String
import kotlin.collections.Map

class MatchServiceTest {

    val apiSportsClient: ApiSportsClient = mockk()
    val underTest = MatchService(apiSportsClient)
    val matchData: Map<String, Any> = mapOf(
        "fixture" to mapOf(
            "id" to 1327475,
            "timezone" to "UTC",
            "date" to "2025-07-27T00:00:00+00:00",
            "timestamp" to 1753574400,

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
    fun shouldGetTodayMatches() {
        every { apiSportsClient.fethTodayMatches(any()) } returns listOf(matchData)

        val matches = underTest.getTodayMatches(MatchStatus.LIVE)

        assertThat(matches).hasSize(1)

        verify { apiSportsClient.fethTodayMatches(any()) }

    }

}