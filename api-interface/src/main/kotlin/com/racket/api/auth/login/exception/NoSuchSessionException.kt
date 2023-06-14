package com.racket.api.auth.login.exception

class NoSuchSessionException : RuntimeException() {
    override val message: String = "session is not exist or invalid"
}