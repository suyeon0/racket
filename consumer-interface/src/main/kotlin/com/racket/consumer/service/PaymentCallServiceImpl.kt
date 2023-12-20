package com.racket.consumer.service

import com.racket.consumer.client.PaymentFeignClient
import com.racket.share.domain.cash.exception.ChargePayException
import org.springframework.stereotype.Service

@Service
class PaymentCallServiceImpl(
    private val paymentClient: PaymentFeignClient
) : PaymentCallService {

    override fun call(accountId: Long, amount: Long) =
        this.paymentClient.accountPay(AccountRequestDTO(accountId, amount)).body
            ?: throw ChargePayException("Payment Api Call Failed.")

    data class AccountRequestDTO(
        val accountId: Long,
        val amount: Long
    )
}