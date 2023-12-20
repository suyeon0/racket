package com.racket.consumer.service

import com.racket.api.payment.presentation.response.PaymentApiResponse

interface PaymentCallService {
    fun call(accountId: Long, amount: Long): PaymentApiResponse
}