package com.beforeyoubet.service

import com.beforeyoubet.client.ApiSportsClient
import com.beforeyoubet.model.MatchStatus
import com.beforeyoubet.response.MatchDetails
import com.beforeyoubet.response.TodayMatch

import org.springframework.stereotype.Service

import java.time.LocalDate
import java.time.ZonedDateTime


@Service
class MatchService(private val apiSportsClient: ApiSportsClient) {

    fun getTodayMatches(status: MatchStatus): List<TodayMatch> {
        val today = LocalDate.now().toString()

        val response = apiSportsClient.fetchMatches("/fixtures?date=$today&status=${status.code}")

        return response.map { TodayMatch.fromResponseData(it) }
            .sortedByDescending { ZonedDateTime.parse(it.date) }
    }

    fun getMatchDetails(matchId: Int): MatchDetails {

        val response = apiSportsClient.fetchMatchDetails("/fixtures?id=${matchId}")

        return MatchDetails.fromResponseData(response)
    }
}