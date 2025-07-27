package com.beforeyoubet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class BeforeYouBetApplication

fun main(args: Array<String>) {
	runApplication<BeforeYouBetApplication>(*args)
}
