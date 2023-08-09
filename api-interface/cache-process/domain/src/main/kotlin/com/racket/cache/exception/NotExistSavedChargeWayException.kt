package com.racket.cache.exception

class NotExistSavedChargeWayException: RuntimeException() {
    override val message: String = "The charging way must be saved"
}