package com.racket.cart.api.vo

data class CartItemVO (

    val cartId: Long,

    val productId: Long,

    val optionId: Long,

    val productName: String?,

    val optionName: String?,

    val originalPrice: Long,

    val calculatedPrice: Long,

    var orderQuantity: Long

)