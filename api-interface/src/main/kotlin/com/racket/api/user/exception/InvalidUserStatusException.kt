package com.racket.api.user.exception

class InvalidUserStatusException: RuntimeException() {
    override val message: String = "Status of users unable to proceed with the event"
}