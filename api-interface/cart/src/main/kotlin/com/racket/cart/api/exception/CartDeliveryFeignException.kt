package com.racket.cart.api.exception

class CartDeliveryFeignException(private val exceptionMessage: String?) : RuntimeException() {
    override val message: String = "delivery api call failed. :${exceptionMessage}"
}