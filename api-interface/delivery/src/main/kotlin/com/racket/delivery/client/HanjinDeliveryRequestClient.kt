package com.racket.delivery.client

import com.racket.delivery.client.feign.HanjinFeignClient
import com.racket.delivery.vo.DeliveryResponseVO

class HanjinDeliveryRequestClient(

    private val client: HanjinFeignClient

): DeliveryRequestClient {
    override fun call(): DeliveryResponseVO {
        TODO("Not yet implemented")
    }
}