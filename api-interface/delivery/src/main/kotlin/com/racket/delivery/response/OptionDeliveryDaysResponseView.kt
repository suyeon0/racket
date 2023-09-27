package com.racket.delivery.response

data class OptionDeliveryDaysResponseView (

    val statusCode: Long,
    val statusMessage: String,

    val optionId: Long,
    val deliveryCost: Long,
    val deliveryDays: Long

)