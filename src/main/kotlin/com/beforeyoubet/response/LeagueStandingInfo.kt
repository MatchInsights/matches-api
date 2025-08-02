package com.beforeyoubet.response

import com.beforeyoubet.clientData.Standing

data class LeagueStandingInfo(
    val rank: Int,
    val teamName: String,
    val logo: String,
    val points: Int,
    val played: Int,
    val won: Int,
    val draw: Int,
    val lost: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val form: String
) {
    companion object {
        fun fromApiResponse(standings: List<Standing>): List<LeagueStandingInfo> {
            return standings.map {
                LeagueStandingInfo(
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
                    form = it.form ?: ""
                )
            }
        }
    }
}
