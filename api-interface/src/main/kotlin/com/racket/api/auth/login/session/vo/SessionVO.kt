package com.racket.api.auth.login.session.vo

data class SessionVO(
    val userId: Long,
    val name: String,
    val email: String,
    val role: String
)
