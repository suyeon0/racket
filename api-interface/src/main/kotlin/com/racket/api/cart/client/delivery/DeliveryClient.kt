package com.racket.api.cart.client.delivery

import com.racket.api.cart.client.delivery.DeliveryResponseView
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "delivery", url = "\${client.serviceUrl.delivery}", contextId = "cart-delivery")
interface DeliveryClient {

    @GetMapping("/{optionId}")
    fun get(@PathVariable optionId: Long): ResponseEntity<DeliveryResponseView>?
}