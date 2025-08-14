package com.beforeyoubet.controller

import com.beforeyoubet.response.Bet
import com.beforeyoubet.response.OddsWinnerFeeling
import com.beforeyoubet.service.OddsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/odds")
class OddsController(private val oddsService: OddsService) {

    @GetMapping("/{fixtureId}")
    fun getMatches(@PathVariable fixtureId: Int): List<Bet> = oddsService.fetchAllOdds(fixtureId)

    @GetMapping("/feeling/winner/{fixtureId}")
    fun getWinerFeeling(@PathVariable fixtureId: Int): OddsWinnerFeeling = oddsService.oddsWinnerFeeling(fixtureId)

}