package com.racket.api.delivery.domain

data class OptionDeliveryInformation (

    val id: Long,

    val optionId: Long,

    val deliveryDays: Long,

    val deliveryCost: Long
) {
    init {
        require(deliveryDays >= 0) { "deliveryDays must be greater than or equal to zero" }
        require(deliveryCost >= 0) { "deliveryCost must be greater than or equal to zero" }
    }
}