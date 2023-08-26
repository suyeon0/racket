package com.racket.cash.consume.service

import com.racket.api.payment.presentation.response.PaymentApiResponse
import com.racket.cash.consume.client.PaymentFeignClient
import com.racket.cash.exception.ChargePayException
import org.springframework.stereotype.Service

@Service
class PaymentCallServiceImpl(
    private val paymentClient: PaymentFeignClient
) : PaymentCallService {

    override fun call(accountId: Long, amount: Long): PaymentApiResponse {
        val response = paymentClient.accountPay(AccountRequestDTO(accountId, amount))
        return response.body ?: throw ChargePayException("Payment Api Call Failed.")
    }

    data class AccountRequestDTO(
        val accountId: Long,
        val amount: Long
    )
}