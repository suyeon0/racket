package com.racket.cart.api.request

import com.racket.cart.api.vo.DeliveryInformationVO

data class CartCreateRequestCommand(
    val userId: Long,
    val productId: Long,
    val optionId: Long,
    val optionName: String,
    val originalPrice: Long,
    val calculatedPrice: Long,
    val orderQuantity: Long,
    val deliveryInformation: DeliveryInformationVO
) {

    fun validate() {
        if (orderQuantity < 0 ) {
            throw IllegalArgumentException("quantity is must be greater than zero.")
        }
    }

}



