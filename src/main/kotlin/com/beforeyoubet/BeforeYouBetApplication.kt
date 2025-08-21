package com.beforeyoubet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableCaching
class BeforeYouBetApplication

fun main(args: Array<String>) {
    runApplication<BeforeYouBetApplication>(*args)
}
