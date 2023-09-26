package com.racket.cart.api.request

data class CartUpdateRequestCommand(
    val orderQuantity: Long
) {

    fun validate() {
        if (orderQuantity < 0 ) {
            throw IllegalArgumentException("quantity is must be greater than zero.")
        }
    }

}



