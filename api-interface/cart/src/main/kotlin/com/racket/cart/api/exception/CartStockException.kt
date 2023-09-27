package com.racket.cart.api.exception

class CartStockException(val optionId: Long): RuntimeException() {
    override val message: String = "out of stock. option Id :${optionId}"
}