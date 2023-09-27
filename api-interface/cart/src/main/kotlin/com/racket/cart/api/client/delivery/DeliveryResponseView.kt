package com.racket.cart.api.client.delivery

import java.time.LocalDate

data class DeliveryResponseView (

    val statusCode: Long,
    val statusMessage: String,

    val deliveryCost: Long,
    val estimatedDeliveryDay: Long

)