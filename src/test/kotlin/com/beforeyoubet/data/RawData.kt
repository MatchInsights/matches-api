package com.beforeyoubet.data

class RawData {

    companion object {
        val todayMatches = """
            {"response":[{"fixture":{"id":1327475,"referee":"Elijio Arreguin","timezone":"UTC","date":"2025-07-27T00:00:00+00:00","timestamp":1753574400,"periods":{"first":1753574400,"second":1753578000},"venue":{"id":20751,"name":"Protective Stadium","city":"Birmingham, Alabama"},"status":{"long":"Match Finished","short":"FT","elapsed":90,"extra":6}},"league":{"id":1095,"name":"USL League One Cup","country":"USA","logo":"https://media.api-sports.io/football/leagues/1095.png","flag":"https://media.api-sports.io/flags/us.svg","season":2025,"round":"Group Stage - 8","standings":false},"teams":{"home":{"id":3989,"name":"Birmingham Legion","logo":"https://media.api-sports.io/football/teams/3989.png","winner":true},"away":{"id":9025,"name":"Forward Madison","logo":"https://media.api-sports.io/football/teams/9025.png","winner":false}},"goals":{"home":2,"away":1},"score":{"halftime":{"home":1,"away":0},"fulltime":{"home":2,"away":1},"extratime":{"home":null,"away":null},"penalty":{"home":null,"away":null}}}]}
        """.trimIndent()

        val leagueStandings = """
        {
          "response": [
            {
              "league": {
                "standings": [
                  [
                    {
                      "rank": 1,
                      "team": {
                        "id": 33,
                        "name": "Manchester United"
                      },
                      "points": 86,
                      "form": "WDLWW"
                    },
                    {
                      "rank": 2,
                      "team": {
                        "id": 34,
                        "name": "Manchester City"
                      },
                      "points": 85,
                      "form": "WWWWW"
                    }
                  ]
                ]
              }
            }
          ]
        }
    """.trimIndent()


        val matchDetails = """
        {
  "response": [
    {
      "fixture": {
        "id": 12345,
        "date": "2023-04-15T18:00:00+00:00",
        "venue": { "name": "Stadium Name", "city": "City" }
      },
      "league": {
        "id": 39,
        "name": "Premier League",
        "season": 2023,
        "logo": "league-logo-url"
      },
      "teams": {
        "home": { "id": 1, "name": "Team A", "logo": "url" },
        "away": { "id": 2, "name": "Team B", "logo": "url" }
      },
      "goals": { "home": 2, "away": 1 },
      "score": {
        "halftime": { "home": 1, "away": 1 },
        "fulltime": { "home": 2, "away": 1 }
      }
    }
  ]
}""".trimIndent()

        val oddsResponse = """
            {
              "response": [
                {
                  "bookmakers": [
                    {
                      "name": "Bet365",
                      "bets": [
                        {
                          "name": "Match Winner",
                          "values": [
                            { "value": "Home", "odd": "1.85" },
                            { "value": "Draw", "odd": "3.50" },
                            { "value": "Away", "odd": "4.20" }
                          ]
                        },
                        {
                          "name": "Odd/Even - First Half",
                          "values": [
                            { "value": "Odd", "odd": "2.00" },
                            { "value": "Even", "odd": "1.85" }
                          ]
                        }
                      ]
                    }
                  ]
                }
              ]
            }

        """.trimIndent()
    }
}