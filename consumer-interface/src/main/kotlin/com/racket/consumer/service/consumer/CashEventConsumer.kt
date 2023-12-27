package com.racket.consumer.service.consumer

import com.racket.api.payment.presentation.PaymentErrorCodeConstants
import com.racket.api.cash.request.CashChargeCommand
import com.racket.api.cash.response.CashTransactionResponseView
import com.racket.consumer.client.CashFeignClient
import com.racket.consumer.enums.DeadLetterType
import com.racket.consumer.enums.EventType
import com.racket.consumer.exception.RetryCallRequiredException
import com.racket.consumer.service.PaymentCallService
import com.racket.consumer.vo.DeadLetterQueueVO
import com.racket.share.domain.cash.enums.CashTransactionStatusType
import com.racket.share.domain.cash.exception.*
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.http.HttpStatus
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.util.concurrent.ListenableFutureCallback
import java.time.Instant

@Service
class CashEventConsumer(
    private val cashClient: CashFeignClient,
    private val paymentCallService: PaymentCallService,
    private val deadLetterQueueKafkaProduceTemplate: KafkaTemplate<String, DeadLetterQueueVO>
) {

    private val log = KotlinLogging.logger { }
    private val paymentRequestSuccessCode = HttpStatus.OK.value()
    private val paymentRequestRetryCode = PaymentErrorCodeConstants.RETRY_REQUIRED

    @KafkaListener(
        topics = ["cash"], groupId = "racket"
    )
    fun consumeChargingProcess(
        @Payload message: String,
        consumer: Consumer<String, String>,
        record: ConsumerRecord<String, String>
    ) {
        try {
            val requestTransactionData = this.getRequestTransactionData(transactionId = message)
            val response = this.paymentCallService.call(accountId = requestTransactionData.accountId, amount = requestTransactionData.amount)
            when (response.code) {
                this.paymentRequestRetryCode -> throw RetryCallRequiredException()
                this.paymentRequestSuccessCode -> {
                    try {
                        this.callCashApiToSaveData(requestTransactionData)
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
                }
                else -> throw ChargePayException("Payment Api Call Failed. - ${response.message}")
            }
        } catch (e: Exception) {
            val deadLetterType = when (e) {
                is RetryCallRequiredException -> DeadLetterType.RETRY
                else -> DeadLetterType.INSERT_DB
            }

            this.publishCashDeadLetterQueue(
                value = DeadLetterQueueVO(
                    originEventTimestamp = Instant.ofEpochMilli(record.timestamp()),
                    failureTopic = "cash",
                    eventType = EventType.CASH,
                    deadLetterType = deadLetterType,
                    key = message,
                    payload = message,
                    errorMessage = e.message ?: "Exception: ${e::class.simpleName}"
                )
            )
        }
        consumer.commitSync()
        log.info { "cash consume offset commit!" }
    }

    fun getRequestTransactionData(transactionId: String): CashTransactionResponseView {
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

    // 충전 결과 DB 반영을 위한 Cash Api 호출
    fun callCashApiToSaveData(chargeRequestTransactionData: CashTransactionResponseView) =
        this.cashClient.postCompletedTransaction(
            CashChargeCommand.Success(
                userId = chargeRequestTransactionData.userId,
                amount = chargeRequestTransactionData.amount,
                transactionId = chargeRequestTransactionData.transactionId,
                accountId = chargeRequestTransactionData.accountId,
                eventType = chargeRequestTransactionData.eventType
            )
        )

    fun publishCashDeadLetterQueue(value: DeadLetterQueueVO) {
        val listenableFuture: ListenableFuture<SendResult<String, DeadLetterQueueVO>> =
            this.deadLetterQueueKafkaProduceTemplate.send("dlq", value)
        listenableFuture.addCallback(object : ListenableFutureCallback<SendResult<String, DeadLetterQueueVO>> {
            override fun onFailure(e: Throwable) {
                log.error { "ERROR Kafka error happened-${e}" }
            }

            override fun onSuccess(result: SendResult<String, DeadLetterQueueVO>?) {
                log.info {
                    "Produce Send Result : SUCCESS! " +
                            " Message ::${result}"
                }
            }
        })
    }
}
