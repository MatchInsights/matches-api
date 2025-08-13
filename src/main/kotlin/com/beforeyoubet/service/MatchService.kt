package com.beforeyoubet.service

import com.beforeyoubet.apidata.Apidata
import com.beforeyoubet.model.MatchStatus
import com.beforeyoubet.response.MatchDetails
import com.beforeyoubet.response.TodayMatch

import org.springframework.stereotype.Service

import java.time.LocalDate
import java.time.ZonedDateTime


@Service
class MatchService(
    private val apidata: Apidata
) {

    fun getTodayMatches(status: MatchStatus): List<TodayMatch> {
        val today = LocalDate.now().toString()
        val response = apidata.todayMatches(today, status.code)
        return response.map { TodayMatch.fromResponseData(it) }.sortedByDescending { ZonedDateTime.parse(it.date) }
    }

    fun getMatchDetails(matchId: Int): MatchDetails = apidata.matchDetails(matchId).let {
        MatchDetails.fromResponseData(it)
    }
}