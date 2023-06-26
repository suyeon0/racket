package com.racket.api.auth.login.session.domain

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.io.Serializable
import java.time.LocalDateTime

@RedisHash(value = "session", timeToLive = 60)
data class SessionUser(
    @Id
    val sessionId: String,
    val expireTime: LocalDateTime,
    val name: String,
    val email: String,
    val role: String,
    val userId: Long
): Serializable
