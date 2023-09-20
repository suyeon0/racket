package com.racket.api.cart.vo

import java.time.LocalDate

data class DeliveryInformationVO (
    val deliveryCost: Long,
    val expectedDate : LocalDate?
)