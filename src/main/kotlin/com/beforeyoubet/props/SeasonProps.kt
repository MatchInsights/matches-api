package com.beforeyoubet.props

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.LocalDate

@ConfigurationProperties(prefix = "season")
data class SeasonProps(val year: Int? = LocalDate.now().year)