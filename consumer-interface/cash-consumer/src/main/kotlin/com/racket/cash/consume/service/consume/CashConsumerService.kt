package com.racket.cash.consume.service.consume

import com.racket.api.payment.presentation.PaymentErrorCodeConstants
import com.racket.api.payment.presentation.RetryPaymentCallRequiredException
import com.racket.cash.consume.client.CashFeignClient
import com.racket.cash.consume.const.DeadLetterType
import com.racket.cash.consume.service.PaymentCallService
import com.racket.cash.consume.vo.DeadLetterQueueVO
import com.racket.cash.enums.CashTransactionStatusType
import com.racket.cash.exception.*
import com.racket.cash.request.CashChargeCommand
import com.racket.cash.response.CashTransactionResponseView
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.Consumer
import org.springframework.http.HttpStatus
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.util.concurrent.ListenableFutureCallback

@Service
class CashConsumerService(

    private val paymentCallService: PaymentCallService,
    private val cashClient: CashFeignClient,
    private val deadLetterQueueKafkaProduceTemplate: KafkaTemplate<String, DeadLetterQueueVO>

) {

    private val log = KotlinLogging.logger { }

    private val paymentRequestRetryCode = PaymentErrorCodeConstants.RETRY_REQUIRED
    private val paymentRequestSuccessCode = HttpStatus.OK.value()

    @KafkaListener(
        topics = ["cash"], groupId = "racket"
    )
    fun consumeChargingProcess(
        @Payload message: String,
        consumer: Consumer<String, String>
    ) {
        try {
            val requestTransactionData = this.getRequestTransactionData(transactionId = message)

            val response =
                this.paymentCallService.call(accountId = requestTransactionData.accountId, amount = requestTransactionData.amount)
            when (response.code) {
                this.paymentRequestRetryCode -> throw RetryPaymentCallRequiredException()
                this.paymentRequestSuccessCode -> {
                    try {
                        this.callCashApiToSaveData(requestTransactionData)
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
                }

                else -> throw ChargePayException("Payment Api Call Failed. - ${response.message}")
            }
        } catch (e: Exception) {
            val deadLetterType = when (e) {
                is ChargePayException -> DeadLetterType.INSERT_DB
                is RetryPaymentCallRequiredException -> DeadLetterType.RETRY
                else -> DeadLetterType.REQUEST_ADMIN
            }
            this.publishCashDeadLetterQueue(
                topic = deadLetterType,
                value = DeadLetterQueueVO(payload = message, errorMessage = e.message ?: "Exception: ${e::class.simpleName}")
            )
        }
        consumer.commitSync()
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
    private fun callCashApiToSaveData(chargeRequestTransactionData: CashTransactionResponseView) =
        this.cashClient.completeCharge(
            CashChargeCommand(
                userId = chargeRequestTransactionData.userId,
                amount = chargeRequestTransactionData.amount,
                transactionId = chargeRequestTransactionData.transactionId,
                accountId = chargeRequestTransactionData.accountId,
                eventType = chargeRequestTransactionData.eventType,
                status = CashTransactionStatusType.COMPLETED
            )
        )

    private fun publishCashDeadLetterQueue(topic: String, value: DeadLetterQueueVO) {
        val listenableFuture: ListenableFuture<SendResult<String, DeadLetterQueueVO>> =
            this.deadLetterQueueKafkaProduceTemplate.send(topic, value)
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
