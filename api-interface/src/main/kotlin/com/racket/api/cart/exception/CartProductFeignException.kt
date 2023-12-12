package com.racket.api.cart.exception

class CartProductFeignException(private val exceptionMessage: String?) : RuntimeException() {
    override val message: String = "product api call failed. :${exceptionMessage}"
}