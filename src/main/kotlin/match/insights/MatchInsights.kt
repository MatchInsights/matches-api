package match.insights

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableCaching
class MatchInsights

fun main(args: Array<String>) {
    runApplication<match.insights.MatchInsights>(*args)
}
