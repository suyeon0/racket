package com.racket.cash.consume.client

import com.racket.api.payment.presentation.response.PaymentApiResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "payment", url = "\${client.serviceUrl.payment}", contextId = "cache-consumer-payment")
interface PaymentFeignClient {

    @PostMapping
    fun pay(price: Long): PaymentApiResponse
}