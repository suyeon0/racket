package com.racket.delivery.response

import java.time.LocalDate

data class DeliveryResponseView (

    val statusCode: Long?,
    val statusMessage: String?,

    val deliveryCost: Long?,
    val expectedDate: LocalDate?

)