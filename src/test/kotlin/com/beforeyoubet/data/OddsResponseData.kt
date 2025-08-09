package com.beforeyoubet.data


import com.beforeyoubet.response.Bet
import com.beforeyoubet.response.SingleOdd

class OddsResponseData {
    companion object {
        val bets = listOf<Bet>(
            Bet(
                betName = "Match Winner", values = listOf(
                    SingleOdd(label = "Home Team", odd = 1.95),
                )
            ), Bet(
                betName = "Odd/Even - First Half", values = listOf(
                    SingleOdd(label = "Odd", odd = 2.05)
                )
            )
        )
    }
}