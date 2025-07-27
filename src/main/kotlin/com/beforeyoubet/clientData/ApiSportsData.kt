package com.beforeyoubet.clientData

data class Venue(
    val name: String? = null,
    val city: String? = null
)

data class MatchStatus(
    val long: String? = null,
    val short: String? = null,
    val elapsed: Int? = null,
    val extra: Int? = null
)

data class League(
    val name: String? = null,
    val country: String? = null,
    val logo: String? = null,
    val flag: String? = null,
    val season: Int? = null,
    val round: String? = null,
)

data class Team(
    val name: String? = null,
    val logo: String? = null,
    val winner: Boolean? = null,
    val goals: Int? = null,
)