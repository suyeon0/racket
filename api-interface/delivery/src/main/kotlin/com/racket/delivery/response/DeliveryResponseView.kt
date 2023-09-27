package com.racket.delivery.response

import java.time.LocalDate

data class DeliveryResponseView (

    val statusCode: Long,
    val statusMessage: String,

    val optionId: Long,
    val deliveryCost: Long,
    val deliveryDays: Long

)