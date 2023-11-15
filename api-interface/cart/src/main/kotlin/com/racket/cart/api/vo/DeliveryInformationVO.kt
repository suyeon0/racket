package com.racket.cart.api.vo

import java.time.LocalDate

data class DeliveryInformationVO (
    val deliveryCost: Long,
    val expectedDate : LocalDate?
)