package com.racket.consumer.client

import com.racket.api.payment.presentation.response.PaymentApiResponse
import com.racket.consumer.service.PaymentCallServiceImpl
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "payment", url = "\${client.serviceUrl.payment}", contextId = "cache-consumer-payment")
interface PaymentFeignClient {

    @PostMapping("/account")
    fun accountPay(accountRequestDTO: PaymentCallServiceImpl.AccountRequestDTO): ResponseEntity<PaymentApiResponse>
}