package com.beforeyoubet.props

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "season")
data class SeasonProps(val year: Int)