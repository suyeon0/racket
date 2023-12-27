package com.racket.consumer.service.component

import com.racket.api.cash.request.CashChargeCommand
import com.racket.api.cash.response.CashTransactionResponseView
import com.racket.api.payment.presentation.PaymentErrorCodeConstants
import com.racket.consumer.client.CashFeignClient
import com.racket.consumer.exception.RetryCallRequiredException
import com.racket.consumer.service.FailedEventService
import com.racket.consumer.service.PaymentCallService
import com.racket.share.domain.cash.enums.CashEventType
import com.racket.share.domain.cash.enums.CashTransactionStatusType
import com.racket.share.domain.cash.exception.*
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class CashRetryComponent(
    private val cashClient: CashFeignClient,
    private val paymentCallService: PaymentCallService,
    private val failedEventService: FailedEventService
) {
    private val paymentRequestSuccessCode = HttpStatus.OK.value()
    private val paymentRequestRetryCode = PaymentErrorCodeConstants.RETRY_REQUIRED

    fun handle(@Payload message: String, topic: String, key: String) {
        val originEventTimestamp = Instant.now()
        try {
            val requestTransactionData = this.getRequestTransactionData(transactionId = message)
            this.retryPaymentCall(requestTransactionData)
        } catch (e: Exception) {
            this.failedEventService.register(
                originEventTimestamp = originEventTimestamp,
                topic = topic,
                key = key,
                payload = message
            )
        }
    }

    private fun retryPaymentCall(requestTransactionData: CashTransactionResponseView, retryCount: Int = 2) {
        this.retryWithMaxAttempts(maxRetries = retryCount) {
            val response = paymentCallService.call(
                accountId = requestTransactionData.accountId,
                amount = requestTransactionData.amount
            )
            when (response.code) {
                paymentRequestSuccessCode -> this.handlePaymentSuccess(requestTransactionData)
                paymentRequestRetryCode -> throw RetryCallRequiredException()
                else -> throw ChargePayException("Payment Api Call Failed. - ${response.message}")
            }
        }
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

    private fun handlePaymentSuccess(requestTransactionData: CashTransactionResponseView) {
        try {
            cashClient.postCompletedTransaction(
                CashChargeCommand.Success(
                    userId = requestTransactionData.userId,
                    amount = requestTransactionData.amount,
                    transactionId = requestTransactionData.transactionId,
                    accountId = requestTransactionData.accountId,
                    eventType = requestTransactionData.eventType
                )
            )
        } catch (e: ChargingProcessingException) {
            cashClient.postFailedTransaction(
                CashChargeCommand.Failure(
                    transactionId = requestTransactionData.transactionId,
                    userId = requestTransactionData.userId,
                    amount = requestTransactionData.amount,
                    accountId = requestTransactionData.accountId,
                    eventType = requestTransactionData.eventType
                )
            )
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

    private val log = KotlinLogging.logger { }

}