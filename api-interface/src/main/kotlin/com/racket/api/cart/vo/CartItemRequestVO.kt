package com.racket.api.cart.vo

data class CartItemRequestVO(

    val productId: Long,
    val optionId: Long,
    val optionName: String,
    val price: Long,
    val orderQuantity: Int,
    val deliveryInformation: DeliveryInformationVO

)