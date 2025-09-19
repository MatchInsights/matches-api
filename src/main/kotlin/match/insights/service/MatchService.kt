package match.insights.service

import match.insights.apidata.MatchesData
import match.insights.model.MatchStatus
import match.insights.response.MatchDetails
import match.insights.response.TodayMatch
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@Service
class MatchService(
    private val apidata: MatchesData
) {

    fun getTodayMatches(status: MatchStatus, leagueId: Int?): List<TodayMatch> {
        val utcNow = ZonedDateTime.now(ZoneId.of("UTC"))
        val today = utcNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val response = apidata.todayMatches(today, status.code, leagueId)

        return response.map { TodayMatch.fromResponseData(it) }.sortedByDescending { ZonedDateTime.parse(it.date) }
    }

    fun getMatchDetails(matchId: Int): MatchDetails = apidata.matchDetails(matchId).let {
        MatchDetails.fromResponseData(it)
    }
}