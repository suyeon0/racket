package com.racket.api.user.exception

class InvalidUserStatusException: Exception() {
    override val message: String = "Status of users unable to proceed with the event"
}