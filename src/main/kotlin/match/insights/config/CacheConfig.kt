package match.insights.config

import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import java.time.Duration

@Configuration
class CacheConfig {

    @Bean
    fun cacheManager(redisConnectionFactory: LettuceConnectionFactory): CacheManager {
        val serializer = JdkSerializationRedisSerializer()

        val defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(serializer)
            ).entryTtl(Duration.ofMinutes(10))


        val cacheConfigs: Map<String, RedisCacheConfiguration> = mapOf(
            "fetchMatches" to defaultConfig.entryTtl(Duration.ofMinutes(15)),
            "fetchLeagueStandings" to defaultConfig.entryTtl(Duration.ofMinutes(30)),
            "fetchFixtureOdds" to defaultConfig.entryTtl(Duration.ofMinutes(15)),
            "fetchMatchDetails" to defaultConfig.entryTtl(Duration.ofMinutes(15)),
            "fetchMatchEvents" to defaultConfig.entryTtl(Duration.ofMinutes(30)),
            "fetchTeamDetails" to defaultConfig.entryTtl(Duration.ofMinutes(30)),
            "fetchCoachDetails" to defaultConfig.entryTtl(Duration.ofMinutes(30)),
            "fetchSquad" to defaultConfig.entryTtl(Duration.ofMinutes(30)),
            "fetchAllLeagues" to defaultConfig.entryTtl(Duration.ofMinutes(120))
        )

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(defaultConfig)
            .withInitialCacheConfigurations(cacheConfigs)
            .build()
    }
}
