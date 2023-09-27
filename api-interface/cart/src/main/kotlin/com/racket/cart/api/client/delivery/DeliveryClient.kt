package com.racket.cart.api.client.delivery

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "delivery", url = "\${client.serviceUrl.delivery}", contextId = "cart-delivery")
interface DeliveryClient {

    @GetMapping("/{optionId}")
    fun get(@PathVariable optionId: Long): ResponseEntity<DeliveryResponseView>?
}