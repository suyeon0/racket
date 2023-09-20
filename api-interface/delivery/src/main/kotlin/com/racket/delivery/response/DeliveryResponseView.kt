package com.racket.delivery.response

import java.time.LocalDate

data class DeliveryResponseView (

    val deliveryCost: Long,
    val expectedDate: LocalDate

)