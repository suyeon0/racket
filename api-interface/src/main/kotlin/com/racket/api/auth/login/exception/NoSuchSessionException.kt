package com.racket.api.auth.login.exception

class NoSuchSessionException : RuntimeException() {
    override val message: String = "Not found session, Please Login again."
}