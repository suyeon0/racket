package com.racket.api.cart.vo

data class CartItemRequestVO(

    val userId: Long,
    val productId: Long,
    val optionId: Long,
    val optionName: String,
    val originalPrice: Long,
    val calculatedPrice: Long,
    val orderQuantity: Long,
    val deliveryInformation: DeliveryInformationVO

)