package com.beforeyoubet.data

import com.beforeyoubet.clientData.Bet
import com.beforeyoubet.clientData.Bookmaker
import com.beforeyoubet.clientData.FixtureOdds
import com.beforeyoubet.clientData.OddValue

class OddsData {
    companion object {
        val mockResponse = listOf(
            FixtureOdds(
                bookmakers = listOf(
                    Bookmaker(
                        name = "Bet365", bets = listOf(
                            Bet(
                                name = "Match Winner", values = listOf(
                                    OddValue("Home Team", "1.95"), OddValue("Draw", "3.2"), OddValue("Away Team", "3.8")
                                )
                            ), Bet(
                                name = "Odd/Even - First Half", values = listOf(
                                    OddValue("Odd", "2.05"), OddValue("Even", "1.8")
                                )
                            )
                        )
                    )
                )
            )
        )


    }
}