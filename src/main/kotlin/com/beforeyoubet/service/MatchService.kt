package com.beforeyoubet.service

import com.beforeyoubet.client.ApiSportsClient
import com.beforeyoubet.response.TodayMatch

import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDate
import java.time.ZonedDateTime


@Service
class MatchService(private val apiSportsClient: ApiSportsClient) {

    fun getTodayMatches(): List<TodayMatch> {
        val today = LocalDate.now().toString()

        val response = apiSportsClient.fethTodayMatches("/fixtures?date=$today")

        return response.map { TodayMatch.fromMapData(it) }
            .sortedByDescending { ZonedDateTime.parse(it.date) }
    }
}