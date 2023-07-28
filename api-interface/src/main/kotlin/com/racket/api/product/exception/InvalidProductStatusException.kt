package com.racket.api.product.exception

class InvalidProductStatusException: RuntimeException() {
    override val message: String = "Status of product unable to proceed with the event"
}