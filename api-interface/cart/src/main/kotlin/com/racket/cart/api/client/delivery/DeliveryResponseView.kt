package com.racket.cart.api.client.delivery

data class DeliveryResponseView (

    val statusCode: Long,
    val statusMessage: String,

    val optionId: Long,
    val deliveryCost: Long,
    val deliveryDays: Long

)