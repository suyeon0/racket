package com.racket.api.cart.vo

data class CartItemRequestVO(

    val userId: Long,
    val productId: Long,
    val optionId: Long,
    val optionName: String,
    val price: Long,
    val orderQuantity: Long,
    val deliveryInformation: DeliveryInformationVO

)