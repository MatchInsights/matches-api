package com.beforeyoubet.service

import com.beforeyoubet.component.Apidata
import com.beforeyoubet.component.DataManipulation
import com.beforeyoubet.response.Bet

import org.springframework.stereotype.Service

@Service
class OddsService(private val apidata: Apidata, private val dataManipulation: DataManipulation) {

    fun fetchAllOdds(fixtureId: Int): List<Bet> =
        dataManipulation.extractBets(apidata.fetchAllOdds(fixtureId))
}