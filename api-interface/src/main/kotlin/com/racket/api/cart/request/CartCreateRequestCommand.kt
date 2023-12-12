package com.racket.api.cart.request

data class CartCreateRequestCommand(
    val userId: Long?,
    val productId: String?,
    val optionId: String?,
    val originalPrice: Long?,
    val calculatedPrice: Long?,
    val orderQuantity: Long?
) {

    fun validate() {
        if (orderQuantity == null || orderQuantity <= 0 ) {
            throw IllegalArgumentException("quantity is must be greater than or equal to zero.")
        }
        if (originalPrice == null || originalPrice < 0 ) {
            throw IllegalArgumentException("originalPrice is must be greater than zero.")
        }
        if (calculatedPrice == null || calculatedPrice < 0 ) {
            throw IllegalArgumentException("originalPrice is must be greater than zero.")
        }
    }

}



