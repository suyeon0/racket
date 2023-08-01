package com.racket.api.auth.login

import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import redis.embedded.RedisServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@TestConfiguration
@EnableRedisRepositories
class EmbeddedRedisConfig(private val redisProperties: RedisProperties) {

    private val redisServer: RedisServer = RedisServer(redisProperties.port)

    @PostConstruct
    fun postConstruct() {
        redisServer.start()
    }

    @PreDestroy
    fun preDestroy() {
        redisServer.stop()
    }
}