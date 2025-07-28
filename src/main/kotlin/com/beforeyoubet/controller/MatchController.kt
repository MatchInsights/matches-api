package com.beforeyoubet.controller

import com.beforeyoubet.model.MatchStatus
import com.beforeyoubet.response.TodayMatch
import com.beforeyoubet.service.MatchService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/matches")
class MatchController(private val matchService: MatchService) {

    @GetMapping("/today/{status}")
    fun getMatches(@PathVariable status: MatchStatus): List<TodayMatch> =
        matchService.getTodayMatches(status)

}