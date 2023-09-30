package com.racket.delivery.client

import com.racket.delivery.client.feign.CJFeignClient
import com.racket.delivery.vo.DeliveryResponseVO

class CJDeliveryRequestClient(
    private val client: CJFeignClient

) : DeliveryRequestClient {
    override fun call(): DeliveryResponseVO {
        TODO("Not yet implemented")
    }

}