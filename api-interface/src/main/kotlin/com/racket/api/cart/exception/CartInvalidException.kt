package com.racket.api.cart.exception

class CartInvalidException(private val exceptionMessage: String?) : RuntimeException() {
    override val message: String = "cart invalid. :${exceptionMessage}"
}