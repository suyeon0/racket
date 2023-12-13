package com.racket.api.delivery.adapter.`in`.rest.response

data class OptionDeliveryInformationResponseView (
    private val optionId: Long,
    private val deliveryCost: Long,
    private val deliveryDays: Long
)