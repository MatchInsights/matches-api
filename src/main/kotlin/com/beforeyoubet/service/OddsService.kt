package com.beforeyoubet.service

import com.beforeyoubet.client.ApiSportsClient
import com.beforeyoubet.clientData.FixtureOdds
import com.beforeyoubet.response.Bet
import com.beforeyoubet.response.SingleOdd
import org.springframework.stereotype.Service

@Service
class OddsService(private val apiSportsClient: ApiSportsClient) {


    fun fetchAllOdds(fixtureId: Int): List<Bet> {
        val uri = "/odds?fixture=$fixtureId"
        val apiResponse = apiSportsClient.fetchFixtureOdds(uri)
        return extractBets(apiResponse)
    }


    private fun extractBets(data: List<FixtureOdds>): List<Bet> {
        val allBets = mutableListOf<Bet>()

        data
            .flatMap { it.bookmakers }
            .firstOrNull()
            ?.bets
            ?.forEach { bet ->
                val odds = bet.values.mapNotNull { value ->
                    val oddDouble = value.odd.toDoubleOrNull()
                    if (oddDouble != null) SingleOdd(label = value.value, odd = oddDouble) else null
                }

                if (odds.isNotEmpty()) {
                    allBets.add(
                        Bet(
                            betName = bet.name,
                            values = odds
                        )
                    )
                }
            }

        return allBets
    }
}