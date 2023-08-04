package com.racket.api.auth.login.presentation.request

data class LoginRequestCommand(
    val email: String,
    val password: String
)