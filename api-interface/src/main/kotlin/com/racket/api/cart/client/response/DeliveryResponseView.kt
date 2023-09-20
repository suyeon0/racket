package com.racket.api.cart.client.response

import java.time.LocalDate

data class DeliveryResponseView (

    val deliveryCost: Long,
    val expectedDate: LocalDate

)