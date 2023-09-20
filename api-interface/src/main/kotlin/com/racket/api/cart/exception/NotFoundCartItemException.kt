package com.racket.api.cart.exception

class NotFoundCartItemException(private val cartItemId: Long): RuntimeException() {
    override val message: String = "Cart Item Not Found. CartItemId :${cartItemId}"
}