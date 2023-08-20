package com.racket.cash.consume.client

import com.racket.api.payment.presentation.response.PaymentApiResponse
import com.racket.cash.consume.service.PaymentCallService
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "payment", url = "\${client.serviceUrl.payment}", contextId = "cache-consumer-payment")
interface PaymentFeignClient {

    @PostMapping("/account")
    fun accountPay(accountRequestDTO: PaymentCallService.AccountRequestDTO): PaymentApiResponse
}