package com.beforeyoubet.response

import com.beforeyoubet.clientData.CoachResponse
import com.beforeyoubet.clientData.SquadResponse
import com.beforeyoubet.clientData.TeamResponse
import com.beforeyoubet.clientData.TrophyResponse

data class TeamDetails(
    val teamName: String,
    val teamLogo: String,
    val teamCountry: String,
    val teamFounded: Int,
    val venueName: String,
    val venueCity: String,
    val venueCapacity: Int,
    val coachName: String,
    val coachAge: Int
) {
    companion object {

        fun fromClientResponse(coachResponse: CoachResponse?, teamResponse: TeamResponse?): TeamDetails {
            return TeamDetails(
                teamResponse?.team?.name ?: "Unknown Team",
                teamResponse?.team?.logo ?: "",
                teamResponse?.team?.country ?: "Unknown Country",
                teamResponse?.team?.founded ?: -1,
                teamResponse?.venue?.name ?: "Unknown Venue",
                teamResponse?.venue?.city ?: "Unknown City",
                teamResponse?.venue?.capacity ?: -1,
                coachResponse?.name ?: "Unknown Coach",
                coachResponse?.age ?: -1

            )
        }
    }
}

data class TeamPlayer(
    val name: String,
    val age: Int,
    val nationality: String,
    val position: String
) {
    companion object {
        fun fromResponse(squadResponse: SquadResponse): List<TeamPlayer> =
            squadResponse.players.map {
                TeamPlayer(
                    it.name,
                    it.age ?: -1,
                    it.nationality ?: "Unknown Nationality",
                    it.position ?: "Unknown Position"
                )
            }
    }
}

data class TeamTrophy(
    val league: String,
    val country: String,
    val season: String,
    val place: String
) {
    companion object {
        fun fromResponse(trophies: List<TrophyResponse>): List<TeamTrophy> =
            trophies.map {
                TeamTrophy(
                    it.league,
                    it.country,
                    it.season,
                    it.place
                )
            }
    }
}