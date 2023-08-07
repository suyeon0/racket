package com.racket.api.cache.exception

class NotExistSavedChargeWay: RuntimeException() {
    override val message: String = "The charging way must be saved"
}