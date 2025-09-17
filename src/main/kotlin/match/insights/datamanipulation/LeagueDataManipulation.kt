package match.insights.datamanipulation

import match.insights.clientData.LeagueAndCountry
import match.insights.clientData.LeagueStandings
import match.insights.clientData.LeagueWithStandings
import match.insights.response.CountryLeagues
import match.insights.response.LeagueBasicInfo
import match.insights.response.LeagueGroup
import match.insights.response.LeagueInfo
import match.insights.response.LeagueTeamInfo
import match.insights.response.LeaguesGroups
import match.insights.response.PositionAndPoints
import match.insights.response.TeamPositionsAndPoints
import org.springframework.stereotype.Component

@Component
class LeagueDataManipulation {

    private val internationalNames = setOf(
        "world",
        "europe",
        "south america",
        "asia",
        "africa",
        "north & central america",
        "oceania"
    )

    fun positionAndPoints(
        homeTeamId: Int, awayTeamId: Int, leagueWithStandings: LeagueWithStandings?
    ): TeamPositionsAndPoints {
        return leagueWithStandings?.let { league ->
            val groupsData: List<LeagueGroup> = league.standings.toLeagueGroups()
            TeamPositionsAndPoints(
                homeTeam = rankAndPointsByTeamId(homeTeamId, groupsData),
                awayTeam = rankAndPointsByTeamId(awayTeamId, groupsData)
            )
        } ?: TeamPositionsAndPoints(homeTeam = emptyList(), awayTeam = emptyList())
    }


    fun extractLeaguesInfo(leagueWithStandings: LeagueWithStandings?): LeagueInfo {
        return leagueWithStandings?.let { league ->
            LeagueInfo(
                id = league.id,
                name = "Unknown League",
                country = "Unknown Country",
                logo = "Unknown Logo",
                flag = "Unknown Flag",
                season = league.season,
                group = league.standings.toLeagueGroups()
            )
        } ?: LeagueInfo(
            id = -1,
            name = "Unknown League",
            country = "Unknown Country",
            logo = "Unknown Logo",
            flag = "Unknown Flag",
            season = -1,
            group = emptyList()
        )
    }

    private fun List<List<LeagueStandings>>.toLeagueGroups(): List<LeagueGroup> =
        this.flatten().groupBy { it.group ?: "Default" }
            .map { (key, value) -> LeagueGroup(key, value.toLeagueTeamInfoList()) }


    private fun List<LeagueStandings>.toLeagueTeamInfoList(): List<LeagueTeamInfo> {
        return this.map {
            LeagueTeamInfo(
                teamId = it.team.id,
                rank = it.rank,
                teamName = it.team.name,
                logo = it.team.logo ?: "",
                points = it.points,
                played = it.all?.played ?: 0,
                won = it.all?.win ?: 0,
                draw = it.all?.draw ?: 0,
                lost = it.all?.lose ?: 0,
                goalsFor = it.all?.goals?.`for` ?: 0,
                goalsAgainst = it.all?.goals?.against ?: 0,
                form = it.form ?: "",
                update = it.update
            )
        }
    }


    fun groupLeagues(leagues: List<LeagueAndCountry>): LeaguesGroups {
        val internationals = mutableListOf<LeagueBasicInfo>()
        val byCountry = mutableMapOf<String, MutableList<LeagueBasicInfo>>()
        val others = mutableListOf<LeagueBasicInfo>()

        leagues.forEach { leagueAndCountry ->
            val name = leagueAndCountry.country.name.lowercase()

            when {
                leagueAndCountry.country.code != null -> {
                    byCountry.computeIfAbsent(leagueAndCountry.country.name) { mutableListOf() }
                        .add(
                            LeagueBasicInfo.fromLeague(leagueAndCountry.league)
                        )
                }

                internationalNames.contains(name) -> {
                    internationals.add(
                        LeagueBasicInfo.fromLeague(leagueAndCountry.league)
                    )
                }

                else -> {
                    others.add(
                        LeagueBasicInfo.fromLeague(leagueAndCountry.league)
                    )
                }
            }
        }

        return LeaguesGroups(
            internationals = internationals,
            countryLeagues = byCountry.map { (country, leagues) ->
                CountryLeagues(
                    country = country,
                    flag = leagues.firstOrNull()?.logo,
                    leagues = leagues
                )
            },
            others = others
        )
    }

    private fun rankAndPointsByTeamId(
        id: Int,
        groupsData: List<LeagueGroup>
    ): List<PositionAndPoints> =
        groupsData.mapNotNull { (label, teams) ->
            teams.firstOrNull { teamInfo -> teamInfo.teamId == id }
                ?.let { PositionAndPoints(it.rank, it.points, label) }
        }
}