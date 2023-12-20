package com.racket.consumer.service.consumer

import com.racket.api.cash.request.CashChargeCommand
import com.racket.api.payment.presentation.PaymentErrorCodeConstants
import com.racket.consumer.client.CashFeignClient
import com.racket.consumer.domain.dlq.DeadLetterEntity
import com.racket.consumer.domain.dlq.DeadLetterRepository
import com.racket.consumer.enums.DeadLetterType
import com.racket.consumer.enums.EventType
import com.racket.consumer.exception.RetryCallRequiredException
import com.racket.consumer.service.PaymentCallService
import com.racket.consumer.vo.DeadLetterQueueVO
import com.racket.share.domain.cash.enums.CashTransactionStatusType
import com.racket.share.domain.cash.exception.ChargePayException
import com.racket.share.domain.cash.exception.ChargingProcessingException
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.Consumer
import org.springframework.http.HttpStatus
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class DeadLetterEventConsumer(
    private val deadLetterRepository: DeadLetterRepository,
    private val cashEventConsumer: CashEventConsumer,
    private val paymentCallService: PaymentCallService,
    private val cashClient: CashFeignClient
) {

    private val log = KotlinLogging.logger { }
    private val paymentRequestSuccessCode = HttpStatus.OK.value()
    private val paymentRequestRetryCode = PaymentErrorCodeConstants.RETRY_REQUIRED

    @KafkaListener(
        topics = ["dlq"], groupId = "racket", containerFactory = "kafkaDLQListenerContainerFactory"
    )
    fun consumeDeadLetterEvent(
        @Payload deadLetter: DeadLetterQueueVO,
        consumer: Consumer<String, DeadLetterQueueVO>
    ) {
        try {
            when(deadLetter.deadLetterType) {
                DeadLetterType.INSERT_DB -> {
                    val deadLetterEntity = DeadLetterEntity(
                        failureTopic = deadLetter.failureTopic,
                        key = deadLetter.key,
                        payload = deadLetter.payload,
                        createdAt = LocalDateTime.now(),
                        isProcessed = false
                    )
                    val savedEntity = this.deadLetterRepository.save(deadLetterEntity)
                    log.info { "DeadLetterEntity saved with ID: ${savedEntity.id}, event: $deadLetter" }
                }
                DeadLetterType.RETRY -> {
                    when(deadLetter.eventType) {
                        EventType.CASH -> {
                            try {
                                this.retryWithMaxAttempts(maxRetries = 2) {
                                    val requestTransactionData = this.cashEventConsumer.getRequestTransactionData(transactionId = deadLetter.payload)
                                    val response = this.paymentCallService.call(
                                        accountId = requestTransactionData.accountId,
                                        amount = requestTransactionData.amount
                                    )
                                    when (response.code) {
                                        this.paymentRequestRetryCode -> throw RetryCallRequiredException()
                                        this.paymentRequestSuccessCode -> {
                                            try {
                                                this.cashEventConsumer.callCashApiToSaveData(requestTransactionData)
                                            } catch (e: ChargingProcessingException) {
                                                this.cashClient.postTransaction(
                                                    CashChargeCommand(
                                                        status = CashTransactionStatusType.FAILED,
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

                                        else -> throw ChargePayException("Payment Api Call Failed. - ${response.message}")
                                    }
                                }
                            } catch (e: Exception) {
                                throw e
                            }
                        }
                        EventType.DELIVERY -> {

                        }
                    }
                }
            }
        } catch (e: Exception) {
            log.error("Error processing dead letter event: $deadLetter", e)
            throw e
        } finally {
            consumer.commitAsync()
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


}