package com.beforeyoubet.controller

import com.beforeyoubet.response.TodayMatch
import com.beforeyoubet.service.MatchService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/matches")
class MatchController(private val matchService: MatchService) {

    @GetMapping("/today")
    fun getMatches(): List<TodayMatch> =
        matchService.getTodayMatches()

}