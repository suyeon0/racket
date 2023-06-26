package com.racket.api.user.exception

class NotFoundUserException : RuntimeException() {
    override val message: String = "No matching user found"
}