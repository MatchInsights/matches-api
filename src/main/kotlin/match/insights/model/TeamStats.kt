package match.insights.model

data class TeamStats(
    val goalsFor: Int,
    val goalsAgainst: Int,
    val cleanSheet: Int,
    val scoredIn: Int,
    val concededIn: Int
)