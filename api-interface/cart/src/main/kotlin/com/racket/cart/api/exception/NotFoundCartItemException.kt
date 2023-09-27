package com.racket.cart.api.exception

class NotFoundCartItemException(private val cartItemId: Long): RuntimeException() {
    override val message: String = "Cart Item Not Found. CartItemId :${cartItemId}"
}