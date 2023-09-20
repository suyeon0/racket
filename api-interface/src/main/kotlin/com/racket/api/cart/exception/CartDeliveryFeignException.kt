package com.racket.api.cart.exception

class CartDeliveryFeignException(private val exceptionMessage: String?) : RuntimeException() {
    override val message: String = "delivery api call failed. :${exceptionMessage}"
}