package com.racket.cash.consume.service.consume

import com.racket.cash.consume.client.CashFeignClient
import com.racket.cash.consume.service.PaymentCallService
import com.racket.cash.enums.CashTransactionStatusType
import com.racket.cash.exception.ChargePayException
import com.racket.cash.exception.InvalidChargingTransactionException
import com.racket.cash.exception.UpdateDataAsChargingCompletedException
import com.racket.cash.request.CashChargeCommand
import com.racket.cash.response.CashTransactionResponseView
import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class CashConsumerServiceImpl(

    val paymentCallService: PaymentCallService,
    val cashClient: CashFeignClient

    ) : CashConsumerService {

    private val log = KotlinLogging.logger { }

    @KafkaListener(
        topics = ["cash"]
    )
    override fun consumeChargingProcess(message: String) {
        val transactionDataList = this.cashClient.getTransaction(transactionId = ObjectId(message)).body
            ?: throw ChargePayException("임시 트랜잭션 데이터를 가져오지 못했습니다-${message}")

        this.validateTransactionData(transactionDataList)

        val requestTransactionData: CashTransactionResponseView =
            transactionDataList.stream()
                .filter { v-> v.status == CashTransactionStatusType.REQUEST }
                .toList()[0]

        this.paymentCallService.call(accountId = requestTransactionData.accountId, amount = requestTransactionData.amount)
        try {
            val balance = this.callCashApiToSaveData(requestTransactionData).body!!.balance
            log.info { "=========== 충전 완료 DB 반영 성공 -> 잔액 :${balance} 원" }
        } catch (e: UpdateDataAsChargingCompletedException) {
            log.info { "=========== 충전 완료 DB 반영 실패" }
        }
    }

    // 결제 API 호출 하기 전 validation
    private fun validateTransactionData(transactionDataList: List<CashTransactionResponseView>) {
        val isExistCompleteTransactionLog = transactionDataList.stream()
            .anyMatch { v -> v.status == CashTransactionStatusType.COMPLETED }
        if (isExistCompleteTransactionLog) {
            throw InvalidChargingTransactionException("status is invalid")
        }
    }

    // 충전 결과 DB 반영을 위한 Cash Api 호출
    private fun callCashApiToSaveData(chargeRequestTransactionData: CashTransactionResponseView) =
        this.cashClient.completeCharge(
            CashChargeCommand(
                userId = chargeRequestTransactionData.userId,
                amount = chargeRequestTransactionData.amount,
                transactionId = chargeRequestTransactionData.transactionId.toString(),
                accountId = chargeRequestTransactionData.accountId,
                eventType = chargeRequestTransactionData.eventType,
                status = CashTransactionStatusType.COMPLETED
            )
        )

}