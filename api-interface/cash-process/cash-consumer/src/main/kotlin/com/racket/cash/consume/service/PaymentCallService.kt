package com.racket.cash.consume.service

import com.racket.api.payment.presentation.PaymentErrorCodeConstants
import com.racket.api.payment.presentation.RetryPaymentCallRequiredException
import com.racket.api.payment.presentation.response.PaymentApiResponse
import com.racket.cash.consume.client.PaymentFeignClient
import com.racket.cash.exception.ChargePayException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class PaymentCallService(
    private val paymentClient: PaymentFeignClient
) {
    private val log = KotlinLogging.logger { }

    fun call(accountId: Long, amount: Long): PaymentApiResponse {
        val response = this.paymentClient.accountPay(
            AccountRequestDTO(
                accountId = accountId,
                amount = amount
            )
        )
        return when (response.code) {
            HttpStatus.OK.value() -> response
            PaymentErrorCodeConstants.RETRY_REQUIRED -> {
                log.info { "=========== 결제 모듈 연동 실패! - Retry 대상" }
                throw RetryPaymentCallRequiredException()
            }

            else -> {
                log.info { "=========== 결제 모듈 연동 실패! - Retry 대상 아님" }
                throw ChargePayException("")
            }
        }
    }

    data class AccountRequestDTO(
        val accountId: Long,
        val amount: Long
    )
}