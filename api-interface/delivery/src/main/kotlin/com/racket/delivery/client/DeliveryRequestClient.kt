package com.racket.delivery.client

import com.racket.delivery.vo.DeliveryResponseVO

interface DeliveryRequestClient {

    fun call(): DeliveryResponseVO
}