package com.beforeyoubet.response

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
    val form: String?
) {
    companion object {
        fun fromRawData(raw: List<Map<String, Any>>): List<LeagueStandingInfo> {
            return raw.map {
                val team = it["team"] as Map<String, Any>
                val stats = it["all"] as Map<String, Any>
                LeagueStandingInfo(
                    rank = (it["rank"] as Number).toInt(),
                    teamName = team["name"].toString(),
                    logo = team["logo"].toString(),
                    points = (it["points"] as Number).toInt(),
                    played = (stats["played"] as Number).toInt(),
                    won = (stats["win"] as Number).toInt(),
                    draw = (stats["draw"] as Number).toInt(),
                    lost = (stats["lose"] as Number).toInt(),
                    goalsFor = (stats["goals"] as Map<String, Any>)["for"] as Int,
                    goalsAgainst = (stats["goals"] as Map<String, Any>)["against"] as Int,
                    form = it["form"]?.toString()
                )
            }
        }
    }
}
