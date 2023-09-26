package com.racket.cart.api.exception

class CartProductFeignException(private val exceptionMessage: String?) : RuntimeException() {
    override val message: String = "product api call failed. :${exceptionMessage}"
}