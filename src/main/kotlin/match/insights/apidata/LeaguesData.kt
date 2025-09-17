package match.insights.apidata

import match.insights.client.ApiSportsClient
import match.insights.clientData.LeagueAndCountry
import match.insights.clientData.LeagueWithStandings
import match.insights.props.SeasonProps
import org.springframework.stereotype.Component

@Component
class LeaguesData(
    private val apiSportsClient: ApiSportsClient,
    private val seasonProps: SeasonProps
) {

    fun leagues(): List<LeagueAndCountry> = apiSportsClient.fetchAllLeagues("/leagues")


    fun leagueStandings(leagueId: Int): LeagueWithStandings? =
        apiSportsClient.fetchLeagueInfo("/standings?league=$leagueId&season=${seasonProps.year}")
            ?: apiSportsClient.fetchLeagueInfo("/standings?league=$leagueId&season=${seasonProps.worldCupYear}")

}