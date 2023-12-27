package com.racket.consumer.service.component

import com.racket.api.cash.request.CashChargeCommand
import com.racket.api.cash.response.CashTransactionResponseView
import com.racket.api.payment.presentation.PaymentErrorCodeConstants
import com.racket.consumer.client.CashFeignClient
import com.racket.consumer.exception.RetryCallRequiredException
import com.racket.consumer.service.FailedEventService
import com.racket.consumer.service.PaymentCallService
import com.racket.share.domain.cash.enums.CashTransactionStatusType
import com.racket.share.domain.cash.exception.*
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class CashRetryComponent(
    private val cashClient: CashFeignClient,
    private val paymentCallService: PaymentCallService,
    private val failedEventService: FailedEventService
) {
    private val paymentRequestSuccessCode = HttpStatus.OK.value()
    private val paymentRequestRetryCode = PaymentErrorCodeConstants.RETRY_REQUIRED

    fun handle(@Payload message: String) {
        try {
            val requestTransactionData = this.getRequestTransactionData(transactionId = message)
            this.retryWithMaxAttempts(maxRetries = 2) {
                // 결제 api 호출
                val response = this.paymentCallService.call(
                    accountId = requestTransactionData.accountId,
                    amount = requestTransactionData.amount
                )
                when (response.code) {
                    this.paymentRequestSuccessCode -> {
                        try {
                            this.cashClient.postCompletedTransaction(
                                CashChargeCommand.Success(
                                    userId = requestTransactionData.userId,
                                    amount = requestTransactionData.amount,
                                    transactionId = requestTransactionData.transactionId,
                                    accountId = requestTransactionData.accountId,
                                    eventType = requestTransactionData.eventType
                                )
                            )
                        } catch (e: ChargingProcessingException) {
                            this.cashClient.postFailedTransaction(
                                CashChargeCommand.Failure(
                                    transactionId = requestTransactionData.transactionId,
                                    userId = requestTransactionData.userId,
                                    amount = requestTransactionData.amount,
                                    accountId = requestTransactionData.accountId,
                                    eventType = requestTransactionData.eventType
                                )
                            )
                        }
                        return@retryWithMaxAttempts // 루프 종료
                    }
                    this.paymentRequestRetryCode -> throw RetryCallRequiredException()
                    else -> throw ChargePayException("Payment Api Call Failed. - ${response.message}")
                }
            }
        } catch (e: Exception) {
        }
    }

    private fun getRequestTransactionData(transactionId: String): CashTransactionResponseView {
        val transactionDataList = try {
            cashClient.getTransactionList(transactionId = transactionId).body as ArrayList<CashTransactionResponseView>
        } catch (e: Exception) {
            log.error { e.message }
            throw CashApiCallException()
        }

        if (transactionDataList.any { it.status == CashTransactionStatusType.COMPLETED }) {
            throw InvalidChargingTransactionException("status is invalid")
        }

        return transactionDataList
            .firstOrNull { it.status == CashTransactionStatusType.REQUEST }
            ?: throw NotFoundCashTransactionException()
    }

    private inline fun retryWithMaxAttempts(maxRetries: Int, action: () -> Unit) {
        var retryCount = 0
        while (retryCount <= maxRetries) {
            try {
                action()
                break
            } catch (e: Exception) {
                if (e is RetryCallRequiredException && (retryCount < maxRetries)) {
                    retryCount++
                    log.info { "Retry Attempt: $retryCount" }
                } else {
                    throw e
                }
            }
        }
    }

    private val log = KotlinLogging.logger { }

}