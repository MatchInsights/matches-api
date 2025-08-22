package match.insights.model

enum class MatchStatus(val code: String) {
    NOT_STARTED("NS"),
    TIME_TBD("TIME"),
    FIRST_HALF("1H"),
    HALF_TIME("HT"),
    SECOND_HALF("2H"),
    EXTRA_TIME("ET"),
    PENALTIES("P"),
    BREAK_TIME("BT"),
    LIVE("LIVE"),
    INTERRUPTED("INT"),

    FULL_TIME("FT"),
    AFTER_EXTRA_TIME("AET"),
    AFTER_PENALTIES("PEN"),
    CANCELLED("CANC"),
    POSTPONED("PST"),
    ABANDONED("ABD"),
    AWARDED("AWD"),
    WALKOVER("WO"),
    SUSPENDED("SUSP");
}
