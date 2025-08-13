package com.beforeyoubet.service

import com.beforeyoubet.apidata.Apidata
import com.beforeyoubet.datamanipulation.DataManipulation
import com.beforeyoubet.response.Bet

import org.springframework.stereotype.Service

@Service
class OddsService(private val apidata: Apidata, private val dataManipulation: DataManipulation) {

    fun fetchAllOdds(fixtureId: Int): List<Bet> =
        dataManipulation.extractBets(apidata.fetchAllOdds(fixtureId))
}