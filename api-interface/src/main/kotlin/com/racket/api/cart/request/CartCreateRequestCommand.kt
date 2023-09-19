package com.racket.api.cart.request

import com.racket.api.cart.vo.DeliveryInformationVO

data class CartCreateRequestCommand(
    val userId: Long,
    val productId: Long,
    val optionId: Long,
    val optionName: String,
    val price: Long,
    val orderQuantity: Long,
    val deliveryInformation: DeliveryInformationVO
) {

    fun validate() {
        if (orderQuantity < 0 ) {
            throw IllegalArgumentException("quantity is must be greater than zero.")
        }
    }

}



