package com.racket.api.cart.client

import org.springframework.cloud.openfeign.FeignClient

@FeignClient(name = "delivery", url = "\${client.serviceUrl.delivery}", contextId = "cart-delivery")
interface DeliveryClient {
}