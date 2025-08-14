package com.beforeyoubet.service

import com.beforeyoubet.apidata.Apidata
import com.beforeyoubet.datamanipulation.DataManipulation
import com.beforeyoubet.model.Odd
import com.beforeyoubet.model.OddFeeling
import com.beforeyoubet.response.Bet
import com.beforeyoubet.response.OddsWinnerFeeling

import org.springframework.stereotype.Service

@Service
class OddsService(private val apidata: Apidata, private val dataManipulation: DataManipulation) {

    fun fetchAllOdds(fixtureId: Int): List<Bet> =
        dataManipulation.extractBets(apidata.fetchAllOdds(fixtureId))

    fun oddsWinnerFeeling(fixtureId: Int): OddsWinnerFeeling =
        dataManipulation.oddsFeeling(apidata.fetchAllOdds(fixtureId))
            .let {
                OddsWinnerFeeling(
                    it[Odd.HOME]?.value ?: OddFeeling.NO_DATA.value,
                    it[Odd.DRAW]?.value ?: OddFeeling.NO_DATA.value,
                    it[Odd.AWAY]?.value ?: OddFeeling.NO_DATA.value
                )
            }


}