package com.racket.delivery

import com.racket.delivery.response.DeliveryResponseView

interface DeliveryService {

    fun getDeliveryDaysByOption(optionId: Long): DeliveryResponseView

}