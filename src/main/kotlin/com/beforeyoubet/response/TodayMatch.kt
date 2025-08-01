package com.beforeyoubet.response

import com.beforeyoubet.clientData.League
import com.beforeyoubet.clientData.MatchStatus
import com.beforeyoubet.clientData.Team
import com.beforeyoubet.clientData.Venue

data class TodayMatch(
    val date: String? = null,
    val timeZone: String? = null,
    val venue: Venue? = null,
    val matchStatus: MatchStatus? = null,
    val league: League? = null,
    val homeTeam: Team? = null,
    val awayTeam: Team? = null
) {
    companion object {

        fun fromMapData(mapData: Map<String, Any>): TodayMatch {
            val fixture: Map<String, Any>? = mapData["fixture"] as Map<String, Any>
            val teams: Map<String, Any>? = mapData["teams"] as Map<String, Any>
            val goals: Map<String, Any>? = mapData["goals"] as Map<String, Any>

            return TodayMatch(
                date = fixture?.let { fixture["date"] as String? },
                timeZone = fixture?.let { fixture["timezone"] as String? },
                venue = venueData(fixture?.let { fixture["venue"] as Map<String, Any> }),
                matchStatus = statusData(fixture?.let { fixture["status"] as Map<String, Any> }),
                league = leagueData(mapData["league"] as Map<String, Any>),
                homeTeam = teamData(
                    teamDataMap = teams,
                    goalsDataMap = goals,
                    key = "home"
                ),
                awayTeam = teamData(
                    teamDataMap = teams,
                    goalsDataMap = goals,
                    key = "away"
                )
            )
        }

        private fun teamData(
            teamDataMap: Map<String, Any>?,
            goalsDataMap: Map<String, Any>?,
            key: String
        ): Team? {
            return teamDataMap?.let { teamDataMap[key] as Map<String, Any> }.let { team ->
                Team(
                    name = team?.let { it["name"] as String? },
                    logo = team?.let { it["logo"] as String? },
                    winner = team?.let { it["winner"] as Boolean? },
                    goals = goalsDataMap?.let { goalsDataMap[key] as Int? }
                )
            }
        }

        private fun statusData(
            statusDataMap: Map<String, Any>?
        ): MatchStatus? =
            MatchStatus(
                long = statusDataMap?.let { statusDataMap["long"] as String? },
                short = statusDataMap?.let { statusDataMap["short"] as String? },
                elapsed = statusDataMap?.let { statusDataMap["elapsed"] as Int? },
                extra = statusDataMap?.let { statusDataMap["extra"] as Int? }
            )

        private fun venueData(
            venueDataMap: Map<String, Any>?
        ): Venue? =
            Venue(
                name = venueDataMap?.let { venueDataMap["name"] as String? },
                city = venueDataMap?.let { venueDataMap["city"] as String? }
            )

        private fun leagueData(
            leagueDataMap: Map<String, Any>?
        ): League? =
            League(
                id = leagueDataMap?.let { leagueDataMap["id"] as Int? },
                name = leagueDataMap?.let { leagueDataMap["name"] as String? },
                country = leagueDataMap?.let { leagueDataMap["country"] as String? },
                logo = leagueDataMap?.let { leagueDataMap["logo"] as String? },
                flag = leagueDataMap?.let { leagueDataMap["flag"] as String? },
                season = leagueDataMap?.let { leagueDataMap["season"] as Int? },
                round = leagueDataMap?.let { leagueDataMap["round"] as String? }
            )
    }
}