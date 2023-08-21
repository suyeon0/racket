package com.racket.share.domain.user.exception

class DuplicateUserException : RuntimeException() {
    override val message: String = "Duplicate user email"
}