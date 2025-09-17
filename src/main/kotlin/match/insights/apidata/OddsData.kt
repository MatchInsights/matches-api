package match.insights.apidata

import match.insights.client.ApiSportsClient
import match.insights.clientData.FixtureOdds
import match.insights.props.SeasonProps
import org.springframework.stereotype.Component

@Component
class OddsData(
    private val apiSportsClient: ApiSportsClient,
    private val seasonProps: SeasonProps
) {

    fun fetchAllOdds(fixtureId: Int): List<FixtureOdds> =
        apiSportsClient.fetchFixtureOdds("/odds?fixture=$fixtureId")

}