package com.racket.api.auth.login.request

data class LoginRequestCommand(
    val email: String,
    val password: String
)