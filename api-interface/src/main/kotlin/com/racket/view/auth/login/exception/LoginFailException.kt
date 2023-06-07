package com.racket.view.auth.login.exception

class LoginFailException : RuntimeException() {
    override val message: String = "Login failed"
}