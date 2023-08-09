package com.racket.cash.consume.client

import com.racket.api.shared.request.MessageRequest
import org.bson.types.ObjectId
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "payment", url = "\${client.serviceUrl.payment}", contextId = "cache-consumer-payment")
interface PaymentFeignClient {

    @PostMapping
    fun pay(price: Long): String
}