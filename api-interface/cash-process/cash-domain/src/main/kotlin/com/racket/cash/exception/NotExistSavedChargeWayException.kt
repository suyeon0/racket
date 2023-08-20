package com.racket.cash.exception

class NotExistSavedChargeWayException: RuntimeException() {
    override val message: String = "The charging way must be saved"
}