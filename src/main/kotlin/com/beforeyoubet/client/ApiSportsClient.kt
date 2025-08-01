package com.beforeyoubet.client

import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class ApiSportsClient(private val restClient: RestClient) {

    fun fetchTodayMatches(uri: String): List<Map<String, Any>> {

        val response = restClient.get()
            .uri(uri)
            .retrieve()
            .body(Map::class.java) as Map<*, *>

        val responseData = response["response"] as? List<*> ?: return emptyList()
        return responseData.filterIsInstance<Map<String, Any>>()
    }

    fun fetchLeagueStandings(uri: String): List<Map<String, Any>> {

        val response = restClient.get()
            .uri(uri)
            .retrieve()
            .body(Map::class.java) as Map<*, *>

        val data = response["response"] as List<*>

        if(data.isEmpty())
            return emptyList()

        val league = (data[0] as Map<String, Any>)["league"] as Map<String, Any>
        return (league["standings"] as List<*>)[0] as List<Map<String, Any>>

    }
}