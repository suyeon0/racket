package com.racket.delivery

import com.racket.delivery.response.DeliveryResponseView

interface DeliveryService {

    fun get(optionId: Long): DeliveryResponseView

}