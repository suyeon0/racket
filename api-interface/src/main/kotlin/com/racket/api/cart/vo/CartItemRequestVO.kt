package com.racket.api.cart.vo

data class CartItemRequestVO(

    val userId: Long,
    val productId: String,
    val optionId: String,
    val originalPrice: Long,
    val calculatedPrice: Long,
    val orderQuantity: Long

)