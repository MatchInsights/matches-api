package com.beforeyoubet.datamanipulation


import com.beforeyoubet.data.client.ClientEventsData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EventsDataManipulationTest {
    val underTest = EventsDataManipulation()

    @Test
    fun shouldSumLastFiveMatchesEvents() {

        val result = underTest.fiveMachesEventsSum(ClientEventsData.mockEvents)

        assertThat(result.penalties).isEqualTo(1)
        assertThat(result.firstHalfGoals).isEqualTo(1)
        assertThat(result.firstHalfYellowCards).isEqualTo(1)
        assertThat(result.secondHalfRedCards).isEqualTo(1)
    }

}