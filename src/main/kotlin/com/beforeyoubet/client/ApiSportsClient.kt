package com.beforeyoubet.client

import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class ApiSportsClient(private val restClient: RestClient) {

    fun fethTodayMatches(uri: String): List<Map<String, Any>> {

        val response = restClient.get()
            .uri(uri)
            .retrieve()
            .body(Map::class.java) as Map<*, *>

        val responseData = response["response"] as? List<*> ?: return emptyList()
        return responseData.filterIsInstance<Map<String, Any>>()
    }
}