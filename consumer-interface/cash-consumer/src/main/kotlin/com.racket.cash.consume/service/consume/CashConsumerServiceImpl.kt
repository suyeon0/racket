package com.racket.cash.consume.service.consume

import com.racket.api.payment.presentation.PaymentErrorCodeConstants
import com.racket.api.payment.presentation.RetryPaymentCallRequiredException
import com.racket.cash.consume.client.CashFeignClient
import com.racket.cash.consume.service.PaymentCallService
import com.racket.cash.enums.CashTransactionStatusType
import com.racket.cash.exception.*
import com.racket.cash.request.CashChargeCommand
import com.racket.cash.response.CashTransactionResponseView
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class CashConsumerServiceImpl(

    private val paymentCallService: PaymentCallService,
    private val cashClient: CashFeignClient

) : CashConsumerService {

    private val log = KotlinLogging.logger { }

    @KafkaListener(
        topics = ["cash"], groupId = "racket"
    )
    override fun consumeChargingProcess(message: String) {
        val requestTransactionData = this.getRequestTransactionData(transactionId = message)

        val response =
            this.paymentCallService.call(accountId = requestTransactionData.accountId, amount = requestTransactionData.amount)
        when (response.code) {
            PaymentErrorCodeConstants.RETRY_REQUIRED -> throw RetryPaymentCallRequiredException()
            HttpStatus.OK.value() -> {
                try {
                    this.callCashApiToSaveData(requestTransactionData)
                } catch (e: UpdateDataAsChargingCompletedException) {
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

            else -> throw ChargePayException("Payment Api Call Failed. - ${response.desc}")
        }
    }

    override fun getRequestTransactionData(transactionId: String): CashTransactionResponseView {
        val transactionDataList: ArrayList<CashTransactionResponseView>
        try {
            val response = cashClient.getTransactionList(transactionId = transactionId)
            transactionDataList = response.body as ArrayList<CashTransactionResponseView>
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
    private fun callCashApiToSaveData(chargeRequestTransactionData: CashTransactionResponseView) {
        val command = CashChargeCommand(
            userId = chargeRequestTransactionData.userId,
            amount = chargeRequestTransactionData.amount,
            transactionId = chargeRequestTransactionData.transactionId,
            accountId = chargeRequestTransactionData.accountId,
            eventType = chargeRequestTransactionData.eventType,
            status = CashTransactionStatusType.COMPLETED
        )
        this.cashClient.completeCharge(command)
    }

}