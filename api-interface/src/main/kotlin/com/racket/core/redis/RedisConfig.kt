package com.racket.core.redis

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
class RedisConfig(
    @Value("\${spring.redis.host}")
    val host: String,

    @Value("\${spring.redis.port}")
    val port: Int,

    @Value("\${spring.redis.password}")
    val password: String
) {

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val redisStandaloneConfiguration = RedisStandaloneConfiguration()
        redisStandaloneConfiguration.hostName = host
        redisStandaloneConfiguration.port = port
        redisStandaloneConfiguration.setPassword(password)
        return LettuceConnectionFactory(redisStandaloneConfiguration)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, String> {
        return RedisTemplate<String, String>().apply {
            this.setConnectionFactory(redisConnectionFactory())

            this.keySerializer = StringRedisSerializer()
            this.hashKeySerializer = StringRedisSerializer()
            this.valueSerializer = StringRedisSerializer()
        }
    }
}