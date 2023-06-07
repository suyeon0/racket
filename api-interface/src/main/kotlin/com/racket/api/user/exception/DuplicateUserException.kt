package com.racket.api.user.exception

class DuplicateUserException : RuntimeException() {
    override val message: String = "Duplicate user email"
}