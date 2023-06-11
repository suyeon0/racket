package com.racket.api.auth.login.request

data class LoginRequest(
    val email: String,
    val password: String
)