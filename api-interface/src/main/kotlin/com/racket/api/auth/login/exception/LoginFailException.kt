package com.racket.api.auth.login.exception

class LoginFailException : RuntimeException() {
    override val message: String = "Login failed"
}