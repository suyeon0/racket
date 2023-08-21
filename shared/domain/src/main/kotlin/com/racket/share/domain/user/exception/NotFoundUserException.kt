package com.racket.share.domain.user.exception

class NotFoundUserException : RuntimeException() {
    override val message: String = "No matching user found"
}