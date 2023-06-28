package com.racket.api.product.exception

class NotFoundOptionException: RuntimeException() {
    override val message: String = "No matching option found"
}