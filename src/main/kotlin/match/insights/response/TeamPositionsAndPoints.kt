package match.insights.response

import match.insights.clientData.Standing

data class TeamPositionsAndPoints(
    val homeTeamPosition: Int?,
    val awayTeamPosition: Int?,
    val homeTeamPoints: Int?,
    val awayTeamPoints: Int?
) {
    companion object {
        fun fromApiResponse(homeTeamStanding: Standing?, awayTeamStanding: Standing?): TeamPositionsAndPoints =
            TeamPositionsAndPoints(
                homeTeamStanding?.rank,
                awayTeamStanding?.rank,
                homeTeamStanding?.points,
                awayTeamStanding?.points
            )
    }
}