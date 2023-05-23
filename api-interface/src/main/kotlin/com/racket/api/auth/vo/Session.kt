package com.racket.api.auth.vo

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class Session(

    @Column(name = "session_id")
    val sessionId: String,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "created_at")
    val createdAt: LocalDateTime,

    @Column(name = "expires_at")
    val expiresAt: LocalDateTime

) {

}