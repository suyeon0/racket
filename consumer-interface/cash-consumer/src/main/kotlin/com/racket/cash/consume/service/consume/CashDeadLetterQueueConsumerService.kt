package com.racket.cash.consume.service.consume

import com.racket.api.payment.presentation.PaymentErrorCodeConstants
import com.racket.api.payment.presentation.RetryPaymentCallRequiredException
import com.racket.cash.consume.client.CashFeignClient
import com.racket.cash.consume.const.DeadLetterType
import com.racket.cash.consume.domain.CashFailLog
import com.racket.cash.consume.domain.CashFailLogRepository
import com.racket.cash.consume.service.PaymentCallService
import com.racket.cash.consume.vo.DeadLetterQueueVO
import com.racket.cash.entity.CashTransaction
import com.racket.cash.enums.CashTransactionStatusType
import com.racket.cash.exception.*
import com.racket.cash.repository.CashTransactionRepository
import com.racket.cash.request.CashChargeCommand
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.Consumer
import org.springframework.http.HttpStatus
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CashDeadLetterQueueConsumerService(
    private val cashConsumerService: CashConsumerService,
    private val cashFailLogRepository: CashFailLogRepository,
    private val cashTransactionRepository: CashTransactionRepository,
    private val paymentCallService: PaymentCallService
) {

    private val log = KotlinLogging.logger { }

    private val paymentRequestRetryCode = PaymentErrorCodeConstants.RETRY_REQUIRED
    private val paymentRequestSuccessCode = HttpStatus.OK.value()

    @KafkaListener(
        topics = [DeadLetterType.INSERT_DB], groupId = "CASH_DLQ", containerFactory = "kafkaDLQListenerContainerFactory"
    )
    @Transactional
    fun consumeInsertDBProcess(
        @Payload value: DeadLetterQueueVO,
        consumer: Consumer<String, DeadLetterQueueVO>
    ) {
        val failTransactionId = value.payload.toString()
        this.cashFailLogRepository.save(
            CashFailLog(
                payload = failTransactionId,
                errorMessage = value.errorMessage!!
            )
        )

        val requestCashTransaction = this.cashTransactionRepository.findAllByTransactionId(transactionId = failTransactionId)[0]
        this.cashTransactionRepository.save(
            CashTransaction(
                transactionId = failTransactionId.toString(),
                amount = requestCashTransaction.amount,
                userId = requestCashTransaction.userId,
                eventType = requestCashTransaction.eventType,
                accountId = requestCashTransaction.accountId,
                status = CashTransactionStatusType.FAILED
            )
        )
        consumer.commitSync()
        log.info { "cash dlq consume insert DB Process done" }
    }

    @KafkaListener(
        topics = [DeadLetterType.RETRY], groupId = "CASH_DLQ", containerFactory = "kafkaDLQListenerContainerFactory"
    )
    fun consumeRetryProcess(
        @Payload value: DeadLetterQueueVO,
        consumer: Consumer<String, DeadLetterQueueVO>
    ) {
        try {
            this.retryWithMaxAttempts(maxRetries = 2) {
                val requestTransactionData = this.cashConsumerService.getRequestTransactionData(transactionId = value.payload.toString())
                val response = this.paymentCallService.call(
                    accountId = requestTransactionData.accountId,
                    amount = requestTransactionData.amount
                )
                when (response.code) {
                    this.paymentRequestRetryCode -> throw RetryPaymentCallRequiredException()
                    this.paymentRequestSuccessCode -> {
                        this.cashConsumerService.callCashApiToSaveData(requestTransactionData)
                        return@retryWithMaxAttempts // 루프 종료
                    }

                    else -> throw ChargePayException("Payment Api Call Failed. - ${response.message}")
                }
            }
        } catch (e: Exception) {
            // 최대 재시도 횟수에 도달하거나, 실패한 경우
            this.cashConsumerService.publishCashDeadLetterQueue(
                topic = DeadLetterType.INSERT_DB,
                value = DeadLetterQueueVO(
                    topic = "CASH_DLQ",
                    key = null,
                    payload = value.payload,
                    errorMessage = e.message ?: "Exception: ${e::class.simpleName}"
                )
            )
        }
        consumer.commitSync()

    }

    private inline fun retryWithMaxAttempts(maxRetries: Int, action: () -> Unit) {
        var retryCount = 0

        while (retryCount <= maxRetries) {
            try {
                action()
                break
            } catch (e: Exception) {
                if (e is RetryPaymentCallRequiredException && (retryCount < maxRetries)) {
                    retryCount++
                    log.info { "Retry Attempt: $retryCount" }
                } else {
                    throw e
                }
            }
        }
    }

}

